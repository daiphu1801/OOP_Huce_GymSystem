package com.gym.oop_huce_gymsystem.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.util.*;

public class ProductsDao {

    // Thêm sản phẩm
    public void addProduct(Products product) throws Exception {
        String query = "INSERT INTO products (name, price, quantity, quantity_sold) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setInt(4,product.getQuantitySold());


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

    // Sửa sản phẩm
    public void updateProduct(Products product) throws SQLException {
        String query = "UPDATE products SET name = ?, price = ?, quantity = ?, " +
                "quantity_sold = ? WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setInt(4, product.getQuantitySold());
            stmt.setString(5, product.getProductId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy sản phẩm với ID: " + product.getProductId());
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi cập nhật sản phẩm: " + e.getMessage(), e);
        }
    }

    // Xóa sản phẩm
    public void deleteProduct(String productId) throws SQLException {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy sản phầm với ID: " + productId);
            }
        }
    }

    //Lấy tất cả sản paharm
    public List<Products> getAllProducts() throws SQLException {
        List<Products> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Products product = new Products(
                        rs.getString("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("quantity_sold")
                );
                products.add(product);
            }
        }
        return products;
    }

    public Products getProductById(String productId) throws SQLException {
        String query = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                   Products product = new Products();
                   product.setProductId(rs.getString("product_id"));
                   product.setName(rs.getString("name"));
                   product.setPrice(rs.getDouble("price"));
                   product.setQuantity(rs.getInt("quantity"));
                   product.setQuantitySold(rs.getInt("quantity_sold"));
                   return product;
                }
            }
        }catch (SQLException e) {
            throw new SQLException("Không tìm thấy sản phẩm với ID: " + productId);
        }
        return null;
    }
}


