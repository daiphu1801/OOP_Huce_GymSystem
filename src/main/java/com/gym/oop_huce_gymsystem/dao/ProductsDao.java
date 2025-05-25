package com.gym.oop_huce_gymsystem.dao;

import com.gym.oop_huce_gymsystem.controller.RevenuaController.RevenueController;
import com.gym.oop_huce_gymsystem.model.Products;
import com.gym.oop_huce_gymsystem.util.AppContext;
import com.gym.oop_huce_gymsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDao {

    // Thêm sản phẩm
    public void addProduct(Products product) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Thêm sản phẩm vào bảng products
            String insertProductSql = "INSERT INTO products (name, price, quantity, quantity_sold) VALUES (?, ?, ?, ?)";
            String productId = null;
            try (PreparedStatement stmt = conn.prepareStatement(insertProductSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, product.getName());
                stmt.setDouble(2, product.getPrice());
                stmt.setInt(3, product.getQuantity());
                stmt.setInt(4, product.getQuantitySold());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không thể thêm sản phẩm: Không có hàng nào được thêm.");
                }

                // Lấy product_id vừa được tạo
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productId = generatedKeys.getString(1);
                    } else {
                        throw new SQLException("Không thể lấy product_id sau khi thêm sản phẩm.");
                    }
                }
            }

            // Nếu quantity_sold > 0, ghi giao dịch vào revenue
            int quantitySold = product.getQuantitySold();
            if (quantitySold > 0) {
                // Kiểm tra số lượng tồn kho
                if (product.getQuantity() < quantitySold) {
                    throw new SQLException("Số lượng tồn kho không đủ: " + product.getQuantity());
                }

                String insertRevenueSql = "INSERT INTO revenue (source_type, amount, transaction_date, product_id, quantity_sold, payment_method, description) " +
                        "VALUES (?, ?, NOW(), ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertRevenueSql)) {
                    stmt.setString(1, "PRODUCT");
                    stmt.setDouble(2, product.getPrice() * quantitySold); // Tính amount = price * quantity_sold
                    stmt.setString(3, productId); // Sử dụng product_id vừa lấy
                    stmt.setInt(4, quantitySold);
                    stmt.setString(5, "CASH"); // Giá trị mặc định
                    stmt.setString(6, "Thêm sản phẩm với số lượng bán ban đầu");
                    stmt.executeUpdate();
                }

                // Cập nhật quantity trong products
                String updateQuantitySql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateQuantitySql)) {
                    stmt.setInt(1, quantitySold);
                    stmt.setString(2, productId);
                    stmt.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Đã thêm sản phẩm thành công: " + product.getName() + " với ID: " + productId);

            // Làm mới biểu đồ sau khi thêm sản phẩm
            RevenueController revenueController = AppContext.getInstance().getRevenueController();
            if (revenueController != null) {
                revenueController.refreshMonthlyRevenue();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Sửa sản phẩm
    public void updateProduct(Products product) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Lấy thông tin sản phẩm hiện tại
            String selectSql = "SELECT quantity, quantity_sold FROM products WHERE product_id = ?";
            int currentQuantity;
            int currentQuantitySold;
            try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
                stmt.setString(1, product.getProductId());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Không tìm thấy sản phẩm với ID: " + product.getProductId());
                    }
                    currentQuantity = rs.getInt("quantity");
                    currentQuantitySold = rs.getInt("quantity_sold");
                }
            }

            // Tính số lượng bán thêm
            int newQuantitySold = product.getQuantitySold();
            int additionalQuantitySold = newQuantitySold - currentQuantitySold;

            // Nếu có số lượng bán thêm, ghi giao dịch vào revenue
            if (additionalQuantitySold > 0) {
                // Kiểm tra số lượng tồn kho
                if (currentQuantity < additionalQuantitySold) {
                    throw new SQLException("Số lượng tồn kho không đủ: " + currentQuantity);
                }

                String insertRevenueSql = "INSERT INTO revenue (source_type, amount, transaction_date, product_id, quantity_sold, payment_method, description) " +
                        "VALUES (?, ?, NOW(), ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertRevenueSql)) {
                    stmt.setString(1, "PRODUCT");
                    stmt.setDouble(2, product.getPrice() * additionalQuantitySold); // Tính amount = price * quantity_sold
                    stmt.setString(3, product.getProductId());
                    stmt.setInt(4, additionalQuantitySold);
                    stmt.setString(5, "CASH"); // Giá trị mặc định
                    stmt.setString(6, "Cập nhật số lượng bán sản phẩm");
                    stmt.executeUpdate();
                }

                // Cập nhật quantity trong products
                String updateQuantitySql = "UPDATE products SET quantity = ? WHERE product_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateQuantitySql)) {
                    stmt.setInt(1, currentQuantity - additionalQuantitySold);
                    stmt.setString(2, product.getProductId());
                    stmt.executeUpdate();
                }
            }

            // Cập nhật thông tin sản phẩm trong products
            String updateProductSql = "UPDATE products SET name = ?, price = ?, quantity = ?, quantity_sold = ? WHERE product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateProductSql)) {
                stmt.setString(1, product.getName());
                stmt.setDouble(2, product.getPrice());
                stmt.setInt(3, product.getQuantity());
                stmt.setInt(4, product.getQuantitySold());
                stmt.setString(5, product.getProductId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không tìm thấy sản phẩm với ID: " + product.getProductId());
                }
            }

            conn.commit(); // Commit transaction

            // Làm mới biểu đồ sau khi cập nhật sản phẩm
            RevenueController revenueController = AppContext.getInstance().getRevenueController();
            if (revenueController != null) {
                revenueController.refreshMonthlyRevenue();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new SQLException("Lỗi khi cập nhật sản phẩm: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Xóa sản phẩm
    public void deleteProduct(String productId) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Xóa các bản ghi trong bảng revenue có product_id tương ứng
            String deleteRevenueSql = "DELETE FROM revenue WHERE product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteRevenueSql)) {
                stmt.setString(1, productId);
                int rowsAffectedRevenue = stmt.executeUpdate();
                System.out.println("Đã xóa " + rowsAffectedRevenue + " bản ghi trong bảng revenue cho product_id: " + productId);
            }

            // Xóa sản phẩm trong bảng products
            String deleteProductSql = "DELETE FROM products WHERE product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteProductSql)) {
                stmt.setString(1, productId);
                int rowsAffectedProduct = stmt.executeUpdate();
                if (rowsAffectedProduct == 0) {
                    throw new SQLException("Không tìm thấy sản phẩm với ID: " + productId);
                }
                System.out.println("Đã xóa sản phẩm với product_id: " + productId);
            }

            conn.commit(); // Commit transaction

            // Làm mới biểu đồ sau khi xóa sản phẩm
            RevenueController revenueController = AppContext.getInstance().getRevenueController();
            if (revenueController != null) {
                revenueController.refreshMonthlyRevenue();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new SQLException("Lỗi khi xóa sản phẩm: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Lấy tất cả sản phẩm
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

    // Lấy sản phẩm theo ID
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
        } catch (SQLException e) {
            throw new SQLException("Không tìm thấy sản phẩm với ID: " + productId);
        }
        return null;
    }
}