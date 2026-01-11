package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Patient class extending Person.
 * Demonstrates OOP concept: Inheritance.
 * 
 * Contains patient-specific attributes like blood group, medical history,
 * and room assignment information.
 */
public class Patient extends Person implements Serializable {
    
    private String patientId;
    private String bloodGroup;
    private LocalDate admissionDate;
    private List<String> medicalHistory;
    private String assignedDoctorId;
    private String roomNumber;
    private boolean isAdmitted;
    private String emergencyContact;
    private String emergencyContactPhone;
    
    /**
     * Default constructor
     */
    public Patient() {
        super();
        this.medicalHistory = new ArrayList<>();
        this.isAdmitted = false;
    }
    
    /**
     * Parameterized constructor
     */
    public Patient(String id, String name, LocalDate dateOfBirth, String gender,
                   String phone, String email, String address, String patientId,
                   String bloodGroup) {
        super(id, name, dateOfBirth, gender, phone, email, address);
        this.patientId = patientId;
        this.bloodGroup = bloodGroup;
        this.medicalHistory = new ArrayList<>();
        this.isAdmitted = false;
    }
    
    /**
     * Implementation of abstract method from Person class.
     * Demonstrates Polymorphism.
     */
    @Override
    public String getRole() {
        return "Patient";
    }
    
    /**
     * Add a medical record to patient's history
     * @param record Medical record entry
     */
    public void addMedicalHistory(String record) {
        if (medicalHistory == null) {
            medicalHistory = new ArrayList<>();
        }
        medicalHistory.add(record);
    }
    
    /**
     * Admit the patient to the hospital
     * @param roomNumber Room number assigned
     * @param doctorId Assigned doctor's ID
     */
    public void admit(String roomNumber, String doctorId) {
        this.isAdmitted = true;
        this.roomNumber = roomNumber;
        this.assignedDoctorId = doctorId;
        this.admissionDate = LocalDate.now();
    }
    
    /**
     * Discharge the patient from the hospital
     */
    public void discharge() {
        this.isAdmitted = false;
        this.roomNumber = null;
    }
    
    // Getters and Setters
    
    public String getPatientId() {
        return patientId;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public LocalDate getAdmissionDate() {
        return admissionDate;
    }
    
    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }
    
    public List<String> getMedicalHistory() {
        return medicalHistory;
    }
    
    public void setMedicalHistory(List<String> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    
    public String getAssignedDoctorId() {
        return assignedDoctorId;
    }
    
    public void setAssignedDoctorId(String assignedDoctorId) {
        this.assignedDoctorId = assignedDoctorId;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public boolean isAdmitted() {
        return isAdmitted;
    }
    
    public void setAdmitted(boolean admitted) {
        isAdmitted = admitted;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }
    
    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + getName() + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", isAdmitted=" + isAdmitted +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }
}
