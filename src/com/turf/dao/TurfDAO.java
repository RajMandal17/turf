package com.turf.dao;

import com.turf.model.Turf;
import com.turf.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Turf operations
 */
public class TurfDAO {

    /**
     * Add a new turf
     * @param turf Turf object to add
     * @return true if addition successful, false otherwise
     */
    public boolean addTurf(Turf turf) {
        String sql = "INSERT INTO turfs (name, location, hourly_rate) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, turf.getName());
            pstmt.setString(2, turf.getLocation());
            pstmt.setDouble(3, turf.getHourlyRate());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding turf: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all turfs
     * @return List of all turfs
     */
    public List<Turf> getAllTurfs() {
        List<Turf> turfs = new ArrayList<>();
        String sql = "SELECT * FROM turfs";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Turf turf = new Turf(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getDouble("hourly_rate")
                );
                turfs.add(turf);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all turfs: " + e.getMessage());
        }
        
        return turfs;
    }

    /**
     * Get turf by ID
     * @param turfId Turf ID
     * @return Turf object if found, null otherwise
     */
    public Turf getTurfById(int turfId) {
        String sql = "SELECT * FROM turfs WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, turfId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Turf(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getDouble("hourly_rate")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting turf by ID: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Delete turf by ID
     * @param turfId Turf ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteTurf(int turfId) {
        String sql = "DELETE FROM turfs WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, turfId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting turf: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if turf exists by ID
     * @param turfId Turf ID to check
     * @return true if turf exists, false otherwise
     */
    public boolean turfExists(int turfId) {
        String sql = "SELECT COUNT(*) FROM turfs WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, turfId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking turf existence: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Update turf information
     * @param turf Turf object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateTurf(Turf turf) {
        String sql = "UPDATE turfs SET name = ?, location = ?, hourly_rate = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, turf.getName());
            pstmt.setString(2, turf.getLocation());
            pstmt.setDouble(3, turf.getHourlyRate());
            pstmt.setInt(4, turf.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating turf: " + e.getMessage());
            return false;
        }
    }
}
