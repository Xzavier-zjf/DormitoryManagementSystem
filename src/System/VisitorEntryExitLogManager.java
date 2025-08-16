package System;

import JDBC.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitorEntryExitLogManager {



    // 记录外来人员的出入信息
    public void logVisitorEntryExit(String visitorName, String visitorPhone, String visitorPurpose, Timestamp entryTime, Timestamp exitTime) throws SQLException {
        String sql = "INSERT INTO VisitorEntryExitLog (visitor_name, visitor_phone, visitor_purpose, entry_timestamp, exit_timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            try {
                stmt.setString(1, visitorName);
                stmt.setString(2, visitorPhone);
                stmt.setString(3, visitorPurpose);
                stmt.setTimestamp(4, entryTime);
                stmt.setTimestamp(5, exitTime);
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    conn.commit();
                    System.out.println(rowsInserted + " rows inserted successfully.");
                } else {
                    System.out.println("No rows inserted.");
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // 更新访客出入记录
    public void updateVisitorEntryExitLog(VisitorEntryExitLog log) throws SQLException {
        String sql = "UPDATE VisitorEntryExitLog SET visitor_name = ?, visitor_phone = ?, visitor_purpose = ?, entry_timestamp = ?, exit_timestamp = ? WHERE visitor_log_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, log.getVisitorName());
            stmt.setString(2, log.getVisitorPhone());
            stmt.setString(3, log.getVisitorPurpose());
            stmt.setTimestamp(4, log.getEntryTime());
            stmt.setTimestamp(5, log.getExitTime());
            stmt.setInt(6, log.getLogId());
            stmt.executeUpdate();
        }
    }

    // 删除访客出入记录
    public void deleteVisitorEntryExitLog(int logId) throws SQLException {
        String sql = "DELETE FROM VisitorEntryExitLog WHERE visitor_log_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, logId);
            stmt.executeUpdate();
        }
    }

    // 根据访客电话查询出入记录
    public List<VisitorEntryExitLog> getEntryExitLogsByVisitorPhone(String visitorPhone) throws SQLException {
        List<VisitorEntryExitLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM VisitorEntryExitLog WHERE visitor_phone = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, visitorPhone);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(new VisitorEntryExitLog(
                            rs.getInt("visitor_log_id"),
                            rs.getString("visitor_name"),
                            rs.getString("visitor_phone"),
                            rs.getString("visitor_purpose"),
                            rs.getTimestamp("entry_timestamp"),
                            rs.getTimestamp("exit_timestamp")
                    ));
                }
            }
        }
        return logs;
    }

    // 获取所有外来人员的出入记录
    public List<VisitorEntryExitLog> getAllVisitorEntryExitLogs() throws SQLException {
        List<VisitorEntryExitLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM VisitorEntryExitLog";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                logs.add(new VisitorEntryExitLog(
                        rs.getInt("visitor_log_id"),
                        rs.getString("visitor_name"),
                        rs.getString("visitor_phone"),
                        rs.getString("visitor_purpose"),
                        rs.getTimestamp("entry_timestamp"),
                        rs.getTimestamp("exit_timestamp")
                ));
            }
        }
        return logs;
    }
}
