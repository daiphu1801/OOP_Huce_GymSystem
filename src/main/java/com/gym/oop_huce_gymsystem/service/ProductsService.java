package com.gym.oop_huce_gymsystem.service;

import com.gym.oop_huce_gymsystem.dao.ProductsDao;
import com.gym.oop_huce_gymsystem.model.Products;
import java.sql.SQLException;
import java.util.List;

public class ProductsService {
    private final ProductsDao productsDao;

    public ProductsService() {
        this.productsDao = new ProductsDao();
    }

    // Thêm sản phẩm với kiểm tra dữ liệu đầu vào
    public void addProduct(Products product) throws Exception {
        validateProduct(product);
        productsDao.addProduct(product);
    }

    // Sửa sản phẩm
    public void updateProduct(Products product) throws Exception {
        validateProduct(product);
        if (product.getProductId() == null || product.getProductId().trim().isEmpty()) {
            throw new IllegalArgumentException("ID sản phẩm không hợp lệ.");
        }
        productsDao.updateProduct(product);
    }

    // Xóa sản phẩm
    public void deleteProduct(String productId) throws Exception {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID sản phẩm không hợp lệ.");
        }
        productsDao.deleteProduct(productId);
    }

    // Lấy tất cả sản phẩm
    public List<Products> getAllProducts() throws SQLException {
        try {
            return productsDao.getAllProducts();
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage(), e);
        }
    }

    // Lấy sản phẩm theo ID
    public Products getProductById(String productId) throws SQLException {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID sản phẩm không hợp lệ.");
        }
        return productsDao.getProductById(productId);
    }

    // Kiểm tra dữ liệu sản phẩm
    private void validateProduct(Products product) throws IllegalArgumentException {
        if (product == null) {
            throw new IllegalArgumentException("Hãy điền đầy đủ thông tin sản phẩm.");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }

        double price = product.getPrice();
        if (Double.isNaN(price) || Double.isInfinite(price)) {
            throw new IllegalArgumentException("Giá sản phẩm phải là một số hợp lệ.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn hoặc bằng 0.");
        }

        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng sản phẩm phải lớn hơn 0.");
        }

        if (product.getQuantitySold() < 0) {
            throw new IllegalArgumentException("Số lượng đã bán phải lớn hơn hoặc bằng 0.");
        }
    }
}