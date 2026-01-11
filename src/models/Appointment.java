package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Appointment class for managing patient-doctor appointments.
 * Demonstrates OOP concept: Composition (contains references to Patient and
 * Doctor).
 */
public class Appointment implements Serializable {

    private String appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private LocalDateTime dateTime;
    private AppointmentStatus status;
    private String notes;
    private String purpose;
    private LocalDateTime createdAt;

    /**
     * Appointment status enumeration
     */
    public enum AppointmentStatus {
        SCHEDULED("Scheduled"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled"),
        NO_SHOW("No Show");

        private final String displayName;

        AppointmentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Default constructor
     */
    public Appointment() {
        this.status = AppointmentStatus.SCHEDULED;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Parameterized constructor
     */
    public Appointment(String appointmentId, String patientId, String patientName,
            String doctorId, String doctorName, LocalDateTime dateTime,
            String purpose) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.dateTime = dateTime;
        this.purpose = purpose;
        this.status = AppointmentStatus.SCHEDULED;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Mark appointment as completed
     */
    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
    }

    /**
     * Cancel the appointment
     */
    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
    }

    /**
     * Check if appointment is today
     * 
     * @return true if appointment is today
     */
    public boolean isToday() {
        if (dateTime == null)
            return false;
        LocalDateTime now = LocalDateTime.now();
        return dateTime.toLocalDate().equals(now.toLocalDate());
    }

    /**
     * Check if appointment is upcoming
     * 
     * @return true if appointment is in the future
     */
    public boolean isUpcoming() {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Get formatted date string
     * 
     * @return Formatted date
     */
    public String getFormattedDate() {
        if (dateTime == null)
            return "";
        return dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /**
     * Get formatted time string
     * 
     * @return Formatted time
     */
    public String getFormattedTime() {
        if (dateTime == null)
            return "";
        return dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    /**
     * Get formatted date and time
     * 
     * @return Formatted date and time
     */
    public String getFormattedDateTime() {
        if (dateTime == null)
            return "";
        return dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }

    // Getters and Setters

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + appointmentId + '\'' +
                ", patient='" + patientName + '\'' +
                ", doctor='" + doctorName + '\'' +
                ", dateTime=" + getFormattedDateTime() +
                ", status=" + status.getDisplayName() +
                '}';
    }
}
