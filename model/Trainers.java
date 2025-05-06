package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDate;

public class Trainers {
    private int trainerId;
    private String name;
    private String phone;
    private String email;
    private String specialization;

    public Trainers(int trainerId, String name, String phone, String email, String specialization, LocalDate create_at) {
        this.trainerId = trainerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
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


    @Override
    public String toString() {
        return "Trainers[trainerId=" + trainerId +
                ", name=" + name +
                ", phone=" + phone +
                ", email=" + email +
                ", specialization=" + specialization +
                "]";

    }
}
