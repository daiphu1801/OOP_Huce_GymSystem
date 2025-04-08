package com.gym.oop_huce_gymsystem.util;


import java.sql.Connection;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Kết nối đến SQL Server thành công!");
            } else {
                System.out.println("❌ Kết nối thất bại!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi kết nối cơ sở dữ liệu:");
            e.printStackTrace();
        }
    }
}
