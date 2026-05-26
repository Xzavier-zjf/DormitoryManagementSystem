package System;

import JDBC.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VisitorEntryExitLogManager {

    // 记录外来人员的出入信息
    public void logVisitorEntryExit(String visitorName, String visitorPhone, String visitorPurpose, Timestamp entryTime, Timestamp exitTime) throws SQLException {
        // SQL 插入语句
        String sql = "INSERT INTO VisitorEntryExitLog (visitor_name, visitor_phone, visitor_purpose, entry_timestamp, exit_timestamp) VALUES (?, ?, ?, ?, ?)";
        // 获取数据库连接和准备 SQL 语句
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // 关闭自动提交
            try {
                // 设置 SQL 语句中的参数
                stmt.setString(1, visitorName);
                stmt.setString(2, visitorPhone);
                stmt.setString(3, visitorPurpose);
                stmt.setTimestamp(4, entryTime);
                stmt.setTimestamp(5, exitTime);

                // 执行插入操作
                int rowsInserted = stmt.executeUpdate();

                // 检查是否有行被插入，并手动提交事务
                if (rowsInserted > 0) {
                    conn.commit();
                    System.out.println(rowsInserted + " rows inserted successfully.");
                } else {
                    throw new SQLException("访客出入记录添加失败。");
                }
            } catch (SQLException e) {
                conn.rollback(); // 回滚事务
                throw e;
            } finally {
                conn.setAutoCommit(true); // 恢复自动提交
            }
        }
    }

    // 根据访客电话查询出入记录
    public List<VisitorEntryExitLog> getEntryExitLogsByVisitorPhone(String visitorPhone) throws SQLException {
        List<VisitorEntryExitLog> logs = new ArrayList<>();
        // SQL 查询语句
        String sql = "SELECT * FROM VisitorEntryExitLog WHERE visitor_phone = ?";
        // 获取数据库连接和准备 SQL 语句
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, visitorPhone);
            // 执行查询操作
            try (ResultSet rs = stmt.executeQuery()) {
                // 处理查询结果
                while (rs.next()) {
                    VisitorEntryExitLog log = new VisitorEntryExitLog(
                            rs.getInt("visitor_log_id"),
                            rs.getString("visitor_name"),
                            rs.getString("visitor_phone"),
                            rs.getString("visitor_purpose"),
                            rs.getTimestamp("entry_timestamp"),
                            rs.getTimestamp("exit_timestamp")
                    );
                    logs.add(log);
                }
            }
        }
        return logs;
    }

    // 获取所有外来人员的出入记录
    public List<VisitorEntryExitLog> getAllVisitorEntryExitLogs() throws SQLException {
        List<VisitorEntryExitLog> logs = new ArrayList<>();
        // SQL 查询语句
        String sql = "SELECT * FROM VisitorEntryExitLog";
        // 获取数据库连接和准备 SQL 语句
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            // 处理查询结果
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
