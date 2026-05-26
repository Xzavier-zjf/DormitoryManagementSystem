package System;

import JDBC.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLogin {
    public boolean ensureAdminTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Admin ("
                + "admin_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "username VARCHAR(50) NOT NULL UNIQUE,"
                + "password VARCHAR(100) NOT NULL"
                + ")";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String username, String password) {
        String query = "SELECT password FROM Admin WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    return password.equals(storedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean register(String username, String password) {
        if (isUsernameTaken(username)) {
            return false;
        }

        String query = "INSERT INTO Admin (username, password) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isUsernameTaken(String username) {
        String query = "SELECT 1 FROM Admin WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        if (login(username, oldPassword)) {
            String query = "UPDATE Admin SET password = ? WHERE username = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, username);
                int result = preparedStatement.executeUpdate();
                connection.commit();
                return result > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean deleteAccount(String username, String password) {
        if (login(username, password)) {
            String query = "DELETE FROM Admin WHERE username = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                int result = preparedStatement.executeUpdate();
                connection.commit();
                return result > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
