package com.gym.oop_huce_gymsystem.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.util.*;

public class ProductsDao {
    // them
    public void addProduct(Products product) throws Exception {
        String query = "INSERT INTO products (name, price, quantity, create_at) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getPrice());
            stmt.setString(3, product.getQuantity());
            stmt.setDate(4, product.getCreate_at());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã thêm sản phẩm thành công: " + product.getName());
            } else {
                throw new SQLException("Không thể thêm sản phẩm: Không có hàng nào được thêm.");
            }
        } catch (SQLException e) {
            throw e;
        }
    }
    // sửa
    public void updateProduct(Products product) throws SQLException {
        String query = "UPDATE product SET name = ?, price = ?, quantity = ?, " +
                "create_at=? WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getPrice());
            stmt.setString(3, product.getQuantity());
            stmt.setDate(4, product.getCreate_at());
            stmt.setInt(5, product.getProductId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy sản phẩm với ID: " + product.getProductId());
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi cập nhật sản phẩm: " + e.getMessage(), e);
        }
    }
    //xóa
    public void deleteProduct(int productId) throws SQLException {
        String query = "DELETE FROM product WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy sản phầm với ID: " + productId);
            }
        }
    }


    }


