package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * MedicalRecord class for managing patient medical records.
 * Demonstrates OOP concept: Composition.
 */
public class MedicalRecord implements Serializable {

    private String recordId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String diagnosis;
    private String symptoms;
    private List<String> prescriptions;
    private String notes;
    private LocalDateTime date;
    private String followUpInstructions;
    private List<String> testResults;

    /**
     * Default constructor
     */
    public MedicalRecord() {
        this.prescriptions = new ArrayList<>();
        this.testResults = new ArrayList<>();
        this.date = LocalDateTime.now();
    }

    /**
     * Parameterized constructor
     */
    public MedicalRecord(String recordId, String patientId, String patientName,
            String doctorId, String doctorName, String diagnosis) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.prescriptions = new ArrayList<>();
        this.testResults = new ArrayList<>();
        this.date = LocalDateTime.now();
    }

    /**
     * Add a prescription
     * 
     * @param prescription Prescription details
     */
    public void addPrescription(String prescription) {
        if (prescriptions == null) {
            prescriptions = new ArrayList<>();
        }
        prescriptions.add(prescription);
    }

    /**
     * Add a test result
     * 
     * @param testResult Test result details
     */
    public void addTestResult(String testResult) {
        if (testResults == null) {
            testResults = new ArrayList<>();
        }
        testResults.add(testResult);
    }

    /**
     * Get formatted date
     * 
     * @return Formatted date string
     */
    public String getFormattedDate() {
        if (date == null)
            return "";
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }

    // Getters and Setters

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public List<String> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<String> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getFollowUpInstructions() {
        return followUpInstructions;
    }

    public void setFollowUpInstructions(String followUpInstructions) {
        this.followUpInstructions = followUpInstructions;
    }

    public List<String> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<String> testResults) {
        this.testResults = testResults;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordId='" + recordId + '\'' +
                ", patient='" + patientName + '\'' +
                ", doctor='" + doctorName + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", date=" + getFormattedDate() +
                '}';
    }
}
