package com.gym.oop_huce_gymsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/MySQL?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";       // Thay đổi thành tài khoản của bạn
    private static final String PASSWORD = "1234"; // Thay đổi thành mật khẩu của bạn

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy JDBC Driver cho MySQL.");
            e.printStackTrace();
        }

        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
}
