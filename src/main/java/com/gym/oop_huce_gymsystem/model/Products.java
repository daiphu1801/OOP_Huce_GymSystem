package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDate;

public class Products {
    private int productId;
    private String name;
    private Double price;
    private String quantity;
    private LocalDate create_at;

    public Products(int productId, String name, Double price, String quantity, LocalDate create_at) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.create_at = create_at;

    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public LocalDate getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDate create_at) {
        this.create_at = create_at;
    }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", create_at=" + create_at + "]";
    }

}
