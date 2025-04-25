package com.gym.oop_huce_gymsystem;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String packageType;
    private String cardType;
    private LocalDate registerDate;
    private String cardStatus;

    // Định dạng ngày tháng cho việc hiển thị
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor mặc định
    public Member() {
    }

    // Constructor với tên đầy đủ, số điện thoại, loại gói tập, loại thẻ, và ngày đăng ký
    public Member(String fullName, String phoneNumber, String packageType, String cardType, LocalDate registerDate) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.packageType = packageType;
        this.cardType = cardType;
        this.registerDate = registerDate;
        this.cardStatus = "Còn hạn"; // Giá trị mặc định, sẽ được cập nhật bởi DAO
    }

    // Constructor đầy đủ bao gồm thêm ngày sinh và giới tính
    public Member(String fullName, String phoneNumber, LocalDate dateOfBirth, String gender,
                  String packageType, String cardType, LocalDate registerDate) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.packageType = packageType;
        this.cardType = cardType;
        this.registerDate = registerDate;
        this.cardStatus = "Còn hạn"; // Giá trị mặc định, sẽ được cập nhật bởi DAO
    }

    // Các phương thức getter và setter
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

    // Getter và setter cho ngày sinh
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getter và setter cho giới tính
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    // Phương thức hỗ trợ để lấy ngày đăng ký đã được định dạng
    public String getFormattedRegisterDate() {
        return registerDate != null ? registerDate.format(DATE_FORMATTER) : "";
    }

    // Phương thức hỗ trợ để lấy ngày sinh đã được định dạng
    public String getFormattedDateOfBirth() {
        return dateOfBirth != null ? dateOfBirth.format(DATE_FORMATTER) : "";
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + getFormattedDateOfBirth() + '\'' +
                ", gender='" + gender + '\'' +
                ", packageType='" + packageType + '\'' +
                ", cardType='" + cardType + '\'' +
                ", registerDate=" + getFormattedRegisterDate() +
                ", cardStatus='" + cardStatus + '\'' +
                '}';
    }
}