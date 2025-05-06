package com.gym.oop_huce_gymsystem.model;

import java.time.*;

public class Members {
    private int memberId;
    private String cardCode;
    private String fullName;
    private String phone;
    private String gender;
    private String email;

    // Default constructor
    public Members() {
    }

    // Parameterized constructor
    public Members(int memberId, String cardCode, String fullName, String phone, String gender, String email) {
        this.memberId = memberId;
        this.cardCode = cardCode;
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
    }

    // Parameterized constructor
    public Members(String cardCode, String fullName, String phone, String gender, String email) {
        this.cardCode = cardCode;
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.email = email;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}