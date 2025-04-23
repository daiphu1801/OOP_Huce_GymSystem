package com.gym.oop_huce_gymsystem;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String fullName;
    private String phoneNumber;
    private String packageType;
    private String cardType;
    private LocalDate registerDate;
    private String cardStatus;

    // Date formatter for display
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Default constructor
    public Member() {
    }

    // Constructor with full name, phone number, package type, card type, and register date
    public Member(String fullName, String phoneNumber, String packageType, String cardType, LocalDate registerDate) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.packageType = packageType;
        this.cardType = cardType;
        this.registerDate = registerDate;
        this.cardStatus = "Còn hạn"; // Default value, will be updated by DAO
    }

    // Getters and setters
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    // Helper method to get formatted register date
    public String getFormattedRegisterDate() {
        return registerDate != null ? registerDate.format(DATE_FORMATTER) : "";
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", packageType='" + packageType + '\'' +
                ", cardType='" + cardType + '\'' +
                ", registerDate=" + getFormattedRegisterDate() +
                ", cardStatus='" + cardStatus + '\'' +
                '}';
    }
}