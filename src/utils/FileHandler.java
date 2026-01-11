package utils;

import models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling file I/O operations.
 * Uses Java Serialization for data persistence.
 */
public class FileHandler {

    private static final String DATA_DIR = "data";
    private static final String PATIENTS_FILE = DATA_DIR + "/patients.dat";
    private static final String DOCTORS_FILE = DATA_DIR + "/doctors.dat";
    private static final String STAFF_FILE = DATA_DIR + "/staff.dat";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.dat";
    private static final String BILLS_FILE = DATA_DIR + "/bills.dat";
    private static final String ROOMS_FILE = DATA_DIR + "/rooms.dat";
    private static final String DEPARTMENTS_FILE = DATA_DIR + "/departments.dat";
    private static final String MEDICAL_RECORDS_FILE = DATA_DIR + "/medical_records.dat";
    private static final String USERS_FILE = DATA_DIR + "/users.dat";

    /**
     * Initialize data directory
     */
    public static void initializeDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    // ==================== PATIENTS ====================

    /**
     * Save patients list to file
     * 
     * @param patients List of patients
     */
    public static void savePatients(List<Patient> patients) {
        saveToFile(PATIENTS_FILE, patients);
    }

    /**
     * Load patients list from file
     * 
     * @return List of patients
     */
    @SuppressWarnings("unchecked")
    public static List<Patient> loadPatients() {
        Object data = loadFromFile(PATIENTS_FILE);
        return data != null ? (List<Patient>) data : new ArrayList<>();
    }

    // ==================== DOCTORS ====================

    /**
     * Save doctors list to file
     * 
     * @param doctors List of doctors
     */
    public static void saveDoctors(List<Doctor> doctors) {
        saveToFile(DOCTORS_FILE, doctors);
    }

    /**
     * Load doctors list from file
     * 
     * @return List of doctors
     */
    @SuppressWarnings("unchecked")
    public static List<Doctor> loadDoctors() {
        Object data = loadFromFile(DOCTORS_FILE);
        return data != null ? (List<Doctor>) data : new ArrayList<>();
    }

    // ==================== STAFF ====================

    /**
     * Save staff list to file
     * 
     * @param staffList List of staff
     */
    public static void saveStaff(List<Staff> staffList) {
        saveToFile(STAFF_FILE, staffList);
    }

    /**
     * Load staff list from file
     * 
     * @return List of staff
     */
    @SuppressWarnings("unchecked")
    public static List<Staff> loadStaff() {
        Object data = loadFromFile(STAFF_FILE);
        return data != null ? (List<Staff>) data : new ArrayList<>();
    }

    // ==================== APPOINTMENTS ====================

    /**
     * Save appointments list to file
     * 
     * @param appointments List of appointments
     */
    public static void saveAppointments(List<Appointment> appointments) {
        saveToFile(APPOINTMENTS_FILE, appointments);
    }

    /**
     * Load appointments list from file
     * 
     * @return List of appointments
     */
    @SuppressWarnings("unchecked")
    public static List<Appointment> loadAppointments() {
        Object data = loadFromFile(APPOINTMENTS_FILE);
        return data != null ? (List<Appointment>) data : new ArrayList<>();
    }

    // ==================== BILLS ====================

    /**
     * Save bills list to file
     * 
     * @param bills List of bills
     */
    public static void saveBills(List<Bill> bills) {
        saveToFile(BILLS_FILE, bills);
    }

    /**
     * Load bills list from file
     * 
     * @return List of bills
     */
    @SuppressWarnings("unchecked")
    public static List<Bill> loadBills() {
        Object data = loadFromFile(BILLS_FILE);
        return data != null ? (List<Bill>) data : new ArrayList<>();
    }

    // ==================== ROOMS ====================

    /**
     * Save rooms list to file
     * 
     * @param rooms List of rooms
     */
    public static void saveRooms(List<Room> rooms) {
        saveToFile(ROOMS_FILE, rooms);
    }

    /**
     * Load rooms list from file
     * 
     * @return List of rooms
     */
    @SuppressWarnings("unchecked")
    public static List<Room> loadRooms() {
        Object data = loadFromFile(ROOMS_FILE);
        return data != null ? (List<Room>) data : new ArrayList<>();
    }

    // ==================== DEPARTMENTS ====================

    /**
     * Save departments list to file
     * 
     * @param departments List of departments
     */
    public static void saveDepartments(List<Department> departments) {
        saveToFile(DEPARTMENTS_FILE, departments);
    }

    /**
     * Load departments list from file
     * 
     * @return List of departments
     */
    @SuppressWarnings("unchecked")
    public static List<Department> loadDepartments() {
        Object data = loadFromFile(DEPARTMENTS_FILE);
        return data != null ? (List<Department>) data : new ArrayList<>();
    }

    // ==================== MEDICAL RECORDS ====================

    /**
     * Save medical records list to file
     * 
     * @param records List of medical records
     */
    public static void saveMedicalRecords(List<MedicalRecord> records) {
        saveToFile(MEDICAL_RECORDS_FILE, records);
    }

    /**
     * Load medical records list from file
     * 
     * @return List of medical records
     */
    @SuppressWarnings("unchecked")
    public static List<MedicalRecord> loadMedicalRecords() {
        Object data = loadFromFile(MEDICAL_RECORDS_FILE);
        return data != null ? (List<MedicalRecord>) data : new ArrayList<>();
    }

    // ==================== USERS ====================

    /**
     * Save users list to file
     * 
     * @param users List of users
     */
    public static void saveUsers(List<User> users) {
        saveToFile(USERS_FILE, users);
    }

    /**
     * Load users list from file
     * 
     * @return List of users
     */
    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() {
        Object data = loadFromFile(USERS_FILE);
        return data != null ? (List<User>) data : new ArrayList<>();
    }

    // ==================== GENERIC FILE OPERATIONS ====================

    /**
     * Generic method to save object to file
     * 
     * @param filename File path
     * @param data     Data to save
     */
    private static void saveToFile(String filename, Object data) {
        initializeDataDirectory();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Generic method to load object from file
     * 
     * @param filename File path
     * @return Loaded data or null
     */
    private static Object loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from " + filename + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if data files exist
     * 
     * @return true if any data files exist
     */
    public static boolean dataExists() {
        return new File(PATIENTS_FILE).exists() ||
                new File(DOCTORS_FILE).exists() ||
                new File(APPOINTMENTS_FILE).exists();
    }

    /**
     * Delete all data files
     */
    public static void clearAllData() {
        new File(PATIENTS_FILE).delete();
        new File(DOCTORS_FILE).delete();
        new File(STAFF_FILE).delete();
        new File(APPOINTMENTS_FILE).delete();
        new File(BILLS_FILE).delete();
        new File(ROOMS_FILE).delete();
        new File(DEPARTMENTS_FILE).delete();
        new File(MEDICAL_RECORDS_FILE).delete();
    }
}
