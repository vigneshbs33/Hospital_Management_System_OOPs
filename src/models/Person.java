package models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Abstract base class representing a person in the hospital system.
 * Demonstrates OOP concepts: Abstraction and Encapsulation.
 * 
 * This class provides common attributes and behaviors for all person types
 * (Patient, Doctor, Staff) in the hospital management system.
 */
public abstract class Person implements Serializable {
    
    // Private fields demonstrating Encapsulation
    private String id;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private String address;
    
    /**
     * Default constructor
     */
    public Person() {
        this.id = "";
        this.name = "";
        this.gender = "";
        this.phone = "";
        this.email = "";
        this.address = "";
    }
    
    /**
     * Parameterized constructor
     */
    public Person(String id, String name, LocalDate dateOfBirth, String gender, 
                  String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
    
    /**
     * Abstract method demonstrating Abstraction.
     * Each subclass must implement this to return their specific role.
     * @return The role of the person in the hospital
     */
    public abstract String getRole();
    
    /**
     * Calculate age based on date of birth
     * @return Age in years
     */
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
    
    // Getters and Setters demonstrating Encapsulation
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role='" + getRole() + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
