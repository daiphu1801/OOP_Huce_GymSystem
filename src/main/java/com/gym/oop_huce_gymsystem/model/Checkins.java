package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDateTime;

public class Checkins {
    private int checkinId;
    private String memberId;
    private LocalDateTime checkinTime;

    public Checkins() {
    }

    public Checkins(int checkinId, String memberId, LocalDateTime checkinTime) {
        this.checkinId = checkinId;
        this.memberId = memberId;
        this.checkinTime = checkinTime;
    }

    public int getCheckinId() {
        return checkinId;
    }

    public void setCheckinId(int checkinId) {
        this.checkinId = checkinId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(LocalDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }
}
