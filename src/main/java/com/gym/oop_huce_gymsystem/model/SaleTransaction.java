package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDateTime;

public class SaleTransaction {
    private int transactionId;
    private String productId;
    private int quantitySold;
    private LocalDateTime transactionDate;

    public SaleTransaction() {
    }

    public SaleTransaction(String productId, int quantitySold, LocalDateTime transactionDate) {
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.transactionDate = transactionDate;
    }

    public SaleTransaction(int transactionId, String productId, int quantitySold, LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.transactionDate = transactionDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "SaleTransaction{" +
                "transactionId=" + transactionId +
                ", productId='" + productId + '\'' +
                ", quantitySold=" + quantitySold +
                ", transactionDate=" + transactionDate +
                '}';
    }
}