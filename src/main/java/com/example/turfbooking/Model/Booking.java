package com.example.turfbooking.Model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ THIS FIELD IS REQUIRED
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ THIS FIELD IS REQUIRED
    @ManyToOne
    @JoinColumn(name = "turf_id")
    private Turf turf;
    private String status;   
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    // 🔥 THIS METHOD FIXES YOUR ERROR
    public void setUser(User user) {
        this.user = user;
    }

    public Turf getTurf() {
        return turf;
    }

    public void setTurf(Turf turf) {
        this.turf = turf;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public String getStatus() {          // ✅ ADD
        return status;
    }

    public void setStatus(String status) { // ✅ ADD
        this.status = status;
    }
}