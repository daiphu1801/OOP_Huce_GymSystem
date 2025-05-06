package com.gym.oop_huce_gymsystem.model;

import java.time.*;

public class Equipments {
    private int equipmentId;
    private String Name;
    private String Status;
    private int Quantity;
    private LocalDate Purchase_Date;

    public Equipments() {}

    public Equipments(int equipmentId, String Name, int Quantity,String Status, LocalDate Purchase_Date) {
        this.equipmentId = equipmentId;
        this.Name = Name;
        this.Status = Status;
        this.Quantity = Quantity;
        this.Purchase_Date = Purchase_Date;
    }

    public Equipments(String Name, int Quantity, String Status, LocalDate Purchase_Date) {
        this.Name = Name;
        this.Status = Status;
        this.Quantity = Quantity;
        this.Purchase_Date = Purchase_Date;
    }

    //getter setter
    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public LocalDate getPurchase_Date() {
        return Purchase_Date;
    }

    public void setPurchase_Date(LocalDate Purchase_Date) {
        this.Purchase_Date = Purchase_Date;
    }


    @Override
    public String toString() {
        return "Equipments{" +
                "equipmentId=" + equipmentId +
                ", name='" + Name + '\'' +
                ", status='" + Status + '\'' +
                ", quantity='" + Quantity + '\'' +
                ", purchaseDate='" + Purchase_Date + '\'' +
                '}';

    }
}




