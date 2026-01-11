package utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating unique IDs for different entities.
 * Uses a combination of prefix, date, and sequential number.
 */
public class IDGenerator {

    // Counters for different entity types
    private static AtomicInteger patientCounter = new AtomicInteger(1000);
    private static AtomicInteger doctorCounter = new AtomicInteger(100);
    private static AtomicInteger staffCounter = new AtomicInteger(500);
    private static AtomicInteger appointmentCounter = new AtomicInteger(1);
    private static AtomicInteger billCounter = new AtomicInteger(1);
    private static AtomicInteger recordCounter = new AtomicInteger(1);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Generate a unique Patient ID
     * Format: PAT-YYYYMMDD-XXXX
     * 
     * @return Generated patient ID
     */
    public static String generatePatientId() {
        String date = LocalDate.now().format(DATE_FORMAT);
        int num = patientCounter.incrementAndGet();
        return String.format("PAT-%s-%04d", date, num);
    }

    /**
     * Generate a unique Doctor ID
     * Format: DOC-XXX
     * 
     * @return Generated doctor ID
     */
    public static String generateDoctorId() {
        int num = doctorCounter.incrementAndGet();
        return String.format("DOC-%03d", num);
    }

    /**
     * Generate a unique Staff ID
     * Format: STF-XXX
     * 
     * @return Generated staff ID
     */
    public static String generateStaffId() {
        int num = staffCounter.incrementAndGet();
        return String.format("STF-%03d", num);
    }

    /**
     * Generate a unique Appointment ID
     * Format: APT-YYYYMMDD-XXXXX
     * 
     * @return Generated appointment ID
     */
    public static String generateAppointmentId() {
        String date = LocalDate.now().format(DATE_FORMAT);
        int num = appointmentCounter.incrementAndGet();
        return String.format("APT-%s-%05d", date, num);
    }

    /**
     * Generate a unique Bill ID
     * Format: BILL-YYYYMMDD-XXXXX
     * 
     * @return Generated bill ID
     */
    public static String generateBillId() {
        String date = LocalDate.now().format(DATE_FORMAT);
        int num = billCounter.incrementAndGet();
        return String.format("BILL-%s-%05d", date, num);
    }

    /**
     * Generate a unique Medical Record ID
     * Format: MR-YYYYMMDD-XXXXX
     * 
     * @return Generated medical record ID
     */
    public static String generateMedicalRecordId() {
        String date = LocalDate.now().format(DATE_FORMAT);
        int num = recordCounter.incrementAndGet();
        return String.format("MR-%s-%05d", date, num);
    }

    /**
     * Generate a unique Room Number
     * Format: F-RXX (F=floor, XX=room number)
     * 
     * @param floor   Floor number
     * @param roomNum Room number on that floor
     * @return Generated room number
     */
    public static String generateRoomNumber(int floor, int roomNum) {
        return String.format("%d-R%02d", floor, roomNum);
    }

    /**
     * Generate a unique Department ID
     * Format: DEPT-XXX
     * 
     * @param name Department name (first 3 chars)
     * @return Generated department ID
     */
    public static String generateDepartmentId(String name) {
        String prefix = name.length() >= 3 ? name.substring(0, 3).toUpperCase() : name.toUpperCase();
        return "DEPT-" + prefix;
    }

    /**
     * Reset all counters (useful for testing)
     */
    public static void resetCounters() {
        patientCounter.set(1000);
        doctorCounter.set(100);
        staffCounter.set(500);
        appointmentCounter.set(1);
        billCounter.set(1);
        recordCounter.set(1);
    }

    /**
     * Set counter values (useful when loading data from file)
     */
    public static void setCounters(int patient, int doctor, int staff, int appointment, int bill, int record) {
        patientCounter.set(patient);
        doctorCounter.set(doctor);
        staffCounter.set(staff);
        appointmentCounter.set(appointment);
        billCounter.set(bill);
        recordCounter.set(record);
    }
}
