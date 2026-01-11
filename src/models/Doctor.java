package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Doctor class extending Person.
 * Demonstrates OOP concept: Inheritance and Polymorphism.
 * 
 * Contains doctor-specific attributes like specialization, department,
 * and consultation fee information.
 */
public class Doctor extends Person implements Serializable {
    
    private String doctorId;
    private String specialization;
    private String department;
    private String qualification;
    private double consultationFee;
    private List<String> availableDays;
    private String workingHours;
    private int experienceYears;
    private List<String> patientIds;
    
    /**
     * Default constructor
     */
    public Doctor() {
        super();
        this.availableDays = new ArrayList<>();
        this.patientIds = new ArrayList<>();
    }
    
    /**
     * Parameterized constructor
     */
    public Doctor(String id, String name, LocalDate dateOfBirth, String gender,
                  String phone, String email, String address, String doctorId,
                  String specialization, String department, String qualification,
                  double consultationFee) {
        super(id, name, dateOfBirth, gender, phone, email, address);
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.department = department;
        this.qualification = qualification;
        this.consultationFee = consultationFee;
        this.availableDays = new ArrayList<>();
        this.patientIds = new ArrayList<>();
    }
    
    /**
     * Implementation of abstract method from Person class.
     * Demonstrates Polymorphism.
     */
    @Override
    public String getRole() {
        return "Doctor";
    }
    
    /**
     * Add a patient to the doctor's patient list
     * @param patientId Patient's ID
     */
    public void addPatient(String patientId) {
        if (patientIds == null) {
            patientIds = new ArrayList<>();
        }
        if (!patientIds.contains(patientId)) {
            patientIds.add(patientId);
        }
    }
    
    /**
     * Remove a patient from the doctor's patient list
     * @param patientId Patient's ID
     */
    public void removePatient(String patientId) {
        if (patientIds != null) {
            patientIds.remove(patientId);
        }
    }
    
    /**
     * Check if doctor is available on a specific day
     * @param day Day of the week
     * @return true if available
     */
    public boolean isAvailableOn(String day) {
        return availableDays != null && availableDays.contains(day);
    }
    
    /**
     * Get the number of patients assigned to this doctor
     * @return Patient count
     */
    public int getPatientCount() {
        return patientIds != null ? patientIds.size() : 0;
    }
    
    // Getters and Setters
    
    public String getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getQualification() {
        return qualification;
    }
    
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    
    public double getConsultationFee() {
        return consultationFee;
    }
    
    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }
    
    public List<String> getAvailableDays() {
        return availableDays;
    }
    
    public void setAvailableDays(List<String> availableDays) {
        this.availableDays = availableDays;
    }
    
    public String getWorkingHours() {
        return workingHours;
    }
    
    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }
    
    public int getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    public List<String> getPatientIds() {
        return patientIds;
    }
    
    public void setPatientIds(List<String> patientIds) {
        this.patientIds = patientIds;
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId='" + doctorId + '\'' +
                ", name='" + getName() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", department='" + department + '\'' +
                ", consultationFee=" + consultationFee +
                '}';
    }
}
