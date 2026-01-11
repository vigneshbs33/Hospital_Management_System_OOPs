package models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Staff class extending Person.
 * Demonstrates OOP concept: Inheritance and Polymorphism.
 * 
 * Contains staff-specific attributes like role, department, shift, and salary.
 */
public class Staff extends Person implements Serializable {
    
    private String staffId;
    private String staffRole;
    private String department;
    private String shift;
    private double salary;
    private LocalDate joiningDate;
    
    /**
     * Staff role enumeration
     */
    public enum StaffRole {
        NURSE("Nurse"),
        RECEPTIONIST("Receptionist"),
        LAB_TECHNICIAN("Lab Technician"),
        PHARMACIST("Pharmacist"),
        ADMINISTRATOR("Administrator"),
        ACCOUNTANT("Accountant"),
        SECURITY("Security"),
        CLEANER("Cleaner");
        
        private final String displayName;
        
        StaffRole(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Shift types
     */
    public enum Shift {
        MORNING("Morning (6AM - 2PM)"),
        AFTERNOON("Afternoon (2PM - 10PM)"),
        NIGHT("Night (10PM - 6AM)"),
        GENERAL("General (9AM - 5PM)");
        
        private final String displayName;
        
        Shift(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Default constructor
     */
    public Staff() {
        super();
    }
    
    /**
     * Parameterized constructor
     */
    public Staff(String id, String name, LocalDate dateOfBirth, String gender,
                 String phone, String email, String address, String staffId,
                 String staffRole, String department, String shift, double salary) {
        super(id, name, dateOfBirth, gender, phone, email, address);
        this.staffId = staffId;
        this.staffRole = staffRole;
        this.department = department;
        this.shift = shift;
        this.salary = salary;
        this.joiningDate = LocalDate.now();
    }
    
    /**
     * Implementation of abstract method from Person class.
     * Demonstrates Polymorphism - returns the specific staff role.
     */
    @Override
    public String getRole() {
        return staffRole != null ? staffRole : "Staff";
    }
    
    // Getters and Setters
    
    public String getStaffId() {
        return staffId;
    }
    
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
    
    public String getStaffRole() {
        return staffRole;
    }
    
    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getShift() {
        return shift;
    }
    
    public void setShift(String shift) {
        this.shift = shift;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public LocalDate getJoiningDate() {
        return joiningDate;
    }
    
    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }
    
    @Override
    public String toString() {
        return "Staff{" +
                "staffId='" + staffId + '\'' +
                ", name='" + getName() + '\'' +
                ", role='" + staffRole + '\'' +
                ", department='" + department + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }
}
