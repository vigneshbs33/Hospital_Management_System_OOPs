package gui;

import gui.components.*;
import managers.HospitalManager;
import managers.BillingManager;
import managers.PatientManager;
import models.Bill;
import models.Bill.BillItem;
import models.Bill.BillStatus;
import models.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Billing management panel.
 * Allows creating bills, processing payments, and viewing billing history.
 */
public class BillingPanel extends JPanel {

    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    private BillingManager billingManager;
    private PatientManager patientManager;
    private StyledTable billsTable;
    private DefaultTableModel tableModel;

    public BillingPanel() {
        HospitalManager hm = HospitalManager.getInstance();
        this.billingManager = hm.getBillingManager();
        this.patientManager = hm.getPatientManager();
        initializeUI();
        loadBillsData();
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

        JLabel titleLabel = new JLabel("Billing");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        leftPanel.add(titleLabel);

        header.add(leftPanel, BorderLayout.WEST);

        // Stats and actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(BACKGROUND);

        // Revenue display
        JLabel revenueLabel = new JLabel("Total Revenue: ₹" + String.format("%.0f", billingManager.getTotalRevenue()));
        revenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        revenueLabel.setForeground(new Color(34, 197, 94));
        rightPanel.add(revenueLabel);

        JLabel pendingLabel = new JLabel("  |  Pending: ₹" + String.format("%.0f", billingManager.getPendingAmount()));
        pendingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pendingLabel.setForeground(new Color(239, 68, 68));
        rightPanel.add(pendingLabel);

        rightPanel.add(Box.createHorizontalStrut(20));

        StyledButton newBillBtn = new StyledButton("+ New Bill");
        newBillBtn.setPreferredSize(new Dimension(130, 40));
        newBillBtn.addActionListener(e -> showCreateBillDialog());
        rightPanel.add(newBillBtn);

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columns = { "Bill ID", "Patient", "Total (₹)", "Paid (₹)", "Balance (₹)", "Status", "Date" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        billsTable = new StyledTable(tableModel);
        billsTable.setColumnWidths(150, 150, 100, 100, 100, 100, 120);

        JScrollPane scrollPane = billsTable.createScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        actionsPanel.setBackground(BACKGROUND);

        StyledButton viewBtn = StyledButton.secondary("View Details");
        viewBtn.addActionListener(e -> viewSelectedBill());
        actionsPanel.add(viewBtn);

        StyledButton payBtn = StyledButton.success("Process Payment");
        payBtn.addActionListener(e -> processPayment());
        actionsPanel.add(payBtn);

        StyledButton addItemBtn = new StyledButton("Add Item", new Color(139, 92, 246), new Color(124, 58, 237));
        addItemBtn.addActionListener(e -> addItemToBill());
        actionsPanel.add(addItemBtn);

        StyledButton cancelBtn = StyledButton.danger("Cancel Bill");
        cancelBtn.addActionListener(e -> cancelSelectedBill());
        actionsPanel.add(cancelBtn);

        StyledButton refreshBtn = StyledButton.secondary("Refresh");
        refreshBtn.addActionListener(e -> loadBillsData());
        actionsPanel.add(refreshBtn);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadBillsData() {
        tableModel.setRowCount(0);
        List<Bill> bills = billingManager.getAllBills();

        for (Bill bill : bills) {
            Object[] row = {
                    bill.getBillId(),
                    bill.getPatientName(),
                    String.format("%.2f", bill.getTotalAmount()),
                    String.format("%.2f", bill.getPaidAmount()),
                    String.format("%.2f", bill.getBalance()),
                    bill.getStatus().getDisplayName(),
                    bill.getFormattedDateGenerated()
            };
            tableModel.addRow(row);
        }
    }

    private void showCreateBillDialog() {
        List<Patient> patients = patientManager.getAllPatients();
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients available. Please add patients first.",
                    "No Patients", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] patientNames = patients.stream()
                .map(p -> p.getPatientId() + " - " + p.getName())
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(this, "Select Patient:",
                "Create New Bill", JOptionPane.PLAIN_MESSAGE, null, patientNames, patientNames[0]);

        if (selected != null) {
            int index = java.util.Arrays.asList(patientNames).indexOf(selected);
            Patient patient = patients.get(index);

            Bill bill = billingManager.createBill(patient.getPatientId(), patient.getName());
            loadBillsData();
            JOptionPane.showMessageDialog(this, "Bill created: " + bill.getBillId(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewSelectedBill() {
        int selectedRow = billsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bill", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String billId = (String) tableModel.getValueAt(selectedRow, 0);
        Bill bill = billingManager.getBillById(billId);

        if (bill != null) {
            StringBuilder info = new StringBuilder();
            info.append("Bill ID: ").append(bill.getBillId()).append("\n");
            info.append("Patient: ").append(bill.getPatientName()).append("\n");
            info.append("Date: ").append(bill.getFormattedDateGenerated()).append("\n");
            info.append("Status: ").append(bill.getStatus().getDisplayName()).append("\n\n");

            info.append("Items:\n");
            info.append("-".repeat(40)).append("\n");

            List<BillItem> items = bill.getItems();
            if (items != null && !items.isEmpty()) {
                for (BillItem item : items) {
                    info.append(String.format("%-20s x%d = ₹%.2f\n",
                            item.getDescription(), item.getQuantity(), item.getAmount()));
                }
            } else {
                info.append("No items\n");
            }

            info.append("-".repeat(40)).append("\n");
            info.append(String.format("Discount: ₹%.2f\n", bill.getDiscount()));
            info.append(String.format("Total: ₹%.2f\n", bill.getTotalAmount()));
            info.append(String.format("Paid: ₹%.2f\n", bill.getPaidAmount()));
            info.append(String.format("Balance: ₹%.2f\n", bill.getBalance()));

            JTextArea textArea = new JTextArea(info.toString());
            textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 350));

            JOptionPane.showMessageDialog(this, scrollPane, "Bill Details", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void addItemToBill() {
        int selectedRow = billsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bill", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String billId = (String) tableModel.getValueAt(selectedRow, 0);
        Bill bill = billingManager.getBillById(billId);

        if (bill != null && bill.getStatus() == BillStatus.PAID) {
            JOptionPane.showMessageDialog(this, "Cannot add items to a paid bill", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Item categories
        String[] categories = { "Consultation", "Medicine", "Lab Test", "Procedure", "Room Charge", "Other" };

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Category:"));
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        panel.add(categoryCombo);

        panel.add(new JLabel("Description:"));
        JTextField descField = new JTextField();
        panel.add(descField);

        panel.add(new JLabel("Quantity:"));
        JTextField qtyField = new JTextField("1");
        panel.add(qtyField);

        panel.add(new JLabel("Unit Price (₹):"));
        JTextField priceField = new JTextField();
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Item to Bill",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String desc = descField.getText().trim();
                String category = (String) categoryCombo.getSelectedItem();
                int qty = Integer.parseInt(qtyField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                if (desc.isEmpty()) {
                    desc = category;
                }

                BillItem item = new BillItem(desc, category, qty, price);
                billingManager.addItemToBill(billId, item);
                loadBillsData();
                JOptionPane.showMessageDialog(this, "Item added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity or price", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void processPayment() {
        int selectedRow = billsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bill", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String billId = (String) tableModel.getValueAt(selectedRow, 0);
        Bill bill = billingManager.getBillById(billId);

        if (bill == null)
            return;

        if (bill.getStatus() == BillStatus.PAID) {
            JOptionPane.showMessageDialog(this, "Bill is already fully paid", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double balance = bill.getBalance();

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Balance Due:"));
        panel.add(new JLabel("₹" + String.format("%.2f", balance)));

        panel.add(new JLabel("Payment Amount (₹):"));
        JTextField amountField = new JTextField(String.format("%.2f", balance));
        panel.add(amountField);

        panel.add(new JLabel("Payment Method:"));
        JComboBox<String> methodCombo = new JComboBox<>(new String[] { "Cash", "Card", "UPI", "Insurance" });
        panel.add(methodCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Process Payment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                String method = (String) methodCombo.getSelectedItem();

                billingManager.processPayment(billId, amount, method);
                loadBillsData();
                JOptionPane.showMessageDialog(this, "Payment processed successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cancelSelectedBill() {
        int selectedRow = billsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bill", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String billId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this bill?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            billingManager.cancelBill(billId);
            loadBillsData();
            JOptionPane.showMessageDialog(this, "Bill cancelled!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refresh() {
        loadBillsData();
    }
}
