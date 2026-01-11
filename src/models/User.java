package models;

import java.io.Serializable;

/**
 * User class for authentication.
 * Stores login credentials and role information.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String role; // Administrator, Doctor, Receptionist
    private String displayName;
    private String linkedId; // Links to Doctor ID if role is Doctor
    private boolean isActive;

    /**
     * Create a new user
     */
    public User() {
        this.isActive = true;
    }

    /**
     * Create a user with credentials
     */
    public User(String username, String password, String role, String displayName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.displayName = displayName;
        this.isActive = true;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Validate password
     */
    public boolean validatePassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }

    @Override
    public String toString() {
        return displayName + " (" + role + ")";
    }
}
