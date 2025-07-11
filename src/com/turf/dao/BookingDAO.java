package com.turf.dao;

import com.turf.model.Booking;
import com.turf.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Booking operations
 */
public class BookingDAO {

    /**
     * Add a new booking
     * @param booking Booking object to add
     * @return true if addition successful, false otherwise
     */
    public boolean addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, turf_id, booking_time, duration, total_cost) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, booking.getUserId());
            pstmt.setInt(2, booking.getTurfId());
            pstmt.setTimestamp(3, booking.getBookingTime());
            pstmt.setInt(4, booking.getDuration());
            pstmt.setDouble(5, booking.getTotalCost());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all bookings
     * @return List of all bookings with user and turf details
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, u.name as user_name, t.name as turf_name, t.location as turf_location
            FROM bookings b
            JOIN users u ON b.user_id = u.id
            JOIN turfs t ON b.turf_id = t.id
            ORDER BY b.booking_time DESC
            """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("turf_id"),
                    rs.getTimestamp("booking_time"),
                    rs.getInt("duration"),
                    rs.getDouble("total_cost")
                );
                booking.setUserName(rs.getString("user_name"));
                booking.setTurfName(rs.getString("turf_name"));
                booking.setTurfLocation(rs.getString("turf_location"));
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
        }
        
        return bookings;
    }

    /**
     * Get bookings by user ID
     * @param userId User ID
     * @return List of bookings for the user
     */
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, u.name as user_name, t.name as turf_name, t.location as turf_location
            FROM bookings b
            JOIN users u ON b.user_id = u.id
            JOIN turfs t ON b.turf_id = t.id
            WHERE b.user_id = ?
            ORDER BY b.booking_time DESC
            """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("turf_id"),
                    rs.getTimestamp("booking_time"),
                    rs.getInt("duration"),
                    rs.getDouble("total_cost")
                );
                booking.setUserName(rs.getString("user_name"));
                booking.setTurfName(rs.getString("turf_name"));
                booking.setTurfLocation(rs.getString("turf_location"));
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bookings by user ID: " + e.getMessage());
        }
        
        return bookings;
    }

    /**
     * Get booking by ID
     * @param bookingId Booking ID
     * @return Booking object if found, null otherwise
     */
    public Booking getBookingById(int bookingId) {
        String sql = """
            SELECT b.*, u.name as user_name, t.name as turf_name, t.location as turf_location
            FROM bookings b
            JOIN users u ON b.user_id = u.id
            JOIN turfs t ON b.turf_id = t.id
            WHERE b.id = ?
            """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("turf_id"),
                    rs.getTimestamp("booking_time"),
                    rs.getInt("duration"),
                    rs.getDouble("total_cost")
                );
                booking.setUserName(rs.getString("user_name"));
                booking.setTurfName(rs.getString("turf_name"));
                booking.setTurfLocation(rs.getString("turf_location"));
                return booking;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting booking by ID: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Delete booking by ID
     * @param bookingId Booking ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check for booking conflicts (same turf at same time)
     * @param turfId Turf ID
     * @param bookingTime Booking time
     * @param duration Duration in hours
     * @return true if conflict exists, false otherwise
     */
    public boolean hasBookingConflict(int turfId, Timestamp bookingTime, int duration) {
        String sql = """
            SELECT COUNT(*) FROM bookings 
            WHERE turf_id = ? 
            AND ((booking_time <= ? AND DATE_ADD(booking_time, INTERVAL duration HOUR) > ?) 
            OR (booking_time < DATE_ADD(?, INTERVAL ? HOUR) AND booking_time >= ?))
            """;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, turfId);
            pstmt.setTimestamp(2, bookingTime);
            pstmt.setTimestamp(3, bookingTime);
            pstmt.setTimestamp(4, bookingTime);
            pstmt.setInt(5, duration);
            pstmt.setTimestamp(6, bookingTime);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking booking conflict: " + e.getMessage());
        }
        
        return false;
    }
}
