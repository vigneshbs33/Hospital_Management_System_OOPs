import gui.HospitalManagementApp;
import javax.swing.*;

/**
 * Hospital Management System - Main Entry Point
 * 
 * A complete Hospital Management System demonstrating Object-Oriented
 * Programming concepts:
 * - Inheritance: Patient, Doctor, Staff extend abstract Person class
 * - Encapsulation: Private fields with getters/setters in all classes
 * - Polymorphism: Abstract methods implemented differently in subclasses
 * - Abstraction: Abstract Person class with common attributes
 * - Composition: Appointment contains Patient and Doctor references
 * - Singleton: HospitalManager manages the entire system
 * 
 * Features:
 * - Patient Management (Registration, Search, Medical History)
 * - Doctor Management (Profiles, Specializations, Schedules)
 * - Appointment Scheduling (Booking, Rescheduling, Cancellation)
 * - Room Management (Allocation, Status Tracking)
 * - Billing (Invoice Generation, Payment Processing)
 * - Dashboard with Real-time Statistics
 * 
 * @author OOP Project
 * @version 1.0
 */
public class Main {

    /**
     * Application entry point
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system properties for better rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Run on Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set UI look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Customize default UI properties for better appearance
                UIManager.put("Button.arc", 8);
                UIManager.put("Component.arc", 8);
                UIManager.put("TextComponent.arc", 8);
                UIManager.put("TextField.margin", new java.awt.Insets(4, 8, 4, 8));

            } catch (Exception e) {
                // Fall back to default look and feel
                System.err.println("Could not set system look and feel: " + e.getMessage());
            }

            // Create and show main application
            HospitalManagementApp app = new HospitalManagementApp();
            app.setVisible(true);

            // Print startup message
            System.out.println("==============================================");
            System.out.println("  MedCare Hospital Management System v1.0");
            System.out.println("  OOP Project - Java Swing Application");
            System.out.println("==============================================");
            System.out.println("Application started successfully!");
            System.out.println("Login with any credentials to access the system.");
            System.out.println("Demo data has been pre-loaded.");
        });
    }
}
