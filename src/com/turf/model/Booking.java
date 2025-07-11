package com.turf.model;

import java.sql.Timestamp;

/**
 * Booking model class representing bookings in the turf management system
 */
public class Booking {
    private int id;
    private int userId;
    private int turfId;
    private Timestamp bookingTime;
    private int duration; // in hours
    private double totalCost;

    // Additional fields for display purposes
    private String userName;
    private String turfName;
    private String turfLocation;

    // Default constructor
    public Booking() {}

    // Constructor without id (for new bookings)
    public Booking(int userId, int turfId, Timestamp bookingTime, int duration, double totalCost) {
        this.userId = userId;
        this.turfId = turfId;
        this.bookingTime = bookingTime;
        this.duration = duration;
        this.totalCost = totalCost;
    }

    // Constructor with id (for existing bookings)
    public Booking(int id, int userId, int turfId, Timestamp bookingTime, int duration, double totalCost) {
        this.id = id;
        this.userId = userId;
        this.turfId = turfId;
        this.bookingTime = bookingTime;
        this.duration = duration;
        this.totalCost = totalCost;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTurfId() {
        return turfId;
    }

    public void setTurfId(int turfId) {
        this.turfId = turfId;
    }

    public Timestamp getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Timestamp bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTurfName() {
        return turfName;
    }

    public void setTurfName(String turfName) {
        this.turfName = turfName;
    }

    public String getTurfLocation() {
        return turfLocation;
    }

    public void setTurfLocation(String turfLocation) {
        this.turfLocation = turfLocation;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId=" + userId +
                ", turfId=" + turfId +
                ", bookingTime=" + bookingTime +
                ", duration=" + duration +
                ", totalCost=" + totalCost +
                ", userName='" + userName + '\'' +
                ", turfName='" + turfName + '\'' +
                ", turfLocation='" + turfLocation + '\'' +
                '}';
    }
}
