package com.turf.service;

import com.turf.dao.TurfDAO;
import com.turf.model.Turf;

import java.util.List;

/**
 * Service class for turf management operations
 */
public class TurfService {
    private TurfDAO turfDAO;

    public TurfService() {
        this.turfDAO = new TurfDAO();
    }

    /**
     * Add a new turf
     * @param name Turf name
     * @param location Turf location
     * @param hourlyRate Hourly rate for the turf
     * @return true if addition successful, false otherwise
     */
    public boolean addTurf(String name, String location, double hourlyRate) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Turf name cannot be empty!");
            return false;
        }
        
        if (location == null || location.trim().isEmpty()) {
            System.out.println("Turf location cannot be empty!");
            return false;
        }
        
        if (hourlyRate <= 0) {
            System.out.println("Hourly rate must be greater than 0!");
            return false;
        }

        // Create turf object and add
        Turf turf = new Turf(name.trim(), location.trim(), hourlyRate);
        boolean success = turfDAO.addTurf(turf);
        
        if (success) {
            System.out.println("Turf added successfully!");
        } else {
            System.out.println("Failed to add turf! Please try again.");
        }
        
        return success;
    }

    /**
     * Get all turfs
     * @return List of all turfs
     */
    public List<Turf> getAllTurfs() {
        return turfDAO.getAllTurfs();
    }

    /**
     * Display all turfs in a formatted way
     */
    public void displayAllTurfs() {
        List<Turf> turfs = getAllTurfs();
        
        if (turfs.isEmpty()) {
            System.out.println("No turfs available!");
            return;
        }

        System.out.println("\n========== AVAILABLE TURFS ==========");
        System.out.printf("%-5s %-20s %-30s %-15s%n", "ID", "NAME", "LOCATION", "HOURLY RATE");
        System.out.println("---------------------------------------------------------------------");
        
        for (Turf turf : turfs) {
            System.out.printf("%-5d %-20s %-30s $%-14.2f%n", 
                turf.getId(), 
                turf.getName(), 
                turf.getLocation(), 
                turf.getHourlyRate());
        }
        System.out.println("=====================================\n");
    }

    /**
     * Get turf by ID
     * @param turfId Turf ID
     * @return Turf object if found, null otherwise
     */
    public Turf getTurfById(int turfId) {
        return turfDAO.getTurfById(turfId);
    }

    /**
     * Delete turf by ID
     * @param turfId Turf ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteTurf(int turfId) {
        // Check if turf exists
        if (!turfDAO.turfExists(turfId)) {
            System.out.println("Turf with ID " + turfId + " does not exist!");
            return false;
        }

        boolean success = turfDAO.deleteTurf(turfId);
        
        if (success) {
            System.out.println("Turf deleted successfully!");
        } else {
            System.out.println("Failed to delete turf! It might have existing bookings.");
        }
        
        return success;
    }

    /**
     * Update turf information
     * @param turfId Turf ID to update
     * @param name New turf name
     * @param location New turf location
     * @param hourlyRate New hourly rate
     * @return true if update successful, false otherwise
     */
    public boolean updateTurf(int turfId, String name, String location, double hourlyRate) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Turf name cannot be empty!");
            return false;
        }
        
        if (location == null || location.trim().isEmpty()) {
            System.out.println("Turf location cannot be empty!");
            return false;
        }
        
        if (hourlyRate <= 0) {
            System.out.println("Hourly rate must be greater than 0!");
            return false;
        }

        // Check if turf exists
        if (!turfDAO.turfExists(turfId)) {
            System.out.println("Turf with ID " + turfId + " does not exist!");
            return false;
        }

        // Create updated turf object
        Turf turf = new Turf(turfId, name.trim(), location.trim(), hourlyRate);
        boolean success = turfDAO.updateTurf(turf);
        
        if (success) {
            System.out.println("Turf updated successfully!");
        } else {
            System.out.println("Failed to update turf! Please try again.");
        }
        
        return success;
    }

    /**
     * Check if turf exists
     * @param turfId Turf ID to check
     * @return true if turf exists, false otherwise
     */
    public boolean turfExists(int turfId) {
        return turfDAO.turfExists(turfId);
    }
}
