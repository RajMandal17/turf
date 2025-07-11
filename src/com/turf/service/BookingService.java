package com.turf.service;

import com.turf.dao.BookingDAO;
import com.turf.model.Booking;
import com.turf.model.Turf;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Service class for booking management operations
 */
public class BookingService {
    private BookingDAO bookingDAO;
    private TurfService turfService;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.turfService = new TurfService();
    }

    /**
     * Create a new booking
     * @param userId User ID making the booking
     * @param turfId Turf ID to book
     * @param dateTimeString Date and time string in format "yyyy-MM-dd HH:mm"
     * @param duration Duration in hours
     * @return true if booking successful, false otherwise
     */
    public boolean createBooking(int userId, int turfId, String dateTimeString, int duration) {
        // Validate input
        if (duration <= 0) {
            System.out.println("Duration must be greater than 0 hours!");
            return false;
        }

        if (duration > 12) {
            System.out.println("Maximum booking duration is 12 hours!");
            return false;
        }

        // Check if turf exists
        Turf turf = turfService.getTurfById(turfId);
        if (turf == null) {
            System.out.println("Turf with ID " + turfId + " does not exist!");
            return false;
        }

        // Parse date and time
        Timestamp bookingTime;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(dateTimeString);
            bookingTime = new Timestamp(date.getTime());
            
            // Check if booking time is in the future
            if (bookingTime.before(new Timestamp(System.currentTimeMillis()))) {
                System.out.println("Booking time must be in the future!");
                return false;
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format! Please use yyyy-MM-dd HH:mm (e.g., 2025-07-15 14:30)");
            return false;
        }

        // Check for booking conflicts
        if (bookingDAO.hasBookingConflict(turfId, bookingTime, duration)) {
            System.out.println("Turf is already booked for the selected time slot!");
            return false;
        }

        // Calculate total cost
        double totalCost = turf.getHourlyRate() * duration;

        // Create booking object
        Booking booking = new Booking(userId, turfId, bookingTime, duration, totalCost);
        boolean success = bookingDAO.addBooking(booking);
        
        if (success) {
            System.out.printf("Booking created successfully!%n");
            System.out.printf("Turf: %s%n", turf.getName());
            System.out.printf("Location: %s%n", turf.getLocation());
            System.out.printf("Date & Time: %s%n", bookingTime);
            System.out.printf("Duration: %d hour(s)%n", duration);
            System.out.printf("Total Cost: $%.2f%n", totalCost);
        } else {
            System.out.println("Failed to create booking! Please try again.");
        }
        
        return success;
    }

    /**
     * Get all bookings
     * @return List of all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    /**
     * Get bookings by user ID
     * @param userId User ID
     * @return List of bookings for the user
     */
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    /**
     * Display all bookings in a formatted way
     */
    public void displayAllBookings() {
        List<Booking> bookings = getAllBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found!");
            return;
        }

        System.out.println("\n========== ALL BOOKINGS ==========");
        System.out.printf("%-5s %-15s %-20s %-30s %-20s %-10s %-12s%n", 
            "ID", "USER", "TURF", "LOCATION", "DATE & TIME", "DURATION", "TOTAL COST");
        System.out.println("-------------------------------------------------------------------------------------------");
        
        for (Booking booking : bookings) {
            System.out.printf("%-5d %-15s %-20s %-30s %-20s %-10d $%-11.2f%n",
                booking.getId(),
                booking.getUserName(),
                booking.getTurfName(),
                booking.getTurfLocation(),
                booking.getBookingTime().toString().substring(0, 16),
                booking.getDuration(),
                booking.getTotalCost());
        }
        System.out.println("=================================\n");
    }

    /**
     * Display bookings for a specific user
     * @param userId User ID
     */
    public void displayUserBookings(int userId) {
        List<Booking> bookings = getBookingsByUserId(userId);
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found for this user!");
            return;
        }

        System.out.println("\n========== YOUR BOOKING HISTORY ==========");
        System.out.printf("%-5s %-20s %-30s %-20s %-10s %-12s%n", 
            "ID", "TURF", "LOCATION", "DATE & TIME", "DURATION", "TOTAL COST");
        System.out.println("-----------------------------------------------------------------------------------------");
        
        for (Booking booking : bookings) {
            System.out.printf("%-5d %-20s %-30s %-20s %-10d $%-11.2f%n",
                booking.getId(),
                booking.getTurfName(),
                booking.getTurfLocation(),
                booking.getBookingTime().toString().substring(0, 16),
                booking.getDuration(),
                booking.getTotalCost());
        }
        System.out.println("=========================================\n");
    }

    /**
     * Get booking by ID
     * @param bookingId Booking ID
     * @return Booking object if found, null otherwise
     */
    public Booking getBookingById(int bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }

    /**
     * Cancel booking by ID
     * @param bookingId Booking ID to cancel
     * @param userId User ID (for verification)
     * @return true if cancellation successful, false otherwise
     */
    public boolean cancelBooking(int bookingId, int userId) {
        // Get booking details
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (booking == null) {
            System.out.println("Booking with ID " + bookingId + " does not exist!");
            return false;
        }

        // Check if the booking belongs to the user
        if (booking.getUserId() != userId) {
            System.out.println("You can only cancel your own bookings!");
            return false;
        }

        // Check if booking is in the future (allow cancellation only for future bookings)
        if (booking.getBookingTime().before(new Timestamp(System.currentTimeMillis()))) {
            System.out.println("Cannot cancel past bookings!");
            return false;
        }

        boolean success = bookingDAO.deleteBooking(bookingId);
        
        if (success) {
            System.out.println("Booking cancelled successfully!");
        } else {
            System.out.println("Failed to cancel booking! Please try again.");
        }
        
        return success;
    }

    /**
     * Validate date format
     * @param dateTimeString Date time string to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidDateTime(String dateTimeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setLenient(false);
            sdf.parse(dateTimeString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
