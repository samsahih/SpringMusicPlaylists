package com.musicplaylists.demo.musicplaylists.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean active;

    @OneToOne
    private NormalUser user;

    public Subscription() {}

    public Subscription(LocalDate startDate, LocalDate endDate, boolean active) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }

    public NormalUser getUser() {
        return user;
    }

    public void setUser(NormalUser user) {
        if (this.user == user) {
            return;
        }

        this.user = user;
        if (user != null && user.getSubscription() != this) {
            user.setSubscription(this);
        }
    }
}