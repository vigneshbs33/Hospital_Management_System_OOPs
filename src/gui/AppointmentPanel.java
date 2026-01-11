package gui;

import gui.components.*;
import managers.HospitalManager;
import managers.AppointmentManager;
import managers.DoctorManager;
import managers.PatientManager;
import models.Appointment;
import models.Doctor;
import models.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Appointment management panel.
 * Allows scheduling, viewing, and managing appointments.
 * For doctors: shows only their appointments.
 * For admin/receptionist: shows all appointments.
 */
public class AppointmentPanel extends JPanel {

    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    private AppointmentManager appointmentManager;
    private DoctorManager doctorManager;
    private PatientManager patientManager;
    private StyledTable appointmentTable;
    private DefaultTableModel tableModel;

    // For filtering appointments by doctor (when logged in as doctor)
    private String currentDoctorId = null;

    public AppointmentPanel() {
        HospitalManager hm = HospitalManager.getInstance();
        this.appointmentManager = hm.getAppointmentManager();
        this.doctorManager = hm.getDoctorManager();
        this.patientManager = hm.getPatientManager();
        initializeUI();
        loadAppointmentData();
    }

    /**
     * Set the current doctor ID for filtering (used when doctor logs in)
     */
    public void setCurrentDoctorId(String doctorId) {
        this.currentDoctorId = doctorId;
        loadAppointmentData();
    }

    /**
     * Clear the doctor filter (for admin/receptionist)
     */
    public void clearDoctorFilter() {
        this.currentDoctorId = null;
        loadAppointmentData();
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

        JLabel titleLabel = new JLabel("Appointments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        leftPanel.add(titleLabel);

        header.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(BACKGROUND);

        StyledButton todayBtn = StyledButton.secondary("Today's");
        todayBtn.addActionListener(e -> loadTodaysAppointments());
        rightPanel.add(todayBtn);

        StyledButton allBtn = StyledButton.secondary("All");
        allBtn.addActionListener(e -> loadAppointmentData());
        rightPanel.add(allBtn);

        StyledButton addButton = new StyledButton("+ New Appointment");
        addButton.setPreferredSize(new Dimension(180, 40));
        addButton.addActionListener(e -> showAddAppointmentDialog());
        rightPanel.add(addButton);

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columns = { "ID", "Patient", "Doctor", "Date", "Time", "Purpose", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentTable = new StyledTable(tableModel);
        appointmentTable.setColumnWidths(150, 150, 150, 100, 80, 150, 100);

        JScrollPane scrollPane = appointmentTable.createScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        actionsPanel.setBackground(BACKGROUND);

        StyledButton completeBtn = StyledButton.success("Complete");
        completeBtn.addActionListener(e -> completeSelectedAppointment());
        actionsPanel.add(completeBtn);

        StyledButton cancelBtn = StyledButton.danger("Cancel");
        cancelBtn.addActionListener(e -> cancelSelectedAppointment());
        actionsPanel.add(cancelBtn);

        StyledButton deleteBtn = StyledButton.secondary("Delete");
        deleteBtn.addActionListener(e -> deleteSelectedAppointment());
        actionsPanel.add(deleteBtn);

        StyledButton refreshBtn = StyledButton.secondary("Refresh");
        refreshBtn.addActionListener(e -> loadAppointmentData());
        actionsPanel.add(refreshBtn);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadAppointmentData() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = appointmentManager.getAllAppointments();

        for (Appointment apt : appointments) {
            // Filter by doctor if currentDoctorId is set
            if (currentDoctorId != null && !apt.getDoctorId().equals(currentDoctorId)) {
                continue;
            }

            Object[] row = {
                    apt.getAppointmentId(),
                    apt.getPatientName(),
                    apt.getDoctorName(),
                    apt.getFormattedDate(),
                    apt.getFormattedTime(),
                    apt.getPurpose(),
                    apt.getStatus().getDisplayName()
            };
            tableModel.addRow(row);
        }
    }

    private void loadTodaysAppointments() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = appointmentManager.getTodaysAppointments();

        for (Appointment apt : appointments) {
            // Filter by doctor if currentDoctorId is set
            if (currentDoctorId != null && !apt.getDoctorId().equals(currentDoctorId)) {
                continue;
            }

            Object[] row = {
                    apt.getAppointmentId(),
                    apt.getPatientName(),
                    apt.getDoctorName(),
                    apt.getFormattedDate(),
                    apt.getFormattedTime(),
                    apt.getPurpose(),
                    apt.getStatus().getDisplayName()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddAppointmentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Schedule New Appointment", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Schedule New Appointment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(25));

        // Patient dropdown
        List<Patient> patients = patientManager.getAllPatients();
        String[] patientNames = patients.stream().map(p -> p.getPatientId() + " - " + p.getName())
                .toArray(String[]::new);
        JComboBox<String> patientCombo = createDropdown(mainPanel, "Select Patient",
                patientNames.length > 0 ? patientNames : new String[] { "No patients available" });

        // Doctor dropdown
        List<Doctor> doctors = doctorManager.getAllDoctors();
        String[] doctorNames = doctors.stream()
                .map(d -> d.getDoctorId() + " - " + d.getName() + " (" + d.getSpecialization() + ")")
                .toArray(String[]::new);
        JComboBox<String> doctorCombo = createDropdown(mainPanel, "Select Doctor",
                doctorNames.length > 0 ? doctorNames : new String[] { "No doctors available" });

        // Date field
        JTextField dateField = createFormField(mainPanel, "Date (DD/MM/YYYY)");
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Time dropdown
        String[] times = { "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
                "12:00 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM" };
        JComboBox<String> timeCombo = createDropdown(mainPanel, "Select Time", times);

        // Purpose field
        JTextField purposeField = createFormField(mainPanel, "Purpose/Reason");

        mainPanel.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyledButton cancelBtn = StyledButton.secondary("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);

        StyledButton saveBtn = StyledButton.success("Schedule");
        saveBtn.addActionListener(e -> {
            if (patients.isEmpty() || doctors.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please add patients and doctors first", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int patientIndex = patientCombo.getSelectedIndex();
            int doctorIndex = doctorCombo.getSelectedIndex();

            if (patientIndex < 0 || doctorIndex < 0) {
                JOptionPane.showMessageDialog(dialog, "Please select patient and doctor", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient patient = patients.get(patientIndex);
            Doctor doctor = doctors.get(doctorIndex);

            // Parse date and time
            LocalDate date;
            try {
                date = LocalDate.parse(dateField.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String timeStr = (String) timeCombo.getSelectedItem();
            LocalTime time;
            try {
                time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("hh:mm a"));
            } catch (Exception ex) {
                time = LocalTime.of(9, 0);
            }

            LocalDateTime dateTime = LocalDateTime.of(date, time);

            Appointment appointment = new Appointment();
            appointment.setPatientId(patient.getPatientId());
            appointment.setPatientName(patient.getName());
            appointment.setDoctorId(doctor.getDoctorId());
            appointment.setDoctorName(doctor.getName());
            appointment.setDateTime(dateTime);
            appointment.setPurpose(purposeField.getText().trim());

            appointmentManager.scheduleAppointment(appointment);
            dialog.dispose();
            loadAppointmentData();
            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!", "Success",
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

    private void completeSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appointmentId = (String) tableModel.getValueAt(selectedRow, 0);
        appointmentManager.completeAppointment(appointmentId);
        loadAppointmentData();
        JOptionPane.showMessageDialog(this, "Appointment marked as completed!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appointmentId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this appointment?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            appointmentManager.cancelAppointment(appointmentId);
            loadAppointmentData();
            JOptionPane.showMessageDialog(this, "Appointment cancelled!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appointmentId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            appointmentManager.deleteAppointment(appointmentId);
            loadAppointmentData();
            JOptionPane.showMessageDialog(this, "Appointment deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refresh() {
        loadAppointmentData();
    }
}
