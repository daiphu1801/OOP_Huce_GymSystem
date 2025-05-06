package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDate;


public class MemberShipCards {
    private int card_id;
    private String name_card;
    private Double renewal_count;
    private LocalDate issue_date;
    private LocalDate expiry_date;
    private LocalDate last_checkin_time;

    public MemberShipCards(int card_id, int member_id, String name_card, Double renewal_count, LocalDate issue_date, LocalDate expiry_date, LocalDate last_checkin_time) {
        this.card_id = card_id;
        this.name_card = name_card;
        this.renewal_count = renewal_count;
        this.issue_date = issue_date;
        this.expiry_date = expiry_date;
        this.last_checkin_time = last_checkin_time;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }


    public String getName_card() {
        return name_card;
    }

    public void setName_card(String name_card) {
        this.name_card = name_card;
    }

    public Double getRenewal_count() {
        return renewal_count;
    }

    public void setRenewal_count(Double renewal_count) {
        this.renewal_count = renewal_count;
    }

    public LocalDate getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(LocalDate issue_date) {
        this.issue_date = issue_date;
    }

    public LocalDate getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(LocalDate expiry_date) {
        this.expiry_date = expiry_date;
    }

    public LocalDate getlast_checkin_time() {
        return last_checkin_time;
    }

    public void setlast_checkin_time(LocalDate last_checkin_time) {
        this.last_checkin_time = last_checkin_time;
    }

    @Override
    public String toString() {
        return "MemberShipCards[" +
                "cardId=" + card_id +
                ", nameCard='" + name_card + '\'' +
                ", renewalCount=" + renewal_count +
                ", issueDate=" + issue_date +
                ", expiryDate=" + expiry_date +
                ", lastCheckinTime=" + last_checkin_time +
                "]";
    }
}
