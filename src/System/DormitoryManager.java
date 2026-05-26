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
        int occupiedBeds = getOccupiedBedCount(dormitory.getDormitoryId());
        if (dormitory.getBedCount() < occupiedBeds) {
            throw new SQLException("床位数不能小于当前入住人数：" + occupiedBeds);
        }

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

    public Dormitory getDormitoryById(int dormitoryId) throws SQLException {
        String sql = "SELECT * FROM Dormitory WHERE dormitory_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Dormitory(
                            rs.getInt("dormitory_id"),
                            rs.getString("building"),
                            rs.getInt("floor"),
                            rs.getString("room_number"),
                            rs.getInt("bed_count"),
                            rs.getDouble("price")
                    );
                }
            }
        }
        return null;
    }

    public Dormitory getDormitoryByBuildingAndRoom(String building, String roomNumber) throws SQLException {
        String sql = "SELECT * FROM Dormitory WHERE building = ? AND room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, building);
            stmt.setString(2, roomNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Dormitory(
                            rs.getInt("dormitory_id"),
                            rs.getString("building"),
                            rs.getInt("floor"),
                            rs.getString("room_number"),
                            rs.getInt("bed_count"),
                            rs.getDouble("price")
                    );
                }
            }
        }
        return null;
    }

    public int getOccupiedBedCount(int dormitoryId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Student WHERE dormitory_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getRemainingBeds(int dormitoryId) throws SQLException {
        Dormitory dormitory = getDormitoryById(dormitoryId);
        if (dormitory == null) {
            throw new SQLException("宿舍ID不存在。");
        }
        return dormitory.getBedCount() - getOccupiedBedCount(dormitoryId);
    }

    public boolean isBedNumberAvailable(int dormitoryId, String bedNumber) throws SQLException {
        return isBedNumberAvailable(dormitoryId, bedNumber, 0);
    }

    public boolean isBedNumberAvailable(int dormitoryId, String bedNumber, int excludedStudentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Student WHERE dormitory_id = ? AND bed_number = ? AND student_id <> ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);
            stmt.setString(2, bedNumber);
            stmt.setInt(3, excludedStudentId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }
}
