package System;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import JDBC.DatabaseConnection;

public class DormitoryManager {

    // 添加宿舍信息
    public void addDormitory(String building, int floor, String roomNumber, int bedCount, double price) throws SQLException {
        String sql = "INSERT INTO Dormitory (building, floor, room_number, bed_count, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, building);
            stmt.setInt(2, floor);
            stmt.setString(3, roomNumber);
            stmt.setInt(4, bedCount);
            stmt.setDouble(5, price);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                conn.commit();  // Explicitly commit the transaction
                System.out.println("Dormitory added successfully!");
            } else {
                throw new SQLException("宿舍添加失败。");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // Print exception stack trace
            throw ex;
        }
    }

    // 更新宿舍信息
    public void updateDormitory(Dormitory dormitory) throws SQLException {
        String sql = "UPDATE Dormitory SET building = ?, floor = ?, room_number = ?, bed_count = ?, price = ? WHERE dormitory_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dormitory.getBuilding());
            stmt.setInt(2, dormitory.getFloor());
            stmt.setString(3, dormitory.getRoomNumber());
            stmt.setInt(4, dormitory.getBedCount());
            stmt.setDouble(5, dormitory.getPrice());
            stmt.setInt(6, dormitory.getDormitoryId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                conn.commit();  // Explicitly commit the transaction
                System.out.println("Dormitory updated successfully!");
            } else {
                throw new SQLException("未找到指定ID的宿舍。");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // Print exception stack trace
            throw ex;
        }
    }

    // 删除宿舍信息
    public void deleteDormitory(int dormitoryId) throws SQLException {
        String sql = "DELETE FROM Dormitory WHERE dormitory_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                conn.commit();  // Explicitly commit the transaction
                System.out.println("Dormitory deleted successfully!");
            } else {
                throw new SQLException("未找到指定ID的宿舍。");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // Print exception stack trace
            throw ex;
        }
    }


    // 根据楼栋查询宿舍信息
    public List<Dormitory> getDormitoriesByBuilding(String building) throws SQLException {
        String sql = "SELECT * FROM Dormitory WHERE building = ?";
        List<Dormitory> dormitories = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, building);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dormitory dormitory = new Dormitory(
                            rs.getInt("dormitory_id"),
                            rs.getString("building"),
                            rs.getInt("floor"),
                            rs.getString("room_number"),
                            rs.getInt("bed_count"),
                            rs.getDouble("price")
                    );
                    dormitories.add(dormitory);
                }
            }

            if (!dormitories.isEmpty()) {
                System.out.println("Dormitory query successfully!");
            } else {
                System.out.println("No dormitory found with the given building.");
            }
        }

        return dormitories;
    }

    public List<Dormitory> getAllDormitories() throws SQLException {
        List<Dormitory> dormitories = new ArrayList<>();
        String sql = "SELECT * FROM Dormitory";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dormitories.add(new Dormitory(
                        rs.getInt("dormitory_id"),
                        rs.getString("building"),
                        rs.getInt("floor"),
                        rs.getString("room_number"),
                        rs.getInt("bed_count"),
                        rs.getDouble("price")
                ));
            }
        }
        return dormitories;
    }

}
