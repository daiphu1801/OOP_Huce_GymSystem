package com.gym.oop_huce_gymsystem.model;

public class Trainers {
    private String trainerId;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private String specialization;

    public Trainers() {}

    public Trainers(String trainerId, String name, String gender, String phone, String email, String specialization) {
        this.trainerId = trainerId;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
    }

    public Trainers(String name, String gender, String phone, String email, String specialization) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
