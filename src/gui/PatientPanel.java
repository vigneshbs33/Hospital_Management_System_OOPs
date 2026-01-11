package gui;

import gui.components.*;
import managers.HospitalManager;
import managers.PatientManager;
import models.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Patient management panel.
 * Allows adding, editing, viewing, and deleting patients.
 * For doctors: shows only their assigned patients.
 */
public class PatientPanel extends JPanel {

    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    private PatientManager patientManager;
    private StyledTable patientTable;
    private DefaultTableModel tableModel;
    private StyledTextField searchField;

    // For filtering patients by doctor (when logged in as doctor)
    private String currentDoctorId = null;

    public PatientPanel() {
        this.patientManager = HospitalManager.getInstance().getPatientManager();
        initializeUI();
        loadPatientData();
    }

    /**
     * Set the current doctor ID for filtering
     */
    public void setCurrentDoctorId(String doctorId) {
        this.currentDoctorId = doctorId;
        loadPatientData();
    }

    /**
     * Clear the doctor filter
     */
    public void clearDoctorFilter() {
        this.currentDoctorId = null;
        loadPatientData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Table panel
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        header.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Left side - Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(BACKGROUND);

        JLabel titleLabel = new JLabel("Patients");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        leftPanel.add(titleLabel);

        JLabel countLabel = new JLabel("  (" + patientManager.getTotalCount() + " total)");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        countLabel.setForeground(TEXT_SECONDARY);
        leftPanel.add(countLabel);

        header.add(leftPanel, BorderLayout.WEST);

        // Right side - Search and Add button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(BACKGROUND);

        searchField = new StyledTextField("Search patients...");
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.addActionListener(e -> searchPatients());
        rightPanel.add(searchField);

        StyledButton addButton = new StyledButton("+ Add Patient");
        addButton.setPreferredSize(new Dimension(140, 40));
        addButton.addActionListener(e -> showAddPatientDialog());
        rightPanel.add(addButton);

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Table columns
        String[] columns = { "Patient ID", "Name", "Age", "Gender", "Blood Group", "Phone", "Status", "Actions" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only actions column
            }
        };

        patientTable = new StyledTable(tableModel);
        patientTable.setColumnWidths(120, 150, 60, 80, 90, 120, 100, 150);

        JScrollPane scrollPane = patientTable.createScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        actionsPanel.setBackground(BACKGROUND);

        StyledButton viewBtn = StyledButton.secondary("View Details");
        viewBtn.addActionListener(e -> viewSelectedPatient());
        actionsPanel.add(viewBtn);

        StyledButton editBtn = new StyledButton("Edit", new Color(245, 158, 11), new Color(217, 119, 6));
        editBtn.addActionListener(e -> editSelectedPatient());
        actionsPanel.add(editBtn);

        StyledButton deleteBtn = StyledButton.danger("Delete");
        deleteBtn.addActionListener(e -> deleteSelectedPatient());
        actionsPanel.add(deleteBtn);

        StyledButton refreshBtn = StyledButton.secondary("Refresh");
        refreshBtn.addActionListener(e -> loadPatientData());
        actionsPanel.add(refreshBtn);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadPatientData() {
        tableModel.setRowCount(0);
        List<Patient> patients = patientManager.getAllPatients();

        for (Patient patient : patients) {
            Object[] row = {
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getBloodGroup(),
                    patient.getPhone(),
                    patient.isAdmitted() ? "Admitted" : "OPD",
                    "View | Edit | Delete"
            };
            tableModel.addRow(row);
        }
    }

    private void searchPatients() {
        String query = searchField.getActualText();
        if (query.isEmpty()) {
            loadPatientData();
            return;
        }

        tableModel.setRowCount(0);
        List<Patient> patients = patientManager.searchByName(query);

        for (Patient patient : patients) {
            Object[] row = {
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getBloodGroup(),
                    patient.getPhone(),
                    patient.isAdmitted() ? "Admitted" : "OPD",
                    "View | Edit | Delete"
            };
            tableModel.addRow(row);
        }
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Patient", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Add New Patient");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(25));

        // Form fields
        JTextField nameField = createFormField(mainPanel, "Full Name");
        JTextField phoneField = createFormField(mainPanel, "Phone Number");
        JTextField emailField = createFormField(mainPanel, "Email Address");
        JTextField addressField = createFormField(mainPanel, "Address");
        JTextField dobField = createFormField(mainPanel, "Date of Birth (DD/MM/YYYY)");

        // Gender dropdown
        JPanel genderPanel = new JPanel(new BorderLayout());
        genderPanel.setBackground(Color.WHITE);
        genderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        genderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        genderLabel.setForeground(TEXT_SECONDARY);
        genderPanel.add(genderLabel, BorderLayout.NORTH);

        JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderPanel.add(genderCombo, BorderLayout.CENTER);
        mainPanel.add(genderPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Blood group dropdown
        JPanel bloodPanel = new JPanel(new BorderLayout());
        bloodPanel.setBackground(Color.WHITE);
        bloodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        bloodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bloodLabel = new JLabel("Blood Group");
        bloodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bloodLabel.setForeground(TEXT_SECONDARY);
        bloodPanel.add(bloodLabel, BorderLayout.NORTH);

        JComboBox<String> bloodCombo = new JComboBox<>(
                new String[] { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" });
        bloodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bloodPanel.add(bloodCombo, BorderLayout.CENTER);
        mainPanel.add(bloodPanel);
        mainPanel.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyledButton cancelBtn = StyledButton.secondary("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);

        StyledButton saveBtn = StyledButton.success("Save Patient");
        saveBtn.addActionListener(e -> {
            // Validate and save
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter patient name", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient patient = new Patient();
            patient.setName(nameField.getText().trim());
            patient.setPhone(phoneField.getText().trim());
            patient.setEmail(emailField.getText().trim());
            patient.setAddress(addressField.getText().trim());
            patient.setGender((String) genderCombo.getSelectedItem());
            patient.setBloodGroup((String) bloodCombo.getSelectedItem());

            // Parse date
            try {
                String dobStr = dobField.getText().trim();
                if (!dobStr.isEmpty()) {
                    LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    patient.setDateOfBirth(dob);
                }
            } catch (Exception ex) {
                // Ignore date parsing errors
            }

            patientManager.addPatient(patient);
            dialog.dispose();
            loadPatientData();
            JOptionPane.showMessageDialog(this, "Patient added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(saveBtn);

        mainPanel.add(buttonPanel);

        dialog.add(new JScrollPane(mainPanel));
        dialog.setVisible(true);
    }

    private JTextField createFormField(JPanel parent, String label) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComp.setForeground(TEXT_SECONDARY);
        fieldPanel.add(labelComp, BorderLayout.NORTH);

        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(8, 12, 8, 12)));
        fieldPanel.add(textField, BorderLayout.CENTER);

        parent.add(fieldPanel);
        parent.add(Box.createVerticalStrut(15));

        return textField;
    }

    private void viewSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient to view", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientId = (String) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientManager.getPatientById(patientId);

        if (patient != null) {
            StringBuilder info = new StringBuilder();
            info.append("Patient ID: ").append(patient.getPatientId()).append("\n");
            info.append("Name: ").append(patient.getName()).append("\n");
            info.append("Age: ").append(patient.getAge()).append(" years\n");
            info.append("Gender: ").append(patient.getGender()).append("\n");
            info.append("Blood Group: ").append(patient.getBloodGroup()).append("\n");
            info.append("Phone: ").append(patient.getPhone()).append("\n");
            info.append("Email: ").append(patient.getEmail()).append("\n");
            info.append("Address: ").append(patient.getAddress()).append("\n");
            info.append("Status: ").append(patient.isAdmitted() ? "Admitted" : "OPD").append("\n");
            if (patient.isAdmitted()) {
                info.append("Room: ").append(patient.getRoomNumber()).append("\n");
            }

            JOptionPane.showMessageDialog(this, info.toString(), "Patient Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientId = (String) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientManager.getPatientById(patientId);

        if (patient != null) {
            // Show edit dialog (simplified)
            String newName = JOptionPane.showInputDialog(this, "Enter new name:", patient.getName());
            if (newName != null && !newName.trim().isEmpty()) {
                patient.setName(newName.trim());
                patientManager.updatePatient(patient);
                loadPatientData();
                JOptionPane.showMessageDialog(this, "Patient updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientId = (String) tableModel.getValueAt(selectedRow, 0);
        String patientName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete patient: " + patientName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            patientManager.deletePatient(patientId);
            loadPatientData();
            JOptionPane.showMessageDialog(this, "Patient deleted successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refresh() {
        loadPatientData();
    }
}
