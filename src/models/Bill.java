package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Bill class for managing patient billing.
 * Demonstrates OOP concept: Composition (contains BillItem objects).
 */
public class Bill implements Serializable {

    private String billId;
    private String patientId;
    private String patientName;
    private List<BillItem> items;
    private double totalAmount;
    private double paidAmount;
    private double discount;
    private BillStatus status;
    private LocalDateTime dateGenerated;
    private LocalDateTime datePaid;
    private String paymentMethod;

    /**
     * Bill status enumeration
     */
    public enum BillStatus {
        PENDING("Pending"),
        PARTIALLY_PAID("Partially Paid"),
        PAID("Paid"),
        CANCELLED("Cancelled");

        private final String displayName;

        BillStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Nested BillItem class
     * Demonstrates inner class concept
     */
    public static class BillItem implements Serializable {
        private String description;
        private String category;
        private int quantity;
        private double unitPrice;
        private double amount;

        public BillItem() {
        }

        public BillItem(String description, String category, int quantity, double unitPrice) {
            this.description = description;
            this.category = category;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.amount = quantity * unitPrice;
        }

        // Getters and Setters
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
            this.amount = quantity * unitPrice;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            this.amount = quantity * unitPrice;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return description + " x" + quantity + " = ₹" + String.format("%.2f", amount);
        }
    }

    /**
     * Default constructor
     */
    public Bill() {
        this.items = new ArrayList<>();
        this.status = BillStatus.PENDING;
        this.dateGenerated = LocalDateTime.now();
        this.totalAmount = 0;
        this.paidAmount = 0;
        this.discount = 0;
    }

    /**
     * Parameterized constructor
     */
    public Bill(String billId, String patientId, String patientName) {
        this.billId = billId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.items = new ArrayList<>();
        this.status = BillStatus.PENDING;
        this.dateGenerated = LocalDateTime.now();
        this.totalAmount = 0;
        this.paidAmount = 0;
        this.discount = 0;
    }

    /**
     * Add an item to the bill
     * 
     * @param item BillItem to add
     */
    public void addItem(BillItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        calculateTotal();
    }

    /**
     * Remove an item from the bill
     * 
     * @param index Index of item to remove
     */
    public void removeItem(int index) {
        if (items != null && index >= 0 && index < items.size()) {
            items.remove(index);
            calculateTotal();
        }
    }

    /**
     * Calculate total amount
     */
    public void calculateTotal() {
        double sum = 0;
        if (items != null) {
            for (BillItem item : items) {
                sum += item.getAmount();
            }
        }
        this.totalAmount = sum - discount;
    }

    /**
     * Process payment
     * 
     * @param amount Amount to pay
     * @param method Payment method
     */
    public void processPayment(double amount, String method) {
        this.paidAmount += amount;
        this.paymentMethod = method;

        if (paidAmount >= totalAmount) {
            this.status = BillStatus.PAID;
            this.datePaid = LocalDateTime.now();
        } else if (paidAmount > 0) {
            this.status = BillStatus.PARTIALLY_PAID;
        }
    }

    /**
     * Get remaining balance
     * 
     * @return Balance amount
     */
    public double getBalance() {
        return totalAmount - paidAmount;
    }

    /**
     * Get formatted date generated
     * 
     * @return Formatted date
     */
    public String getFormattedDateGenerated() {
        if (dateGenerated == null)
            return "";
        return dateGenerated.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }

    // Getters and Setters

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
        calculateTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        calculateTotal();
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(LocalDateTime dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public LocalDateTime getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(LocalDateTime datePaid) {
        this.datePaid = datePaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId='" + billId + '\'' +
                ", patient='" + patientName + '\'' +
                ", total=₹" + String.format("%.2f", totalAmount) +
                ", status=" + status.getDisplayName() +
                '}';
    }
}
