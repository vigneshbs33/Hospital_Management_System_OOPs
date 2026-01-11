package managers;

import models.Appointment;
import models.Appointment.AppointmentStatus;
import utils.FileHandler;
import utils.IDGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager class for Appointment operations.
 * Handles scheduling, rescheduling, and cancellation of appointments.
 */
public class AppointmentManager {

    private List<Appointment> appointments;

    /**
     * Constructor - loads appointments from file
     */
    public AppointmentManager() {
        this.appointments = FileHandler.loadAppointments();
        if (this.appointments == null) {
            this.appointments = new ArrayList<>();
        }
    }

    /**
     * Schedule a new appointment
     * 
     * @param appointment Appointment to schedule
     * @return Generated appointment ID
     */
    public String scheduleAppointment(Appointment appointment) {
        String appointmentId = IDGenerator.generateAppointmentId();
        appointment.setAppointmentId(appointmentId);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointments.add(appointment);
        saveData();
        return appointmentId;
    }

    /**
     * Update an existing appointment
     * 
     * @param appointment Appointment with updated data
     * @return true if update successful
     */
    public boolean updateAppointment(Appointment appointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(appointment.getAppointmentId())) {
                appointments.set(i, appointment);
                saveData();
                return true;
            }
        }
        return false;
    }

    /**
     * Cancel an appointment
     * 
     * @param appointmentId Appointment ID to cancel
     * @return true if cancellation successful
     */
    public boolean cancelAppointment(String appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Complete an appointment
     * 
     * @param appointmentId Appointment ID to mark complete
     * @return true if successful
     */
    public boolean completeAppointment(String appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Delete an appointment
     * 
     * @param appointmentId Appointment ID to delete
     * @return true if deletion successful
     */
    public boolean deleteAppointment(String appointmentId) {
        boolean removed = appointments.removeIf(a -> a.getAppointmentId().equals(appointmentId));
        if (removed) {
            saveData();
        }
        return removed;
    }

    /**
     * Get appointment by ID
     * 
     * @param appointmentId Appointment ID
     * @return Appointment or null if not found
     */
    public Appointment getAppointmentById(String appointmentId) {
        return appointments.stream()
                .filter(a -> a.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all appointments
     * 
     * @return List of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Get appointments for a specific date
     * 
     * @param date Date to filter
     * @return Appointments on that date
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointments.stream()
                .filter(a -> a.getDateTime() != null &&
                        a.getDateTime().toLocalDate().equals(date))
                .sorted((a1, a2) -> a1.getDateTime().compareTo(a2.getDateTime()))
                .collect(Collectors.toList());
    }

    /**
     * Get today's appointments
     * 
     * @return List of today's appointments
     */
    public List<Appointment> getTodaysAppointments() {
        return getAppointmentsByDate(LocalDate.now());
    }

    /**
     * Get appointments for a specific patient
     * 
     * @param patientId Patient ID
     * @return Patient's appointments
     */
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointments.stream()
                .filter(a -> patientId.equals(a.getPatientId()))
                .sorted((a1, a2) -> a2.getDateTime().compareTo(a1.getDateTime()))
                .collect(Collectors.toList());
    }

    /**
     * Get appointments for a specific doctor
     * 
     * @param doctorId Doctor ID
     * @return Doctor's appointments
     */
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointments.stream()
                .filter(a -> doctorId.equals(a.getDoctorId()))
                .sorted((a1, a2) -> a1.getDateTime().compareTo(a2.getDateTime()))
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming appointments
     * 
     * @return List of future appointments
     */
    public List<Appointment> getUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        return appointments.stream()
                .filter(a -> a.getDateTime() != null &&
                        a.getDateTime().isAfter(now) &&
                        a.getStatus() == AppointmentStatus.SCHEDULED)
                .sorted((a1, a2) -> a1.getDateTime().compareTo(a2.getDateTime()))
                .collect(Collectors.toList());
    }

    /**
     * Get appointments by status
     * 
     * @param status Appointment status
     * @return Matching appointments
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointments.stream()
                .filter(a -> a.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Check for scheduling conflicts
     * 
     * @param doctorId Doctor ID
     * @param dateTime Proposed date/time
     * @return true if conflict exists
     */
    public boolean hasConflict(String doctorId, LocalDateTime dateTime) {
        return appointments.stream()
                .anyMatch(a -> doctorId.equals(a.getDoctorId()) &&
                        a.getStatus() == AppointmentStatus.SCHEDULED &&
                        a.getDateTime() != null &&
                        Math.abs(a.getDateTime().toLocalTime().toSecondOfDay() -
                                dateTime.toLocalTime().toSecondOfDay()) < 1800
                        &&
                        a.getDateTime().toLocalDate().equals(dateTime.toLocalDate()));
    }

    /**
     * Get total appointment count
     * 
     * @return Total number of appointments
     */
    public int getTotalCount() {
        return appointments.size();
    }

    /**
     * Get today's appointment count
     * 
     * @return Number of today's appointments
     */
    public int getTodaysCount() {
        return getTodaysAppointments().size();
    }

    /**
     * Get count by status
     * 
     * @param status Appointment status
     * @return Count of appointments with that status
     */
    public int getCountByStatus(AppointmentStatus status) {
        return (int) appointments.stream()
                .filter(a -> a.getStatus() == status)
                .count();
    }

    /**
     * Save data to file
     */
    private void saveData() {
        FileHandler.saveAppointments(appointments);
    }

    /**
     * Reload data from file
     */
    public void reloadData() {
        this.appointments = FileHandler.loadAppointments();
        if (this.appointments == null) {
            this.appointments = new ArrayList<>();
        }
    }
}
