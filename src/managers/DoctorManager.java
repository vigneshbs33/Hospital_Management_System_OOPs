package managers;

import models.Doctor;
import utils.FileHandler;
import utils.IDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager class for Doctor CRUD operations.
 * Handles all doctor-related business logic.
 */
public class DoctorManager {

    private List<Doctor> doctors;

    /**
     * Constructor - loads doctors from file
     */
    public DoctorManager() {
        this.doctors = FileHandler.loadDoctors();
        if (this.doctors == null) {
            this.doctors = new ArrayList<>();
        }
    }

    /**
     * Add a new doctor
     * 
     * @param doctor Doctor to add
     * @return Generated doctor ID
     */
    public String addDoctor(Doctor doctor) {
        String doctorId = IDGenerator.generateDoctorId();
        doctor.setDoctorId(doctorId);
        doctor.setId(doctorId);
        doctors.add(doctor);
        saveData();
        return doctorId;
    }

    /**
     * Update an existing doctor
     * 
     * @param doctor Doctor with updated data
     * @return true if update successful
     */
    public boolean updateDoctor(Doctor doctor) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getDoctorId().equals(doctor.getDoctorId())) {
                doctors.set(i, doctor);
                saveData();
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a doctor by ID
     * 
     * @param doctorId Doctor ID to delete
     * @return true if deletion successful
     */
    public boolean deleteDoctor(String doctorId) {
        boolean removed = doctors.removeIf(d -> d.getDoctorId().equals(doctorId));
        if (removed) {
            saveData();
        }
        return removed;
    }

    /**
     * Get doctor by ID
     * 
     * @param doctorId Doctor ID
     * @return Doctor or null if not found
     */
    public Doctor getDoctorById(String doctorId) {
        return doctors.stream()
                .filter(d -> d.getDoctorId().equals(doctorId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all doctors
     * 
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    /**
     * Search doctors by name
     * 
     * @param name Name to search (case-insensitive, partial match)
     * @return Matching doctors
     */
    public List<Doctor> searchByName(String name) {
        String searchTerm = name.toLowerCase();
        return doctors.stream()
                .filter(d -> d.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Get doctors by specialization
     * 
     * @param specialization Specialization to filter
     * @return Matching doctors
     */
    public List<Doctor> getBySpecialization(String specialization) {
        return doctors.stream()
                .filter(d -> specialization.equalsIgnoreCase(d.getSpecialization()))
                .collect(Collectors.toList());
    }

    /**
     * Get doctors by department
     * 
     * @param department Department to filter
     * @return Matching doctors
     */
    public List<Doctor> getByDepartment(String department) {
        return doctors.stream()
                .filter(d -> department.equalsIgnoreCase(d.getDepartment()))
                .collect(Collectors.toList());
    }

    /**
     * Get all unique specializations
     * 
     * @return List of unique specializations
     */
    public List<String> getAllSpecializations() {
        return doctors.stream()
                .map(Doctor::getSpecialization)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get all unique departments
     * 
     * @return List of unique departments
     */
    public List<String> getAllDepartments() {
        return doctors.stream()
                .map(Doctor::getDepartment)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get total doctor count
     * 
     * @return Total number of doctors
     */
    public int getTotalCount() {
        return doctors.size();
    }

    /**
     * Get doctors available on a specific day
     * 
     * @param day Day of the week
     * @return Available doctors
     */
    public List<Doctor> getAvailableOn(String day) {
        return doctors.stream()
                .filter(d -> d.isAvailableOn(day))
                .collect(Collectors.toList());
    }

    /**
     * Add a patient to doctor's list
     * 
     * @param doctorId  Doctor ID
     * @param patientId Patient ID
     * @return true if successful
     */
    public boolean addPatientToDoctor(String doctorId, String patientId) {
        Doctor doctor = getDoctorById(doctorId);
        if (doctor != null) {
            doctor.addPatient(patientId);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Save data to file
     */
    private void saveData() {
        FileHandler.saveDoctors(doctors);
    }

    /**
     * Reload data from file
     */
    public void reloadData() {
        this.doctors = FileHandler.loadDoctors();
        if (this.doctors == null) {
            this.doctors = new ArrayList<>();
        }
    }
}
