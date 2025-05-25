package com.gym.oop_huce_gymsystem.model;

import java.time.LocalDateTime;

public class Checkins {
    private int checkinId;
    private int memberId;
    private LocalDateTime checkinTime;

    public Checkins() {
    }

    public Checkins(int checkinId, int memberId, LocalDateTime checkinTime) {
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(LocalDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }
}
