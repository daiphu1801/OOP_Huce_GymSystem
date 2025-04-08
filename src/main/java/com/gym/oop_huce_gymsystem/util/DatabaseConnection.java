package com.gym.oop_huce_gymsystem.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=GymManagement";
    private static final String USER = "sa";  // Hoặc user bạn đặt
    private static final String PASSWORD = "123"; // Đổi thành mật khẩu của bạn

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy JDBC Driver.");
            e.printStackTrace();
        }

        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
}
