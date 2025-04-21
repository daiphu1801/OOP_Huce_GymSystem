package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDate;

public class Revenue {
    private String revenueId;
    private String source_type;
    private double amount;
    private LocalDate transaction_date;
    private String description;
    private String create_at;

    public Revenue(String revenueId, String source_type, double amount, LocalDate transaction_date, String description, String create_at) {
        this.revenueId = revenueId;
        this.source_type = source_type;
        this.amount = amount;
        this.transaction_date = transaction_date;
        this.description = description;
        this.create_at = create_at;


    }
    public String getRevenueId() {return revenueId;}
    public void setRevenueId(String revenueId) {this.revenueId = revenueId;}

    public String getSource_type() {return source_type;}
    public void setSource_type(String source_type) {this.source_type = source_type;}

    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}

    public LocalDate getTransaction_date() {return transaction_date;}
    public void setTransaction_date(LocalDate transaction_date) {this.transaction_date = transaction_date;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getCreate_at() {return create_at;}
    public void setCreate_at(String create_at) {this.create_at = create_at;}

    @Override
    public String toString() {
        return "Revenue[" +
                "revenueId=" + revenueId +
                ", sourceType=" + source_type +
                ", amount=" + amount +
                ", transactionDate=" + transaction_date +
                ", description=" + description +
                ", createAt=" + create_at +
                "]";
    }
    }



}
