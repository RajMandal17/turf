package com.turf;

import com.turf.model.User;
import com.turf.service.AuthService;
import com.turf.service.BookingService;
import com.turf.service.TurfService;
import com.turf.util.DatabaseUtil;

import java.util.Scanner;

/**
 * Main application class for Turf Management System
 */
public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthService authService = new AuthService();
    private static TurfService turfService = new TurfService();
    private static BookingService bookingService = new BookingService();
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   TURF MANAGEMENT SYSTEM");
        System.out.println("====================================");
        
        // Initialize database
        DatabaseUtil.initializeDatabase();
        
        // Main application loop
        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else if (authService.isAdmin(currentUser)) {
                showAdminMenu();
            } else {
                showCustomerMenu();
            }
        }
    }

    /**
     * Display main menu for unauthenticated users
     */
    private static void showMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegistration();
                break;
            case 3:
                handleExit();
                break;
            default:
                System.out.println("Invalid option! Please try again.");
        }
    }

    /**
     * Display admin menu
     */
    private static void showAdminMenu() {
        System.out.println("\n========== ADMIN MENU ==========");
        System.out.println("Hello, " + currentUser.getName() + " (Admin)");
        System.out.println("1. View All Turfs");
        System.out.println("2. Add New Turf");
        System.out.println("3. Delete Turf");
        System.out.println("4. View All Bookings");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                turfService.displayAllTurfs();
                break;
            case 2:
                handleAddTurf();
                break;
            case 3:
                handleDeleteTurf();
                break;
            case 4:
                bookingService.displayAllBookings();
                break;
            case 5:
                handleLogout();
                break;
            default:
                System.out.println("Invalid option! Please try again.");
        }
    }

    /**
     * Display customer menu
     */
    private static void showCustomerMenu() {
        System.out.println("\n========== CUSTOMER MENU ==========");
        System.out.println("Hello, " + currentUser.getName() + " (Customer)");
        System.out.println("1. View All Turfs");
        System.out.println("2. Book a Turf");
        System.out.println("3. View My Bookings");
        System.out.println("4. Cancel Booking");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                turfService.displayAllTurfs();
                break;
            case 2:
                handleBookTurf();
                break;
            case 3:
                bookingService.displayUserBookings(currentUser.getId());
                break;
            case 4:
                handleCancelBooking();
                break;
            case 5:
                handleLogout();
                break;
            default:
                System.out.println("Invalid option! Please try again.");
        }
    }

    /**
     * Handle user login
     */
    private static void handleLogin() {
        System.out.println("\n========== LOGIN ==========");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = authService.loginUser(email, password);
    }

    /**
     * Handle user registration
     */
    private static void handleRegistration() {
        System.out.println("\n========== REGISTRATION ==========");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        System.out.println("Role:");
        System.out.println("1. Customer");
        System.out.println("2. Admin");
        System.out.print("Choose role (1 or 2): ");
        
        int roleChoice = getIntInput();
        String role = (roleChoice == 2) ? "ADMIN" : "CUSTOMER";

        authService.registerUser(name, email, password, role);
    }

    /**
     * Handle adding a new turf (Admin only)
     */
    private static void handleAddTurf() {
        System.out.println("\n========== ADD NEW TURF ==========");
        System.out.print("Turf Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Location: ");
        String location = scanner.nextLine().trim();
        
        System.out.print("Hourly Rate ($): ");
        double hourlyRate = getDoubleInput();

        turfService.addTurf(name, location, hourlyRate);
    }

    /**
     * Handle deleting a turf (Admin only)
     */
    private static void handleDeleteTurf() {
        System.out.println("\n========== DELETE TURF ==========");
        turfService.displayAllTurfs();
        
        System.out.print("Enter Turf ID to delete: ");
        int turfId = getIntInput();

        turfService.deleteTurf(turfId);
    }

    /**
     * Handle booking a turf (Customer only)
     */
    private static void handleBookTurf() {
        System.out.println("\n========== BOOK A TURF ==========");
        turfService.displayAllTurfs();
        
        System.out.print("Enter Turf ID to book: ");
        int turfId = getIntInput();
        
        System.out.print("Enter booking date and time (yyyy-MM-dd HH:mm): ");
        String dateTime = scanner.nextLine().trim();
        
        System.out.print("Enter duration (hours): ");
        int duration = getIntInput();

        bookingService.createBooking(currentUser.getId(), turfId, dateTime, duration);
    }

    /**
     * Handle canceling a booking (Customer only)
     */
    private static void handleCancelBooking() {
        System.out.println("\n========== CANCEL BOOKING ==========");
        bookingService.displayUserBookings(currentUser.getId());
        
        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = getIntInput();

        bookingService.cancelBooking(bookingId, currentUser.getId());
    }

    /**
     * Handle user logout
     */
    private static void handleLogout() {
        System.out.println("Logged out successfully!");
        currentUser = null;
    }

    /**
     * Handle application exit
     */
    private static void handleExit() {
        System.out.println("Thank you for using Turf Management System!");
        DatabaseUtil.closeConnection();
        scanner.close();
        System.exit(0);
    }

    /**
     * Get integer input from user with validation
     * @return Valid integer input
     */
    private static int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    /**
     * Get double input from user with validation
     * @return Valid double input
     */
    private static double getDoubleInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.print("Please enter a positive number: ");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
