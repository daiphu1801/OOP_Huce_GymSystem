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
                "GROUP BY DATE_FORMAT(registration_date, '%Y-%m') " +
                "ORDER BY period";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Kiểm tra doanh thu membership từ bảng membership_cards:");
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                String period = rs.getString("period");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                if (totalAmount != null) {
                    totalAmount = totalAmount.divide(BigDecimal.valueOf(1000000)); // Chia cho 1 triệu để hiển thị Triệu VNĐ
                } else {
                    totalAmount = BigDecimal.ZERO;
                }
                System.out.println(" - Period: " + period + ", Total Amount: " + totalAmount + " triệu VNĐ");
                monthlyRevenue.computeIfAbsent(period, k -> new HashMap<>());
                monthlyRevenue.get(period).put("MEMBERSHIP", totalAmount);
            }
            if (rowCount == 0) {
                System.out.println("Không tìm thấy dữ liệu membership trong bảng membership_cards cho năm " + year);
            }
        }
        return monthlyRevenue;
    }

    // Lấy doanh thu từ sản phẩm theo tháng (từ bảng sale_transactions và products)
    public Map<String, Map<String, BigDecimal>> getProductRevenueByMonth(int year) throws SQLException {
        Map<String, Map<String, BigDecimal>> monthlyRevenue = new HashMap<>();
        String query = "SELECT DATE_FORMAT(st.transaction_date, '%Y-%m') AS period, " +
                "SUM(st.quantity_sold * p.price) AS total_amount " +
                "FROM sale_transactions st " +
                "JOIN products p ON st.product_id = p.product_id " +
                "WHERE YEAR(st.transaction_date) = ? " +
                "GROUP BY DATE_FORMAT(st.transaction_date, '%Y-%m') " +
                "ORDER BY period";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Kiểm tra doanh thu sản phẩm từ bảng sale_transactions:");
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                String period = rs.getString("period");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                if (totalAmount != null) {
                    totalAmount = totalAmount.divide(BigDecimal.valueOf(1000000)); // Chia cho 1 triệu để hiển thị Triệu VNĐ
                } else {
                    totalAmount = BigDecimal.ZERO;
                }
                System.out.println(" - Period: " + period + ", Total Amount: " + totalAmount + " triệu VNĐ");
                monthlyRevenue.computeIfAbsent(period, k -> new HashMap<>());
                monthlyRevenue.get(period).put("PRODUCT", totalAmount);
            }
            if (rowCount == 0) {
                System.out.println("Không tìm thấy dữ liệu giao dịch sản phẩm trong bảng sale_transactions cho năm " + year);
            }
        }
        return monthlyRevenue;
    }

    // Lấy doanh thu từ membership_cards theo quý
    public Map<String, Map<String, BigDecimal>> getMembershipRevenueByQuarter(int year) throws SQLException {
        Map<String, Map<String, BigDecimal>> quarterlyRevenue = new HashMap<>();
        String query = "SELECT CONCAT('Q', QUARTER(registration_date)) AS period, SUM(price) AS total_amount " +
                "FROM membership_cards " +
                "WHERE YEAR(registration_date) = ? " +
                "GROUP BY QUARTER(registration_date) " +
                "ORDER BY QUARTER(registration_date)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Kiểm tra doanh thu membership theo quý từ bảng membership_cards:");
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                String period = rs.getString("period");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                if (totalAmount != null) {
                    totalAmount = totalAmount.divide(BigDecimal.valueOf(1000000)); // Chia cho 1 triệu để hiển thị Triệu VNĐ
                } else {
                    totalAmount = BigDecimal.ZERO;
                }
                System.out.println(" - Period: " + period + ", Total Amount: " + totalAmount + " triệu VNĐ");
                quarterlyRevenue.computeIfAbsent(period, k -> new HashMap<>());
                quarterlyRevenue.get(period).put("MEMBERSHIP", totalAmount);
            }
            if (rowCount == 0) {
                System.out.println("Không tìm thấy dữ liệu membership theo quý trong bảng membership_cards cho năm " + year);
            }
        }
        return quarterlyRevenue;
    }

    // Lấy doanh thu từ sản phẩm theo quý
    public Map<String, Map<String, BigDecimal>> getProductRevenueByQuarter(int year) throws SQLException {
        Map<String, Map<String, BigDecimal>> quarterlyRevenue = new HashMap<>();
        String query = "SELECT CONCAT('Q', QUARTER(st.transaction_date)) AS period, " +
                "SUM(st.quantity_sold * p.price) AS total_amount " +
                "FROM sale_transactions st " +
                "JOIN products p ON st.product_id = p.product_id " +
                "WHERE YEAR(st.transaction_date) = ? " +
                "GROUP BY QUARTER(st.transaction_date) " +
                "ORDER BY QUARTER(st.transaction_date)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Kiểm tra doanh thu sản phẩm theo quý từ bảng sale_transactions:");
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                String period = rs.getString("period");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                if (totalAmount != null) {
                    totalAmount = totalAmount.divide(BigDecimal.valueOf(1000000)); // Chia cho 1 triệu để hiển thị Triệu VNĐ
                } else {
                    totalAmount = BigDecimal.ZERO;
                }
                System.out.println(" - Period: " + period + ", Total Amount: " + totalAmount + " triệu VNĐ");
                quarterlyRevenue.computeIfAbsent(period, k -> new HashMap<>());
                quarterlyRevenue.get(period).put("PRODUCT", totalAmount);
            }
            if (rowCount == 0) {
                System.out.println("Không tìm thấy dữ liệu giao dịch sản phẩm theo quý trong bảng sale_transactions cho năm " + year);
            }
        }
        return quarterlyRevenue;
    }

    // Method để kiểm tra dữ liệu có trong database
    public void checkDatabaseData(int year) throws SQLException {
        System.out.println("=== KIỂM TRA DỮ LIỆU DATABASE ===");

        // Kiểm tra bảng membership_cards
        String membershipQuery = "SELECT COUNT(*) as count, MIN(registration_date) as min_date, MAX(registration_date) as max_date " +
                "FROM membership_cards WHERE YEAR(registration_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(membershipQuery)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Membership Cards năm " + year + ":");
                System.out.println(" - Số lượng: " + rs.getInt("count"));
                System.out.println(" - Ngày sớm nhất: " + rs.getDate("min_date"));
                System.out.println(" - Ngày muộn nhất: " + rs.getDate("max_date"));
            }
        }

        // Kiểm tra bảng sale_transactions
        String productQuery = "SELECT COUNT(*) as count, MIN(transaction_date) as min_date, MAX(transaction_date) as max_date " +
                "FROM sale_transactions WHERE YEAR(transaction_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(productQuery)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Sale Transactions năm " + year + ":");
                System.out.println(" - Số lượng: " + rs.getInt("count"));
                System.out.println(" - Ngày sớm nhất: " + rs.getDate("min_date"));
                System.out.println(" - Ngày muộn nhất: " + rs.getDate("max_date"));
            }
        }
        System.out.println("=== KẾT THÚC KIỂM TRA ===");
    }
}