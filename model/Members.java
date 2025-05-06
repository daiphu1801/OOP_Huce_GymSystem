package com.gym.oop_huce_gymsystem.model;

import java.time.*;

public class Members {
    private int memberId; // ID của từng hội viên
    private String name; // Tên hội viên
    private String phone; // Số điện thoại hội viên
    private String membershipType; // Loại thẻ hội viên đăng ký
    private LocalDate registrationDate; // Ngày đăng ký
    private String trainingPackage; // Gói tập luyện

    public Members() {
    }

    // Constructor đầy đủ
    public Members(int memberId, String name, String phone, String membershipType, String trainingPackage, LocalDate registrationDate, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.membershipType = membershipType;
        this.trainingPackage = trainingPackage;
        this.registrationDate = registrationDate;
    }

    // Constructor dùng khi tạo mới
    public Members(String name, String phone, String membershipType, String trainingPackage, LocalDate registrationDate) {
        this.name = name;
        this.phone = phone;
        this.membershipType = membershipType;
        this.trainingPackage = trainingPackage;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }


    public String getTrainingPackage() {
        return trainingPackage;
    }

    public void setTrainingPackage(String trainingPackage) {
        this.trainingPackage = trainingPackage;
    }

    @Override
    public String toString() {
        return "Members{" + "memberId=" + memberId + ", name='" + name + '\'' + ", phone='" + phone + '\'' + ", membershipType='" + membershipType + '\'' + ", registrationDate=" + registrationDate +  ", trainingPackage='" + trainingPackage + '\'' + '}';
    }
}
