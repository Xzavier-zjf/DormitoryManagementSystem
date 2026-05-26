package System;

import JDBC.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class EntryExitLogManager {

    // 记录学生的出入信息
    public void logEntryExit(String studentNumber, Timestamp entryTime, Timestamp exitTime) throws SQLException {
        // SQL 插入语句
        String sql = "INSERT INTO EntryExitLog (student_number, entry_time, exit_time) VALUES (?, ?, ?)";
        // 获取数据库连接和准备 SQL 语句
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // 关闭自动提交
            try {
                // 设置 SQL 语句中的参数
                stmt.setString(1, studentNumber);
                stmt.setTimestamp(2, entryTime);
                stmt.setTimestamp(3, exitTime);
                // 执行插入操作
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new SQLException("学生出入记录添加失败。");
                }
                conn.commit(); // 手动提交事务
                System.out.println(rowsInserted + " rows inserted.");
            } catch (SQLException e) {
                conn.rollback(); // 回滚事务
                throw e;
            } finally {
                conn.setAutoCommit(true); // 恢复自动提交
            }
        }
    }

    // 根据学生学号查询出入记录
    public List<EntryExitLog> getEntryExitLogsByStudentId(String studentNumber) throws SQLException {
        List<EntryExitLog> logs = new ArrayList<>();
        // SQL 查询语句
        String sql = "SELECT * FROM EntryExitLog WHERE student_number = ?";
        // 获取数据库连接和准备 SQL 语句
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentNumber);
            // 执行查询操作
            try (ResultSet rs = stmt.executeQuery()) {
                // 处理查询结果
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
        // SQL 查询语句
        String sql = "SELECT * FROM EntryExitLog";
        // 获取数据库连接和准备 SQL 语句
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            // 处理查询结果
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
