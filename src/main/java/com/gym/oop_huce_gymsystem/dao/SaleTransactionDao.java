package com.gym.oop_huce_gymsystem.dao;

import com.gym.oop_huce_gymsystem.model.SaleTransaction;
import com.gym.oop_huce_gymsystem.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleTransactionDao {

    // Thêm giao dịch bán hàng
    public void addSaleTransaction(SaleTransaction transaction) throws SQLException {
        String query = "INSERT INTO sale_transactions (product_id, quantity_sold, transaction_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transaction.getProductId());
            stmt.setInt(2, transaction.getQuantitySold());
            stmt.setTimestamp(3, Timestamp.valueOf(transaction.getTransactionDate()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã thêm giao dịch bán hàng thành công cho sản phẩm: " + transaction.getProductId());
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi thêm giao dịch bán hàng: " + e.getMessage(), e);
        }
    }

    // Lấy tất cả giao dịch của một sản phẩm
    public List<SaleTransaction> getTransactionsByProductId(String productId) throws SQLException {
        List<SaleTransaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM sale_transactions WHERE product_id = ? ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SaleTransaction transaction = new SaleTransaction(
                            rs.getInt("transaction_id"),
                            rs.getString("product_id"),
                            rs.getInt("quantity_sold"),
                            rs.getTimestamp("transaction_date").toLocalDateTime()
                    );
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy danh sách giao dịch: " + e.getMessage(), e);
        }

        return transactions;
    }

    // Xóa tất cả giao dịch của một sản phẩm (khi xóa sản phẩm)
    public void deleteTransactionsByProductId(String productId) throws SQLException {
        String query = "DELETE FROM sale_transactions WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi xóa giao dịch: " + e.getMessage(), e);
        }
    }
}