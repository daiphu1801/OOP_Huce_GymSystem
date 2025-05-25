package com.gym.oop_huce_gymsystem.dao;

import com.gym.oop_huce_gymsystem.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RevenueDao {
    private final Connection connection;

    public RevenueDao() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    // Lấy doanh thu từ membership_cards theo tháng
    public Map<String, Map<String, BigDecimal>> getMembershipRevenueByMonth(int year) throws SQLException {
        Map<String, Map<String, BigDecimal>> monthlyRevenue = new HashMap<>();
        String query = "SELECT DATE_FORMAT(registration_date, '%Y-%m') AS period, SUM(price) AS total_amount " +
                "FROM membership_cards " +
                "WHERE YEAR(registration_date) = ? " +
                "GROUP BY DATE_FORMAT(registration_date, '%Y-%m')";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String period = rs.getString("period");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount").divide(BigDecimal.valueOf(1000000)); // Chia cho 1 triệu để hiển thị Triệu VNĐ
                monthlyRevenue.computeIfAbsent(period, k -> new HashMap<>());
                monthlyRevenue.get(period).put("MEMBERSHIP", totalAmount);
            }
        }
        return monthlyRevenue;
    }

    // Lấy doanh thu từ revenue (sản phẩm) theo tháng
    public Map<String, Map<String, BigDecimal>> getProductRevenueByMonth(int year) throws SQLException {
        Map<String, Map<String, BigDecimal>> monthlyRevenue = new HashMap<>();
        String query = "SELECT DATE_FORMAT(r.transaction_date, '%Y-%m') AS period, r.source_type, " +
                "SUM(r.amount) AS total_amount " +
                "FROM revenue r " +
                "WHERE YEAR(r.transaction_date) = ? AND r.source_type = 'PRODUCT' " +
                "GROUP BY DATE_FORMAT(r.transaction_date, '%Y-%m'), r.source_type";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Kiểm tra dữ liệu sản phẩm từ bảng revenue:");
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                String period = rs.getString("period");
                String sourceType = rs.getString("source_type");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount").divide(BigDecimal.valueOf(1000000)); // Chia cho 1 triệu để hiển thị Triệu VNĐ
                System.out.println(" - Period: " + period + ", Source Type: " + sourceType + ", Total Amount: " + totalAmount + " triệu VNĐ");
                monthlyRevenue.computeIfAbsent(period, k -> new HashMap<>());
                monthlyRevenue.get(period).put(sourceType, totalAmount);
            }
            if (rowCount == 0) {
                System.out.println("Không tìm thấy dữ liệu sản phẩm trong bảng revenue cho năm " + year + " với source_type = 'PRODUCT'");
            }
        }
        return monthlyRevenue;
    }
}