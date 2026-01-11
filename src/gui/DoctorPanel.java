package gui;

import gui.components.*;
import managers.HospitalManager;
import managers.DoctorManager;
import managers.UserManager;
import models.Doctor;
import models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Doctor management panel.
 * Allows adding, editing, viewing, and managing doctors.
 */
public class DoctorPanel extends JPanel {

    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    private DoctorManager doctorManager;
    private StyledTable doctorTable;
    private DefaultTableModel tableModel;
    private StyledTextField searchField;

    public DoctorPanel() {
        this.doctorManager = HospitalManager.getInstance().getDoctorManager();
        initializeUI();
        loadDoctorData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        header.setBorder(new EmptyBorder(0, 0, 25, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(BACKGROUND);

        JLabel titleLabel = new JLabel("Doctors");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        leftPanel.add(titleLabel);

        JLabel countLabel = new JLabel("  (" + doctorManager.getTotalCount() + " total)");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        countLabel.setForeground(TEXT_SECONDARY);
        leftPanel.add(countLabel);

        header.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(BACKGROUND);

        searchField = new StyledTextField("Search doctors...");
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.addActionListener(e -> searchDoctors());
        rightPanel.add(searchField);

        StyledButton addButton = new StyledButton("+ Add Doctor");
        addButton.setPreferredSize(new Dimension(140, 40));
        addButton.addActionListener(e -> showAddDoctorDialog());
        rightPanel.add(addButton);

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columns = { "Doctor ID", "Name", "Specialization", "Department", "Experience", "Fee (₹)", "Phone" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new StyledTable(tableModel);
        doctorTable.setColumnWidths(100, 150, 130, 130, 90, 80, 120);

        JScrollPane scrollPane = doctorTable.createScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        actionsPanel.setBackground(BACKGROUND);

        StyledButton viewBtn = StyledButton.secondary("View Profile");
        viewBtn.addActionListener(e -> viewSelectedDoctor());
        actionsPanel.add(viewBtn);

        StyledButton editBtn = new StyledButton("Edit", new Color(245, 158, 11), new Color(217, 119, 6));
        editBtn.addActionListener(e -> editSelectedDoctor());
        actionsPanel.add(editBtn);

        StyledButton deleteBtn = StyledButton.danger("Delete");
        deleteBtn.addActionListener(e -> deleteSelectedDoctor());
        actionsPanel.add(deleteBtn);

        StyledButton refreshBtn = StyledButton.secondary("Refresh");
        refreshBtn.addActionListener(e -> loadDoctorData());
        actionsPanel.add(refreshBtn);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadDoctorData() {
        tableModel.setRowCount(0);
        List<Doctor> doctors = doctorManager.getAllDoctors();

        for (Doctor doctor : doctors) {
            Object[] row = {
                    doctor.getDoctorId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getDepartment(),
                    doctor.getExperienceYears() + " yrs",
                    String.format("%.0f", doctor.getConsultationFee()),
                    doctor.getPhone()
            };
            tableModel.addRow(row);
        }
    }

    private void searchDoctors() {
        String query = searchField.getActualText();
        if (query.isEmpty()) {
            loadDoctorData();
            return;
        }

        tableModel.setRowCount(0);
        List<Doctor> doctors = doctorManager.searchByName(query);

        for (Doctor doctor : doctors) {
            Object[] row = {
                    doctor.getDoctorId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getDepartment(),
                    doctor.getExperienceYears() + " yrs",
                    String.format("%.0f", doctor.getConsultationFee()),
                    doctor.getPhone()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDoctorDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Doctor", true);
        dialog.setSize(500, 650);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Add New Doctor");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(25));

        JTextField nameField = createFormField(mainPanel, "Full Name");
        JTextField phoneField = createFormField(mainPanel, "Phone Number");
        JTextField emailField = createFormField(mainPanel, "Email Address");
        JTextField qualificationField = createFormField(mainPanel, "Qualification (e.g., MBBS, MD)");
        JTextField experienceField = createFormField(mainPanel, "Experience (Years)");
        JTextField feeField = createFormField(mainPanel, "Consultation Fee (₹)");

        // Specialization dropdown
        String[] specializations = { "General Medicine", "Cardiology", "Neurology", "Orthopedics",
                "Pediatrics", "Dermatology", "Ophthalmology", "ENT", "Gynecology", "Psychiatry" };
        JComboBox<String> specCombo = createDropdown(mainPanel, "Specialization", specializations);

        // Gender dropdown
        JComboBox<String> genderCombo = createDropdown(mainPanel, "Gender", new String[] { "Male", "Female", "Other" });

        mainPanel.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyledButton cancelBtn = StyledButton.secondary("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);

        StyledButton saveBtn = StyledButton.success("Save Doctor");
        saveBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter doctor name", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Doctor doctor = new Doctor();
            doctor.setName(nameField.getText().trim());
            doctor.setPhone(phoneField.getText().trim());
            doctor.setEmail(emailField.getText().trim());
            doctor.setQualification(qualificationField.getText().trim());
            doctor.setGender((String) genderCombo.getSelectedItem());
            doctor.setSpecialization((String) specCombo.getSelectedItem());
            doctor.setDepartment((String) specCombo.getSelectedItem());
            doctor.setAvailableDays(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
            doctor.setWorkingHours("9:00 AM - 5:00 PM");

            try {
                doctor.setExperienceYears(Integer.parseInt(experienceField.getText().trim()));
            } catch (NumberFormatException ex) {
                doctor.setExperienceYears(0);
            }

            try {
                doctor.setConsultationFee(Double.parseDouble(feeField.getText().trim()));
            } catch (NumberFormatException ex) {
                doctor.setConsultationFee(500);
            }

            doctorManager.addDoctor(doctor);

            // Create user account for the doctor
            String username = "dr." + nameField.getText().trim().toLowerCase()
                    .replaceAll("\\s+", ".").replaceAll("[^a-z.]", "");
            String password = "doc" + doctor.getDoctorId().replace("DOC-", "");

            UserManager userManager = new UserManager();
            User doctorUser = userManager.createDoctorUser(username, password,
                    "Dr. " + doctor.getName(), doctor.getDoctorId());

            dialog.dispose();
            loadDoctorData();

            // Show credentials to admin
            String message = "Doctor added successfully!\n\n" +
                    "Login Credentials Created:\n" +
                    "Username: " + username + "\n" +
                    "Password: " + password + "\n\n" +
                    "Please share these credentials with the doctor.";
            JOptionPane.showMessageDialog(this, message, "Success",
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

    private JComboBox<String> createDropdown(JPanel parent, String label, String[] options) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComp.setForeground(TEXT_SECONDARY);
        fieldPanel.add(labelComp, BorderLayout.NORTH);

        JComboBox<String> combo = new JComboBox<>(options);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fieldPanel.add(combo, BorderLayout.CENTER);

        parent.add(fieldPanel);
        parent.add(Box.createVerticalStrut(15));

        return combo;
    }

    private void viewSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to view", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doctorId = (String) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.getDoctorById(doctorId);

        if (doctor != null) {
            StringBuilder info = new StringBuilder();
            info.append("Doctor ID: ").append(doctor.getDoctorId()).append("\n");
            info.append("Name: ").append(doctor.getName()).append("\n");
            info.append("Specialization: ").append(doctor.getSpecialization()).append("\n");
            info.append("Department: ").append(doctor.getDepartment()).append("\n");
            info.append("Qualification: ").append(doctor.getQualification()).append("\n");
            info.append("Experience: ").append(doctor.getExperienceYears()).append(" years\n");
            info.append("Consultation Fee: ₹").append(String.format("%.0f", doctor.getConsultationFee())).append("\n");
            info.append("Phone: ").append(doctor.getPhone()).append("\n");
            info.append("Email: ").append(doctor.getEmail()).append("\n");
            info.append("Working Hours: ").append(doctor.getWorkingHours()).append("\n");
            info.append("Patients: ").append(doctor.getPatientCount()).append("\n");

            JOptionPane.showMessageDialog(this, info.toString(), "Doctor Profile", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to edit", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doctorId = (String) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.getDoctorById(doctorId);

        if (doctor != null) {
            String newFee = JOptionPane.showInputDialog(this, "Enter new consultation fee:",
                    doctor.getConsultationFee());
            if (newFee != null) {
                try {
                    doctor.setConsultationFee(Double.parseDouble(newFee));
                    doctorManager.updateDoctor(doctor);
                    loadDoctorData();
                    JOptionPane.showMessageDialog(this, "Doctor updated successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid fee amount", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doctorId = (String) tableModel.getValueAt(selectedRow, 0);
        String doctorName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete doctor: " + doctorName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            doctorManager.deleteDoctor(doctorId);
            loadDoctorData();
            JOptionPane.showMessageDialog(this, "Doctor deleted successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refresh() {
        loadDoctorData();
    }
}
