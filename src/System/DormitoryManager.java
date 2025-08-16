package System;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import JDBC.DatabaseConnection;

public class DormitoryManager {

    public void addDormitory(String building, int floor, String roomNumber, int bedCount, double price) throws SQLException {
        String sql = "INSERT INTO Dormitory (building, floor, room_number, bed_count, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, building);
            stmt.setInt(2, floor);
            stmt.setString(3, roomNumber);
            stmt.setInt(4, bedCount);
            stmt.setDouble(5, price);

            int rowsInserted = stmt.executeUpdate();
            conn.commit();

            if (rowsInserted > 0) {
                System.out.println("Dormitory added successfully!");
            } else {
                System.out.println("Failed to add dormitory.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

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
            conn.commit();

            if (rowsUpdated > 0) {
                System.out.println("Dormitory updated successfully!");
            } else {
                System.out.println("No dormitory found with the given ID.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteDormitory(int dormitoryId) throws SQLException {
        String sql = "DELETE FROM Dormitory WHERE dormitory_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);

            int rowsDeleted = stmt.executeUpdate();
            conn.commit();

            if (rowsDeleted > 0) {
                System.out.println("Dormitory deleted successfully!");
            } else {
                System.out.println("No dormitory found with the given ID.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

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

    public Dormitory getDormitoryById(int dormitoryId) throws SQLException {
        String sql = "SELECT * FROM Dormitory WHERE dormitory_id = ?";
        Dormitory dormitory = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dormitoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dormitory = new Dormitory(
                            rs.getInt("dormitory_id"),
                            rs.getString("building"),
                            rs.getInt("floor"),
                            rs.getString("room_number"),
                            rs.getInt("bed_count"),
                            rs.getDouble("price")
                    );
                }
            }

            if (dormitory != null) {
                System.out.println("Dormitory query by ID successful!");
            } else {
                System.out.println("No dormitory found with the given ID.");
            }
        }

        return dormitory;
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

        if (!dormitories.isEmpty()) {
            System.out.println("Dormitories retrieved successfully.");
        } else {
            System.out.println("No dormitories found in the database.");
        }

        return dormitories;
    }

    public int getRemainingBeds(int dormitoryId) throws SQLException {
        String sql = "SELECT bed_count - (SELECT COUNT(*) FROM Student WHERE dormitory_id = ?) AS remaining_beds FROM Dormitory WHERE dormitory_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);
            stmt.setInt(2, dormitoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("remaining_beds");
                }
            }
        }
        return 0;
    }

    public boolean isBedNumberAvailable(int dormitoryId, String bedNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Student WHERE dormitory_id = ? AND bed_number = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);
            stmt.setString(2, bedNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }

    public List<Dormitory> getDormitoriesNotFullyOccupied() throws SQLException {
        List<Dormitory> dormitories = new ArrayList<>();
        String sql = "SELECT d.dormitory_id, d.building, d.floor, d.room_number, d.bed_count, d.price, " +
                "(SELECT COUNT(*) FROM student WHERE dormitory_id = d.dormitory_id) AS current_occupancy, " +
                "(d.bed_count - (SELECT COUNT(*) FROM student WHERE dormitory_id = d.dormitory_id)) AS remaining_beds " +
                "FROM dormitory d " +
                "WHERE (SELECT COUNT(*) FROM student WHERE dormitory_id = d.dormitory_id) < d.bed_count";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Dormitory dormitory = new Dormitory(
                        rs.getInt("dormitory_id"),
                        rs.getString("building"),
                        rs.getInt("floor"),
                        rs.getString("room_number"),
                        rs.getInt("bed_count"),
                        rs.getDouble("price")
                );
                dormitory.setRemainingBeds(rs.getInt("remaining_beds"));
                dormitories.add(dormitory);
            }
        }
        return dormitories;
    }

    public List<Student> getStudentsByDormitoryId(int dormitoryId) throws SQLException {
        String sql = "SELECT * FROM Student WHERE dormitory_id = ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_number"),
                            rs.getString("name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getString("department"),
                            rs.getInt("grade"),
                            rs.getString("phone"),
                            rs.getString("bed_number"),
                            rs.getString("fee_paid"),
                            rs.getInt("dormitory_id")
                    );
                    students.add(student);
                }
            }
        }
        return students;
    }

}
