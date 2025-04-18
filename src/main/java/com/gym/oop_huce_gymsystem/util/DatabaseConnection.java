package com.gym.oop_huce_gymsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
//    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/MySQL?allowPublicKeyRetrieval=true&useSSL=false";
//    private static final String USER = "root";
//    private static final String PASSWORD = "1234";

    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=gym_management;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "12345678";

    public static Connection getConnection() throws SQLException {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy JDBC Driver cho MySQL.");
            e.printStackTrace();
        }

        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
}
