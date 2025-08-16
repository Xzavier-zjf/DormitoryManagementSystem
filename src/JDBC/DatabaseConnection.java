package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;


public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=dormitorymanagementsystem;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";


    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);  // 禁用自动提交
        return conn;

    }
}
