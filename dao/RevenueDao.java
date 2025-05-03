package com.gym.oop_huce_gymsystem.dao;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.util.*;



public class RevenueDao {
    public void addRevenue(Revenue revenue) throws SQLException {
        String query = "INSERT INTO revenue (source_type, amount, description, transaction_date) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, revenue.getSource_type());
            stmt.setDouble(2, revenue.getAmount());
            stmt.setString(3, revenue.getDescription());
            stmt.setDate(5, java.sql.Date.valueOf(revenue.getTransaction_date()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã thêm doanh thu thành công: " + revenue.getSource_type());
            } else {
                throw new SQLException("Không thể thêm doanh thu : Không có hàng nào được thêm.");
            }
        } catch (SQLException e) {
            throw e;
        }

    }
    public void updateRevenue(Revenue revenue) throws SQLException {
        String query = "UPDATE revenue SET source_type = ?, amount = ?, description = ?, " +
                "transaction_date=? WHERE revenue_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, revenue.getSource_type());
            stmt.setDouble(2, revenue.getAmount());
            stmt.setString(3, revenue.getDescription());
            stmt.setDate(4, java.sql.Date.valueOf(revenue.getTransaction_date()));
            stmt.setString(5, revenue.getRevenueId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy doanh thu với ID: " + revenue.getRevenueId());
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi cập nhật doanh thu: " + e.getMessage(), e);
        }
    }
    public void deleteRevenue(int revenueId) throws SQLException {
        String query = "DELETE FROM revenue WHERE revenue_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, revenueId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy doanh thu với ID: " + revenueId);
            }
        }
    }



}

