package managers;

import models.Bill;
import models.Bill.BillItem;
import models.Bill.BillStatus;
import utils.FileHandler;
import utils.IDGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager class for Billing operations.
 * Handles bill generation, payment processing, and reporting.
 */
public class BillingManager {

    private List<Bill> bills;

    /**
     * Constructor - loads bills from file
     */
    public BillingManager() {
        this.bills = FileHandler.loadBills();
        if (this.bills == null) {
            this.bills = new ArrayList<>();
        }
    }

    /**
     * Create a new bill
     * 
     * @param patientId   Patient ID
     * @param patientName Patient name
     * @return Generated bill
     */
    public Bill createBill(String patientId, String patientName) {
        String billId = IDGenerator.generateBillId();
        Bill bill = new Bill(billId, patientId, patientName);
        bills.add(bill);
        saveData();
        return bill;
    }

    /**
     * Add item to a bill
     * 
     * @param billId Bill ID
     * @param item   Bill item to add
     * @return true if successful
     */
    public boolean addItemToBill(String billId, BillItem item) {
        Bill bill = getBillById(billId);
        if (bill != null) {
            bill.addItem(item);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Process payment for a bill
     * 
     * @param billId        Bill ID
     * @param amount        Amount to pay
     * @param paymentMethod Payment method
     * @return true if successful
     */
    public boolean processPayment(String billId, double amount, String paymentMethod) {
        Bill bill = getBillById(billId);
        if (bill != null) {
            bill.processPayment(amount, paymentMethod);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Apply discount to a bill
     * 
     * @param billId   Bill ID
     * @param discount Discount amount
     * @return true if successful
     */
    public boolean applyDiscount(String billId, double discount) {
        Bill bill = getBillById(billId);
        if (bill != null) {
            bill.setDiscount(discount);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Cancel a bill
     * 
     * @param billId Bill ID
     * @return true if successful
     */
    public boolean cancelBill(String billId) {
        Bill bill = getBillById(billId);
        if (bill != null) {
            bill.setStatus(BillStatus.CANCELLED);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Delete a bill
     * 
     * @param billId Bill ID
     * @return true if successful
     */
    public boolean deleteBill(String billId) {
        boolean removed = bills.removeIf(b -> b.getBillId().equals(billId));
        if (removed) {
            saveData();
        }
        return removed;
    }

    /**
     * Update a bill
     * 
     * @param bill Bill with updated data
     * @return true if successful
     */
    public boolean updateBill(Bill bill) {
        for (int i = 0; i < bills.size(); i++) {
            if (bills.get(i).getBillId().equals(bill.getBillId())) {
                bills.set(i, bill);
                saveData();
                return true;
            }
        }
        return false;
    }

    /**
     * Get bill by ID
     * 
     * @param billId Bill ID
     * @return Bill or null if not found
     */
    public Bill getBillById(String billId) {
        return bills.stream()
                .filter(b -> b.getBillId().equals(billId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all bills
     * 
     * @return List of all bills
     */
    public List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }

    /**
     * Get bills for a patient
     * 
     * @param patientId Patient ID
     * @return Patient's bills
     */
    public List<Bill> getBillsByPatient(String patientId) {
        return bills.stream()
                .filter(b -> patientId.equals(b.getPatientId()))
                .collect(Collectors.toList());
    }

    /**
     * Get bills by status
     * 
     * @param status Bill status
     * @return Matching bills
     */
    public List<Bill> getBillsByStatus(BillStatus status) {
        return bills.stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Get pending bills
     * 
     * @return List of pending bills
     */
    public List<Bill> getPendingBills() {
        return bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PENDING ||
                        b.getStatus() == BillStatus.PARTIALLY_PAID)
                .collect(Collectors.toList());
    }

    /**
     * Get today's bills
     * 
     * @return List of bills generated today
     */
    public List<Bill> getTodaysBills() {
        LocalDate today = LocalDate.now();
        return bills.stream()
                .filter(b -> b.getDateGenerated() != null &&
                        b.getDateGenerated().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }

    /**
     * Get total revenue
     * 
     * @return Total revenue from paid bills
     */
    public double getTotalRevenue() {
        return bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PAID)
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }

    /**
     * Get today's revenue
     * 
     * @return Revenue from bills paid today
     */
    public double getTodaysRevenue() {
        LocalDate today = LocalDate.now();
        return bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PAID &&
                        b.getDatePaid() != null &&
                        b.getDatePaid().toLocalDate().equals(today))
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }

    /**
     * Get pending amount
     * 
     * @return Total pending amount
     */
    public double getPendingAmount() {
        return bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PENDING ||
                        b.getStatus() == BillStatus.PARTIALLY_PAID)
                .mapToDouble(Bill::getBalance)
                .sum();
    }

    /**
     * Get total bill count
     * 
     * @return Total number of bills
     */
    public int getTotalCount() {
        return bills.size();
    }

    /**
     * Get count by status
     * 
     * @param status Bill status
     * @return Count
     */
    public int getCountByStatus(BillStatus status) {
        return (int) bills.stream()
                .filter(b -> b.getStatus() == status)
                .count();
    }

    /**
     * Save data to file
     */
    private void saveData() {
        FileHandler.saveBills(bills);
    }

    /**
     * Reload data from file
     */
    public void reloadData() {
        this.bills = FileHandler.loadBills();
        if (this.bills == null) {
            this.bills = new ArrayList<>();
        }
    }
}
