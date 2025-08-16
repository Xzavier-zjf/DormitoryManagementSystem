package System;

import JDBC.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntryExitLogManager {

    // 记录学生的出入信息
    public void logEntryExit(String studentNumber, Timestamp entryTime, Timestamp exitTime) throws SQLException {
        String sql = "INSERT INTO EntryExitLog (student_number, entry_time, exit_time) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            try {
                stmt.setString(1, studentNumber);
                stmt.setTimestamp(2, entryTime);
                stmt.setTimestamp(3, exitTime);
                int rowsInserted = stmt.executeUpdate();
                conn.commit();
                System.out.println(rowsInserted + " rows inserted.");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // 更新学生的出入记录
    public void updateEntryExitLog(EntryExitLog log) throws SQLException {
        String sql = "UPDATE EntryExitLog SET student_number = ?, entry_time = ?, exit_time = ? WHERE log_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, log.getStudentNumber());
            stmt.setTimestamp(2, log.getEntryTime());
            stmt.setTimestamp(3, log.getExitTime());
            stmt.setInt(4, log.getLogId());
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    // 删除学生的出入记录
    public void deleteEntryExitLog(int logId) throws SQLException {
        String sql = "DELETE FROM EntryExitLog WHERE log_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, logId);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    // 根据学生学号查询出入记录
    public List<EntryExitLog> getEntryExitLogsByStudentId(String studentNumber) throws SQLException {
        List<EntryExitLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM EntryExitLog WHERE student_number = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EntryExitLog log = new EntryExitLog(
                            rs.getInt("log_id"),
                            rs.getString("student_number"),
                            rs.getTimestamp("entry_time"),
                            rs.getTimestamp("exit_time")
                    );
                    logs.add(log);
                }
            }
        }
        return logs;
    }

    // 获取所有学生的出入记录
    public List<EntryExitLog> getAllEntryExitLogs() throws SQLException {
        List<EntryExitLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM EntryExitLog";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                logs.add(new EntryExitLog(
                        rs.getInt("log_id"),
                        rs.getString("student_number"),
                        rs.getTimestamp("entry_time"),
                        rs.getTimestamp("exit_time")
                ));
            }
        }
        return logs;
    }
}
