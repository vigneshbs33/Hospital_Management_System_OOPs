package gui;

import gui.components.*;
import managers.HospitalManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Dashboard panel showing hospital statistics and quick actions.
 * Features stat cards, recent activities, and quick action buttons.
 */
public class DashboardPanel extends JPanel {

    // Color constants
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    private HospitalManager hospitalManager;
    private JLabel patientsLabel;
    private JLabel doctorsLabel;
    private JLabel appointmentsLabel;
    private JLabel roomsLabel;
    private JLabel revenueLabel;
    private JLabel occupancyLabel;

    /**
     * Create dashboard panel
     */
    public DashboardPanel() {
        this.hospitalManager = HospitalManager.getInstance();
        initializeUI();
    }

    /**
     * Initialize the dashboard UI
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND);

        // Stats grid
        JPanel statsPanel = createStatsGrid();
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(25));

        // Quick actions
        JPanel actionsPanel = createQuickActions();
        contentPanel.add(actionsPanel);
        contentPanel.add(Box.createVerticalStrut(25));

        // Recent activities
        JPanel activitiesPanel = createRecentActivities();
        contentPanel.add(activitiesPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getViewport().setBackground(BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Create header with title and date
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        header.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Title
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);

        // Date/Time
        JLabel dateLabel = new JLabel(utils.DateUtils.getCurrentDateString());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(TEXT_SECONDARY);
        header.add(dateLabel, BorderLayout.EAST);

        return header;
    }

    /**
     * Create statistics grid
     */
    private JPanel createStatsGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setBackground(BACKGROUND);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        // Stat cards with initial values
        patientsLabel = new JLabel("0");
        doctorsLabel = new JLabel("0");
        appointmentsLabel = new JLabel("0");
        roomsLabel = new JLabel("0");
        revenueLabel = new JLabel("₹0");
        occupancyLabel = new JLabel("0%");

        grid.add(createStatCard("Total Patients", patientsLabel, new Color(59, 130, 246), "\u263A"));
        grid.add(createStatCard("Doctors", doctorsLabel, new Color(16, 185, 129), "\u2695"));
        grid.add(createStatCard("Today's Appointments", appointmentsLabel, new Color(139, 92, 246), "\u2637"));
        grid.add(createStatCard("Available Rooms", roomsLabel, new Color(245, 158, 11), "\u2302"));
        grid.add(createStatCard("Today's Revenue", revenueLabel, new Color(34, 197, 94), "₹"));
        grid.add(createStatCard("Room Occupancy", occupancyLabel, new Color(239, 68, 68), "%"));

        // Update values
        refreshStats();

        return grid;
    }

    /**
     * Create a statistics card
     */
    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor, String icon) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                // Left accent bar
                g2.setColor(accentColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, 4, getHeight(), 4, 4));

                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 25, 20, 20));

        // Content panel
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        iconLabel.setForeground(accentColor);
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(iconLabel);
        content.add(Box.createVerticalStrut(10));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(5));

        // Value
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(valueLabel);

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    /**
     * Create quick actions panel
     */
    private JPanel createQuickActions() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Title
        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonsPanel.setBackground(BACKGROUND);

        StyledButton addPatientBtn = new StyledButton("+ Add Patient");
        addPatientBtn.setPreferredSize(new Dimension(150, 45));

        StyledButton addAppointmentBtn = new StyledButton("+ New Appointment",
                new Color(16, 185, 129), new Color(5, 150, 105));
        addAppointmentBtn.setPreferredSize(new Dimension(180, 45));

        StyledButton generateBillBtn = new StyledButton("Generate Bill",
                new Color(139, 92, 246), new Color(124, 58, 237));
        generateBillBtn.setPreferredSize(new Dimension(150, 45));

        StyledButton viewReportsBtn = StyledButton.secondary("View Reports");
        viewReportsBtn.setPreferredSize(new Dimension(140, 45));

        buttonsPanel.add(addPatientBtn);
        buttonsPanel.add(addAppointmentBtn);
        buttonsPanel.add(generateBillBtn);
        buttonsPanel.add(viewReportsBtn);

        panel.add(buttonsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Create recent activities panel
     */
    private JPanel createRecentActivities() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Title
        JLabel titleLabel = new JLabel("System Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Overview items
        content.add(createOverviewItem("Hospital Name", hospitalManager.getHospitalName()));
        content.add(Box.createVerticalStrut(10));
        content.add(createOverviewItem("Address", hospitalManager.getHospitalAddress()));
        content.add(Box.createVerticalStrut(10));
        content.add(createOverviewItem("Contact", hospitalManager.getHospitalPhone()));
        content.add(Box.createVerticalStrut(10));
        content.add(createOverviewItem("Email", hospitalManager.getHospitalEmail()));
        content.add(Box.createVerticalStrut(15));

        // Department count
        int deptCount = hospitalManager.getDoctorManager().getAllDepartments().size();
        content.add(createOverviewItem("Active Departments", String.valueOf(deptCount)));
        content.add(Box.createVerticalStrut(10));

        // Pending bills
        int pendingBills = hospitalManager.getBillingManager().getPendingBills().size();
        content.add(createOverviewItem("Pending Bills", String.valueOf(pendingBills)));

        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Create an overview item row
     */
    private JPanel createOverviewItem(String label, String value) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComp.setForeground(TEXT_SECONDARY);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueComp.setForeground(TEXT_PRIMARY);

        item.add(labelComp, BorderLayout.WEST);
        item.add(valueComp, BorderLayout.EAST);

        return item;
    }

    /**
     * Refresh statistics display
     */
    public void refreshStats() {
        patientsLabel.setText(String.valueOf(hospitalManager.getTotalPatients()));
        doctorsLabel.setText(String.valueOf(hospitalManager.getTotalDoctors()));
        appointmentsLabel.setText(String.valueOf(hospitalManager.getTodaysAppointments()));
        roomsLabel.setText(String.valueOf(hospitalManager.getAvailableRooms()));
        revenueLabel.setText("₹" + String.format("%.0f", hospitalManager.getTodaysRevenue()));
        occupancyLabel.setText(String.format("%.0f%%", hospitalManager.getRoomOccupancyRate()));
    }
}
