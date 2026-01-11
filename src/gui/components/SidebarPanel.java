package gui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Sidebar navigation panel for the hospital management system.
 * Features navigation buttons with icons and hover effects.
 */
public class SidebarPanel extends JPanel {

    // Color constants
    private static final Color SIDEBAR_BG = new Color(30, 41, 59);
    private static final Color ITEM_HOVER = new Color(51, 65, 85);
    private static final Color ITEM_ACTIVE = new Color(37, 99, 235);
    private static final Color TEXT_COLOR = new Color(226, 232, 240);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);

    private JPanel menuPanel;
    private NavItem activeItem;
    private NavigationListener navigationListener;
    private LogoutListener logoutListener;
    private String userRole = "Administrator";
    private String userName = "User";
    private JLabel roleLabel;
    private JLabel userLabel;

    /**
     * Interface for navigation callbacks
     */
    public interface NavigationListener {
        void onNavigate(String panelName);
    }

    /**
     * Interface for logout callbacks
     */
    public interface LogoutListener {
        void onLogout();
    }

    /**
     * Create sidebar panel with default admin role
     */
    public SidebarPanel() {
        this("Administrator", "Admin");
    }

    /**
     * Create sidebar panel with specific role
     * 
     * @param role     User role (Administrator, Doctor, Receptionist)
     * @param userName User name to display
     */
    public SidebarPanel(String role, String userName) {
        this.userRole = role;
        this.userName = userName;
        initializeUI();
    }

    /**
     * Initialize the sidebar UI
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(SIDEBAR_BG);
        setPreferredSize(new Dimension(250, 0));

        // Header with hospital name
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Menu items
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(SIDEBAR_BG);
        menuPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Add role-based navigation items
        addMenuItemsForRole();

        add(menuPanel, BorderLayout.CENTER);

        // Footer with logout
        JPanel footerPanel = createFooter();
        add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Add menu items based on user role
     */
    private void addMenuItemsForRole() {
        menuPanel.removeAll();

        // Dashboard - visible to all
        addMenuItem("Dashboard", "dashboard", true);

        switch (userRole) {
            case "Administrator":
                // Admin sees everything
                addMenuItem("Patients", "patients", false);
                addMenuItem("Doctors", "doctors", false);
                addMenuItem("Appointments", "appointments", false);
                addMenuItem("Rooms", "rooms", false);
                addMenuItem("Billing", "billing", false);
                break;

            case "Doctor":
                // Doctor sees patients and appointments only
                addMenuItem("My Patients", "patients", false);
                addMenuItem("Appointments", "appointments", false);
                break;

            case "Receptionist":
                // Receptionist sees patients, appointments, billing
                addMenuItem("Patients", "patients", false);
                addMenuItem("Appointments", "appointments", false);
                addMenuItem("Billing", "billing", false);
                break;

            default:
                // Default to admin access
                addMenuItem("Patients", "patients", false);
                addMenuItem("Doctors", "doctors", false);
                addMenuItem("Appointments", "appointments", false);
                addMenuItem("Rooms", "rooms", false);
                addMenuItem("Billing", "billing", false);
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    /**
     * Set user role and update menu
     * 
     * @param role     User role
     * @param userName User name
     */
    public void setUserRole(String role, String userName) {
        this.userRole = role;
        this.userName = userName;
        if (userLabel != null) {
            userLabel.setText(userName);
        }
        if (roleLabel != null) {
            roleLabel.setText(role);
        }
        addMenuItemsForRole();
    }

    /**
     * Create header with hospital logo and name
     */
    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(SIDEBAR_BG);
        header.setBorder(new EmptyBorder(25, 20, 25, 20));

        // Hospital icon (using a simple plus symbol)
        JLabel iconLabel = new JLabel("\u2695"); // Medical symbol
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 40));
        iconLabel.setForeground(new Color(96, 165, 250));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Hospital name
        JLabel nameLabel = new JLabel("MedCare");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Hospital Management");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(iconLabel);
        header.add(Box.createVerticalStrut(10));
        header.add(nameLabel);
        header.add(Box.createVerticalStrut(3));
        header.add(subtitleLabel);

        // Separator
        JPanel separator = new JPanel();
        separator.setBackground(new Color(51, 65, 85));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(Box.createVerticalStrut(20));
        header.add(separator);

        return header;
    }

    /**
     * Create footer with logout button and version info
     */
    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(SIDEBAR_BG);
        footer.setBorder(new EmptyBorder(15, 20, 20, 20));

        // User info section
        userLabel = new JLabel(userName);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(TEXT_COLOR);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.add(userLabel);
        footer.add(Box.createVerticalStrut(3));

        roleLabel = new JLabel(userRole);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(TEXT_MUTED);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.add(roleLabel);
        footer.add(Box.createVerticalStrut(15));

        // Logout button
        JPanel logoutBtn = new JPanel();
        logoutBtn.setLayout(new BorderLayout());
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(new EmptyBorder(10, 15, 10, 15));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoutLabel = new JLabel("\u2190 Logout");
        logoutLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutLabel.setForeground(Color.WHITE);
        logoutBtn.add(logoutLabel, BorderLayout.CENTER);

        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (logoutListener != null) {
                    logoutListener.onLogout();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(220, 38, 38));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(239, 68, 68));
            }
        });
        footer.add(logoutBtn);
        footer.add(Box.createVerticalStrut(15));

        // Version info
        JLabel versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(TEXT_MUTED);
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.add(versionLabel);

        return footer;
    }

    /**
     * Set logout listener
     */
    public void setLogoutListener(LogoutListener listener) {
        this.logoutListener = listener;
    }

    /**
     * Add a menu item to the sidebar
     */
    private void addMenuItem(String text, String panelName, boolean isActive) {
        NavItem item = new NavItem(text, panelName);
        if (isActive) {
            item.setActive(true);
            activeItem = item;
        }

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setActiveItem(item);
                if (navigationListener != null) {
                    navigationListener.onNavigate(panelName);
                }
            }
        });

        menuPanel.add(item);
    }

    /**
     * Set the active navigation item
     */
    private void setActiveItem(NavItem item) {
        if (activeItem != null) {
            activeItem.setActive(false);
        }
        item.setActive(true);
        activeItem = item;
    }

    /**
     * Set navigation listener
     */
    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }

    /**
     * Navigate to a specific panel programmatically
     */
    public void navigateTo(String panelName) {
        for (Component comp : menuPanel.getComponents()) {
            if (comp instanceof NavItem) {
                NavItem item = (NavItem) comp;
                if (item.getPanelName().equals(panelName)) {
                    setActiveItem(item);
                    break;
                }
            }
        }
    }

    /**
     * Inner class for navigation items
     */
    private class NavItem extends JPanel {
        private String text;
        private String panelName;
        private boolean isActive = false;
        private boolean isHovered = false;
        private JLabel textLabel;

        public NavItem(String text, String panelName) {
            this.text = text;
            this.panelName = panelName;
            initializeUI();
        }

        private void initializeUI() {
            setLayout(new BorderLayout());
            setBackground(SIDEBAR_BG);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(12, 25, 12, 20));

            // Icon placeholder (using bullet point)
            JLabel iconLabel = new JLabel(getIcon());
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            iconLabel.setForeground(TEXT_MUTED);
            iconLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
            add(iconLabel, BorderLayout.WEST);

            // Text
            textLabel = new JLabel(text);
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textLabel.setForeground(TEXT_COLOR);
            add(textLabel, BorderLayout.CENTER);

            // Hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    updateBackground();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    updateBackground();
                }
            });
        }

        private String getIcon() {
            switch (panelName) {
                case "dashboard":
                    return "\u2302"; // Home
                case "patients":
                    return "\u263A"; // Person
                case "doctors":
                    return "\u2695"; // Medical
                case "appointments":
                    return "\u2637"; // Calendar
                case "rooms":
                    return "\u2302"; // Building
                case "billing":
                    return "\u20B9"; // Currency
                default:
                    return "\u2022"; // Bullet
            }
        }

        public void setActive(boolean active) {
            this.isActive = active;
            updateBackground();
            textLabel.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 14));
        }

        private void updateBackground() {
            if (isActive) {
                setBackground(ITEM_ACTIVE);
            } else if (isHovered) {
                setBackground(ITEM_HOVER);
            } else {
                setBackground(SIDEBAR_BG);
            }
            repaint();
        }

        public String getPanelName() {
            return panelName;
        }
    }
}
