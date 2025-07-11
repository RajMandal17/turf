package com.turf.model;

/**
 * Turf model class representing turfs in the management system
 */
public class Turf {
    private int id;
    private String name;
    private String location;
    private double hourlyRate;

    // Default constructor
    public Turf() {}

    // Constructor without id (for new turfs)
    public Turf(String name, String location, double hourlyRate) {
        this.name = name;
        this.location = location;
        this.hourlyRate = hourlyRate;
    }

    // Constructor with id (for existing turfs)
    public Turf(int id, String name, String location, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.hourlyRate = hourlyRate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "Turf{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", hourlyRate=" + hourlyRate +
                '}';
    }
}
