package managers;

import models.User;
import utils.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages user authentication and user accounts.
 */
public class UserManager {

    private List<User> users;
    private User currentUser;

    public UserManager() {
        this.users = new ArrayList<>();
        loadUsers();
        initializeDefaultUsers();
    }

    /**
     * Initialize default admin user if no users exist
     */
    private void initializeDefaultUsers() {
        if (users.isEmpty()) {
            // Default admin
            User admin = new User("admin", "admin123", "Administrator", "System Admin");
            users.add(admin);

            // Default receptionist
            User receptionist = new User("reception", "reception123", "Receptionist", "Front Desk");
            users.add(receptionist);

            saveUsers();
        }
    }

    /**
     * Authenticate user with username and password
     */
    public User authenticate(String username, String password) {
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username) &&
                        u.validatePassword(password) &&
                        u.isActive())
                .findFirst();

        if (user.isPresent()) {
            currentUser = user.get();
            return currentUser;
        }
        return null;
    }

    /**
     * Create a new user account
     */
    public User createUser(String username, String password, String role, String displayName) {
        // Check if username already exists
        if (getUserByUsername(username) != null) {
            return null;
        }

        User user = new User(username, password, role, displayName);
        users.add(user);
        saveUsers();
        return user;
    }

    /**
     * Create a doctor user linked to a doctor record
     */
    public User createDoctorUser(String username, String password, String doctorName, String doctorId) {
        User user = createUser(username, password, "Doctor", doctorName);
        if (user != null) {
            user.setLinkedId(doctorId);
            saveUsers();
        }
        return user;
    }

    /**
     * Delete a user account
     */
    public boolean deleteUser(String username) {
        boolean removed = users.removeIf(u -> u.getUsername().equalsIgnoreCase(username)
                && !u.getRole().equals("Administrator")); // Don't delete admins
        if (removed) {
            saveUsers();
        }
        return removed;
    }

    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Get users by role
     */
    public List<User> getUsersByRole(String role) {
        return users.stream()
                .filter(u -> u.getRole().equals(role))
                .toList();
    }

    /**
     * Get currently logged in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logout current user
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Update user password
     */
    public boolean updatePassword(String username, String newPassword) {
        User user = getUserByUsername(username);
        if (user != null) {
            user.setPassword(newPassword);
            saveUsers();
            return true;
        }
        return false;
    }

    /**
     * Load users from file
     */
    private void loadUsers() {
        List<User> loaded = FileHandler.loadUsers();
        if (loaded != null) {
            users = loaded;
        }
    }

    /**
     * Save users to file
     */
    public void saveUsers() {
        FileHandler.saveUsers(users);
    }

    /**
     * Get total user count
     */
    public int getTotalCount() {
        return users.size();
    }
}
