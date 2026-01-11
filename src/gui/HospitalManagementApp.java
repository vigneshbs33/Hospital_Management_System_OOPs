package gui;

import gui.components.SidebarPanel;
import managers.HospitalManager;
import managers.UserManager;
import models.User;

import javax.swing.*;
import java.awt.*;

/**
 * Main application frame for the Hospital Management System.
 * Features a sidebar navigation and CardLayout for panel switching.
 */
public class HospitalManagementApp extends JFrame {

    private static final Color BACKGROUND = new Color(248, 250, 252);

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout contentCardLayout;

    private LoginPanel loginPanel;
    private SidebarPanel sidebarPanel;
    private DashboardPanel dashboardPanel;
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    private RoomPanel roomPanel;
    private BillingPanel billingPanel;

    private User currentUser;
    private UserManager userManager;

    /**
     * Create the main application
     */
    public HospitalManagementApp() {
        initializeFrame();
        initializeUI();
        showLoginScreen();
    }

    /**
     * Initialize frame properties
     */
    private void initializeFrame() {
        setTitle("MedCare Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND);

        // Login panel
        loginPanel = new LoginPanel();
        userManager = loginPanel.getUserManager();
        loginPanel.setLoginListener(user -> {
            currentUser = user;
            showMainApplication();
        });
        mainPanel.add(loginPanel, "login");

        // Main application panel (sidebar + content)
        JPanel appPanel = new JPanel(new BorderLayout());
        appPanel.setBackground(BACKGROUND);

        // Sidebar
        sidebarPanel = new SidebarPanel();
        sidebarPanel.setNavigationListener(this::navigateTo);
        sidebarPanel.setLogoutListener(this::logout);
        appPanel.add(sidebarPanel, BorderLayout.WEST);

        // Content area with CardLayout
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(BACKGROUND);

        // Initialize all panels
        dashboardPanel = new DashboardPanel();
        patientPanel = new PatientPanel();
        doctorPanel = new DoctorPanel();
        appointmentPanel = new AppointmentPanel();
        roomPanel = new RoomPanel();
        billingPanel = new BillingPanel();

        // Add panels to content area
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(patientPanel, "patients");
        contentPanel.add(doctorPanel, "doctors");
        contentPanel.add(appointmentPanel, "appointments");
        contentPanel.add(roomPanel, "rooms");
        contentPanel.add(billingPanel, "billing");

        appPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(appPanel, "main");

        add(mainPanel);
    }

    /**
     * Show login screen
     */
    private void showLoginScreen() {
        cardLayout.show(mainPanel, "login");
    }

    /**
     * Show main application with role-based access
     */
    private void showMainApplication() {
        // Update sidebar with user role for role-based menu
        sidebarPanel.setUserRole(currentUser.getRole(), currentUser.getDisplayName());

        // Set up doctor-specific filtering for appointments
        if ("Doctor".equals(currentUser.getRole()) && currentUser.getLinkedId() != null) {
            // Doctor sees only their appointments
            appointmentPanel.setCurrentDoctorId(currentUser.getLinkedId());
            patientPanel.setCurrentDoctorId(currentUser.getLinkedId());
        } else {
            // Admin/Receptionist sees all
            appointmentPanel.clearDoctorFilter();
            patientPanel.clearDoctorFilter();
        }

        cardLayout.show(mainPanel, "main");
        navigateTo("dashboard");
    }

    /**
     * Navigate to a specific panel
     * 
     * @param panelName Name of panel to show
     */
    private void navigateTo(String panelName) {
        contentCardLayout.show(contentPanel, panelName);

        // Refresh the panel when navigating to it
        switch (panelName) {
            case "dashboard":
                dashboardPanel.refreshStats();
                break;
            case "patients":
                patientPanel.refresh();
                break;
            case "doctors":
                doctorPanel.refresh();
                break;
            case "appointments":
                appointmentPanel.refresh();
                break;
            case "rooms":
                roomPanel.refresh();
                break;
            case "billing":
                billingPanel.refresh();
                break;
        }
    }

    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Get user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Logout and return to login screen
     */
    public void logout() {
        if (userManager != null) {
            userManager.logout();
        }
        currentUser = null;
        loginPanel.reset();
        showLoginScreen();

        JOptionPane.showMessageDialog(this,
                "You have been logged out successfully.",
                "Logged Out",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
