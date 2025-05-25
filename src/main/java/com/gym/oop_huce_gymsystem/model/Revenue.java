package com.gym.oop_huce_gymsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Revenue {
    private int revenueId;
    private String sourceType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String cardId;
    private Integer productId;
    private Integer quantitySold; // Thêm thuộc tính cho quantity_sold
    private BigDecimal unitPrice; // Thêm thuộc tính cho unit_price
    private String description;
    private String paymentMethod;
    // Constructor mặc định
    public Revenue() {
    }

    // Constructor đầy đủ
    public Revenue(int revenueId, String sourceType, BigDecimal amount, LocalDateTime transactionDate,
                   String cardId, Integer productId, Integer quantitySold, BigDecimal unitPrice,
                   String paymentMethod, String description) {
        this.revenueId = revenueId;
        this.sourceType = sourceType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.cardId = cardId;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.paymentMethod = paymentMethod;
        this.description = description;
    }

    // Getters và Setters
    public int getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(int revenueId) {
        this.revenueId = revenueId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}