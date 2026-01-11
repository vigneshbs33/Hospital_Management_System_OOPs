package managers;

import models.*;
import utils.FileHandler;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Hospital Manager - Singleton class that manages the entire hospital system.
 * Demonstrates the Singleton design pattern.
 * 
 * This class coordinates all sub-managers and provides centralized access
 * to hospital-wide statistics and operations.
 */
public class HospitalManager {

    // Singleton instance
    private static HospitalManager instance;

    // Sub-managers
    private PatientManager patientManager;
    private DoctorManager doctorManager;
    private AppointmentManager appointmentManager;
    private BillingManager billingManager;
    private RoomManager roomManager;

    // Hospital information
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPhone;
    private String hospitalEmail;

    /**
     * Private constructor for Singleton pattern
     */
    private HospitalManager() {
        this.hospitalName = "MedCare Hospital";
        this.hospitalAddress = "123 Healthcare Avenue, Medical District";
        this.hospitalPhone = "+91 1800-MEDCARE";
        this.hospitalEmail = "contact@medcare.hospital";

        // Initialize all managers
        initializeManagers();
    }

    /**
     * Get the singleton instance
     * 
     * @return HospitalManager instance
     */
    public static synchronized HospitalManager getInstance() {
        if (instance == null) {
            instance = new HospitalManager();
        }
        return instance;
    }

    /**
     * Initialize all sub-managers
     */
    private void initializeManagers() {
        FileHandler.initializeDataDirectory();
        this.patientManager = new PatientManager();
        this.doctorManager = new DoctorManager();
        this.appointmentManager = new AppointmentManager();
        this.billingManager = new BillingManager();
        this.roomManager = new RoomManager();

        // Initialize sample data if empty
        if (doctorManager.getTotalCount() == 0) {
            initializeSampleData();
        }
    }

    /**
     * Initialize sample data for demonstration
     */
    private void initializeSampleData() {
        // Add sample doctors
        String[] specializations = { "Cardiology", "Neurology", "Orthopedics", "Pediatrics",
                "Dermatology", "Ophthalmology", "ENT", "General Medicine" };
        String[] doctorNames = { "Arun Kumar", "Priya Sharma", "Rajesh Verma",
                "Sneha Patel", "Amit Singh", "Kavitha Reddy",
                "Suresh Nair", "Anita Das" };

        UserManager userManager = new UserManager();

        for (int i = 0; i < doctorNames.length; i++) {
            Doctor doctor = new Doctor();
            doctor.setName(doctorNames[i]);
            doctor.setSpecialization(specializations[i]);
            doctor.setDepartment(specializations[i]);
            doctor.setQualification("MBBS, MD");
            doctor.setConsultationFee(500 + (i * 100));
            doctor.setPhone("98765" + String.format("%05d", 10000 + i));
            doctor.setEmail(doctorNames[i].toLowerCase().replace(" ", ".") + "@medcare.hospital");
            doctor.setGender(i % 2 == 0 ? "Male" : "Female");
            doctor.setExperienceYears(5 + i);
            doctor.setWorkingHours("9:00 AM - 5:00 PM");
            doctor.setAvailableDays(java.util.Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
            doctorManager.addDoctor(doctor);

            // Create user account for the doctor
            String username = "dr." + doctorNames[i].toLowerCase().replace(" ", ".");
            String password = "doc" + doctor.getDoctorId().replace("DOC-", "");
            userManager.createDoctorUser(username, password, "Dr. " + doctorNames[i], doctor.getDoctorId());
        }

        // Add sample patients
        String[] patientNames = { "Rahul Mehta", "Sunita Gupta", "Vikram Joshi", "Meera Iyer",
                "Arjun Malhotra" };
        String[] bloodGroups = { "A+", "B+", "O+", "AB+", "O-" };

        for (int i = 0; i < patientNames.length; i++) {
            Patient patient = new Patient();
            patient.setName(patientNames[i]);
            patient.setGender(i % 2 == 0 ? "Male" : "Female");
            patient.setBloodGroup(bloodGroups[i]);
            patient.setPhone("97654" + String.format("%05d", 20000 + i));
            patient.setEmail(patientNames[i].toLowerCase().replace(" ", ".") + "@email.com");
            patient.setAddress("Address " + (i + 1) + ", City");
            patient.setDateOfBirth(LocalDate.of(1990 - i * 5, (i % 12) + 1, (i % 28) + 1));
            patientManager.addPatient(patient);
        }
    }

    // ==================== GETTERS FOR MANAGERS ====================

    public PatientManager getPatientManager() {
        return patientManager;
    }

    public DoctorManager getDoctorManager() {
        return doctorManager;
    }

    public AppointmentManager getAppointmentManager() {
        return appointmentManager;
    }

    public BillingManager getBillingManager() {
        return billingManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    // ==================== HOSPITAL INFO ====================

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getHospitalPhone() {
        return hospitalPhone;
    }

    public void setHospitalPhone(String hospitalPhone) {
        this.hospitalPhone = hospitalPhone;
    }

    public String getHospitalEmail() {
        return hospitalEmail;
    }

    public void setHospitalEmail(String hospitalEmail) {
        this.hospitalEmail = hospitalEmail;
    }

    // ==================== DASHBOARD STATISTICS ====================

    /**
     * Get total patient count
     */
    public int getTotalPatients() {
        return patientManager.getTotalCount();
    }

    /**
     * Get total doctor count
     */
    public int getTotalDoctors() {
        return doctorManager.getTotalCount();
    }

    /**
     * Get today's appointment count
     */
    public int getTodaysAppointments() {
        return appointmentManager.getTodaysCount();
    }

    /**
     * Get available room count
     */
    public int getAvailableRooms() {
        return roomManager.getAvailableCount();
    }

    /**
     * Get admitted patient count
     */
    public int getAdmittedPatients() {
        return patientManager.getAdmittedCount();
    }

    /**
     * Get today's revenue
     */
    public double getTodaysRevenue() {
        return billingManager.getTodaysRevenue();
    }

    /**
     * Get pending bills amount
     */
    public double getPendingBillsAmount() {
        return billingManager.getPendingAmount();
    }

    /**
     * Get room occupancy rate
     */
    public double getRoomOccupancyRate() {
        return roomManager.getOccupancyRate();
    }

    /**
     * Get all specializations
     */
    public List<String> getSpecializations() {
        return doctorManager.getAllSpecializations();
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Reload all data from files
     */
    public void reloadAllData() {
        patientManager.reloadData();
        doctorManager.reloadData();
        appointmentManager.reloadData();
        billingManager.reloadData();
        roomManager.reloadData();
    }

    /**
     * Reset the singleton instance (for testing)
     */
    public static void resetInstance() {
        instance = null;
    }
}
