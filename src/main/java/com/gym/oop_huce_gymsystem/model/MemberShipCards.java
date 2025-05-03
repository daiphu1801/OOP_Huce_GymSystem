package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDate;


public class MemberShipCards {
    private String cardId;
    private String trainingPackage;
    private String cardType;
    private Double price;
    private LocalDate registrationDate;
    private LocalDate expiryDate;

    // Default constructor
    public MemberShipCards() {
    }

    // Parameterized constructor
    public MemberShipCards(String cardId, double price, String trainingPackage, String cardType, LocalDate registrationDate, LocalDate expiryDate) {
        this.cardId = cardId;
        this.trainingPackage = trainingPackage;
        this.cardType = cardType;
        this.price = price;
        this.registrationDate = registrationDate;
        this.expiryDate = expiryDate;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getTrainingPackage() {
        return trainingPackage;
    }

    public void setTrainingPackage(String trainingPackage) {
        this.trainingPackage = trainingPackage;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}