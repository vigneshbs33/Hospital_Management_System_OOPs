package managers;

import models.Patient;
import utils.FileHandler;
import utils.IDGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager class for Patient CRUD operations.
 * Handles all patient-related business logic.
 */
public class PatientManager {

    private List<Patient> patients;

    /**
     * Constructor - loads patients from file
     */
    public PatientManager() {
        this.patients = FileHandler.loadPatients();
        if (this.patients == null) {
            this.patients = new ArrayList<>();
        }
    }

    /**
     * Add a new patient
     * 
     * @param patient Patient to add
     * @return Generated patient ID
     */
    public String addPatient(Patient patient) {
        String patientId = IDGenerator.generatePatientId();
        patient.setPatientId(patientId);
        patient.setId(patientId);
        patients.add(patient);
        saveData();
        return patientId;
    }

    /**
     * Update an existing patient
     * 
     * @param patient Patient with updated data
     * @return true if update successful
     */
    public boolean updatePatient(Patient patient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientId().equals(patient.getPatientId())) {
                patients.set(i, patient);
                saveData();
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a patient by ID
     * 
     * @param patientId Patient ID to delete
     * @return true if deletion successful
     */
    public boolean deletePatient(String patientId) {
        boolean removed = patients.removeIf(p -> p.getPatientId().equals(patientId));
        if (removed) {
            saveData();
        }
        return removed;
    }

    /**
     * Get patient by ID
     * 
     * @param patientId Patient ID
     * @return Patient or null if not found
     */
    public Patient getPatientById(String patientId) {
        return patients.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all patients
     * 
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    /**
     * Search patients by name
     * 
     * @param name Name to search (case-insensitive, partial match)
     * @return Matching patients
     */
    public List<Patient> searchByName(String name) {
        String searchTerm = name.toLowerCase();
        return patients.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Search patients by phone
     * 
     * @param phone Phone number to search
     * @return Matching patients
     */
    public List<Patient> searchByPhone(String phone) {
        return patients.stream()
                .filter(p -> p.getPhone() != null && p.getPhone().contains(phone))
                .collect(Collectors.toList());
    }

    /**
     * Get patients admitted today
     * 
     * @return List of patients admitted today
     */
    public List<Patient> getPatientsAdmittedToday() {
        LocalDate today = LocalDate.now();
        return patients.stream()
                .filter(p -> p.isAdmitted() &&
                        p.getAdmissionDate() != null &&
                        p.getAdmissionDate().equals(today))
                .collect(Collectors.toList());
    }

    /**
     * Get all admitted patients
     * 
     * @return List of currently admitted patients
     */
    public List<Patient> getAdmittedPatients() {
        return patients.stream()
                .filter(Patient::isAdmitted)
                .collect(Collectors.toList());
    }

    /**
     * Get patients by assigned doctor
     * 
     * @param doctorId Doctor's ID
     * @return List of patients assigned to the doctor
     */
    public List<Patient> getPatientsByDoctor(String doctorId) {
        return patients.stream()
                .filter(p -> doctorId.equals(p.getAssignedDoctorId()))
                .collect(Collectors.toList());
    }

    /**
     * Get total patient count
     * 
     * @return Total number of patients
     */
    public int getTotalCount() {
        return patients.size();
    }

    /**
     * Get admitted patient count
     * 
     * @return Number of currently admitted patients
     */
    public int getAdmittedCount() {
        return (int) patients.stream().filter(Patient::isAdmitted).count();
    }

    /**
     * Admit a patient
     * 
     * @param patientId  Patient ID
     * @param roomNumber Room number
     * @param doctorId   Assigned doctor ID
     * @return true if successful
     */
    public boolean admitPatient(String patientId, String roomNumber, String doctorId) {
        Patient patient = getPatientById(patientId);
        if (patient != null) {
            patient.admit(roomNumber, doctorId);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Discharge a patient
     * 
     * @param patientId Patient ID
     * @return true if successful
     */
    public boolean dischargePatient(String patientId) {
        Patient patient = getPatientById(patientId);
        if (patient != null) {
            patient.discharge();
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Save data to file
     */
    private void saveData() {
        FileHandler.savePatients(patients);
    }

    /**
     * Reload data from file
     */
    public void reloadData() {
        this.patients = FileHandler.loadPatients();
        if (this.patients == null) {
            this.patients = new ArrayList<>();
        }
    }
}
