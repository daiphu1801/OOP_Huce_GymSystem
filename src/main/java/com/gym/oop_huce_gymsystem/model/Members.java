package com.gym.oop_huce_gymsystem.model;

import java.time.*;
import java.util.*;
public class Members {
    private int memberId; //ID của từng hội viên
    private String name; //Tên hội viên
    private String phone; //So điện thoại hội viên
    private String membershipType; //Loại thẻ hội viên đăng ky
    private LocalDate registrationDate; // Ngay đăng ký
    private LocalDateTime createdAt;

    //Constructor
    public Members(int memberId, String name, String phone, String membershipType,
                  LocalDate registrationDate, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.membershipType = membershipType;
        this.registrationDate = registrationDate;
        this.createdAt = createdAt;
    }

    //Constuctor để create
    public Members(String name, String phone, String membershipType,
                  LocalDate registrationDate) {
        this.name = name;
        this.phone = phone;
        this.membershipType = membershipType;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) {
        if (!Arrays.asList("BASIC", "PREMIUM", "VIP").contains(membershipType)) {
            throw new IllegalArgumentException("Membership type must be BASIC, PREMIUM, or VIP");
        }
        this.membershipType = membershipType;
    }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Members{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", membershipType='" + membershipType + '\'' +
                ", registrationDate=" + registrationDate +
                ", createdAt=" + createdAt +
                '}';
    }
}
