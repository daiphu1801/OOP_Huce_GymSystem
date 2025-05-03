package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDate;

public class Products {
    private int productId;
    private String name;
    private String price;
    private int quantity;
    private int quantitySold;

    public Products() {
    }

    public Products(int productId, String name, String price, int quantity, int quantitySold) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.quantitySold = quantitySold;
    }

    public Products(String name, String price, int quantity, int quantitySold) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.quantitySold = quantitySold;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", quantitySold= " + quantitySold + "]";
    }

}
