package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Department class for managing hospital departments.
 */
public class Department implements Serializable {

    private String departmentId;
    private String name;
    private String headDoctorId;
    private String headDoctorName;
    private String description;
    private String location;
    private List<String> doctorIds;
    private String phoneExtension;

    /**
     * Default constructor
     */
    public Department() {
        this.doctorIds = new ArrayList<>();
    }

    /**
     * Parameterized constructor
     */
    public Department(String departmentId, String name, String description) {
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
        this.doctorIds = new ArrayList<>();
    }

    /**
     * Add a doctor to the department
     * 
     * @param doctorId Doctor's ID
     */
    public void addDoctor(String doctorId) {
        if (doctorIds == null) {
            doctorIds = new ArrayList<>();
        }
        if (!doctorIds.contains(doctorId)) {
            doctorIds.add(doctorId);
        }
    }

    /**
     * Remove a doctor from the department
     * 
     * @param doctorId Doctor's ID
     */
    public void removeDoctor(String doctorId) {
        if (doctorIds != null) {
            doctorIds.remove(doctorId);
        }
    }

    /**
     * Get count of doctors in department
     * 
     * @return Doctor count
     */
    public int getDoctorCount() {
        return doctorIds != null ? doctorIds.size() : 0;
    }

    // Getters and Setters

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadDoctorId() {
        return headDoctorId;
    }

    public void setHeadDoctorId(String headDoctorId) {
        this.headDoctorId = headDoctorId;
    }

    public String getHeadDoctorName() {
        return headDoctorName;
    }

    public void setHeadDoctorName(String headDoctorName) {
        this.headDoctorName = headDoctorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(List<String> doctorIds) {
        this.doctorIds = doctorIds;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id='" + departmentId + '\'' +
                ", name='" + name + '\'' +
                ", doctorCount=" + getDoctorCount() +
                '}';
    }
}
