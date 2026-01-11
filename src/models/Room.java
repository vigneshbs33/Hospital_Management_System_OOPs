package models;

import java.io.Serializable;

/**
 * Room class for managing hospital rooms.
 * Demonstrates OOP concepts with enumerations for room types and status.
 */
public class Room implements Serializable {

    private String roomNumber;
    private RoomType type;
    private RoomStatus status;
    private double pricePerDay;
    private String currentPatientId;
    private String currentPatientName;
    private int floor;
    private int bedCount;
    private String features;

    /**
     * Room type enumeration
     */
    public enum RoomType {
        GENERAL("General Ward", 500),
        SEMI_PRIVATE("Semi-Private", 1500),
        PRIVATE("Private", 3000),
        DELUXE("Deluxe", 5000),
        ICU("ICU", 8000),
        OPERATION_THEATER("Operation Theater", 15000),
        EMERGENCY("Emergency", 2000);

        private final String displayName;
        private final double basePrice;

        RoomType(String displayName, double basePrice) {
            this.displayName = displayName;
            this.basePrice = basePrice;
        }

        public String getDisplayName() {
            return displayName;
        }

        public double getBasePrice() {
            return basePrice;
        }
    }

    /**
     * Room status enumeration
     */
    public enum RoomStatus {
        AVAILABLE("Available"),
        OCCUPIED("Occupied"),
        MAINTENANCE("Under Maintenance"),
        RESERVED("Reserved"),
        CLEANING("Cleaning");

        private final String displayName;

        RoomStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Default constructor
     */
    public Room() {
        this.status = RoomStatus.AVAILABLE;
        this.bedCount = 1;
    }

    /**
     * Parameterized constructor
     */
    public Room(String roomNumber, RoomType type, int floor) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.floor = floor;
        this.status = RoomStatus.AVAILABLE;
        this.pricePerDay = type.getBasePrice();
        this.bedCount = type == RoomType.GENERAL ? 6 : (type == RoomType.SEMI_PRIVATE ? 2 : 1);
    }

    /**
     * Allocate room to a patient
     * 
     * @param patientId   Patient's ID
     * @param patientName Patient's name
     */
    public void allocate(String patientId, String patientName) {
        this.currentPatientId = patientId;
        this.currentPatientName = patientName;
        this.status = RoomStatus.OCCUPIED;
    }

    /**
     * Release the room
     */
    public void release() {
        this.currentPatientId = null;
        this.currentPatientName = null;
        this.status = RoomStatus.CLEANING;
    }

    /**
     * Mark room as available after cleaning
     */
    public void markAvailable() {
        this.status = RoomStatus.AVAILABLE;
    }

    /**
     * Check if room is available
     * 
     * @return true if available
     */
    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    // Getters and Setters

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
        if (pricePerDay == 0) {
            this.pricePerDay = type.getBasePrice();
        }
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getCurrentPatientId() {
        return currentPatientId;
    }

    public void setCurrentPatientId(String currentPatientId) {
        this.currentPatientId = currentPatientId;
    }

    public String getCurrentPatientName() {
        return currentPatientName;
    }

    public void setCurrentPatientName(String currentPatientName) {
        this.currentPatientName = currentPatientName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getBedCount() {
        return bedCount;
    }

    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number='" + roomNumber + '\'' +
                ", type=" + type.getDisplayName() +
                ", status=" + status.getDisplayName() +
                ", price=₹" + pricePerDay + "/day" +
                '}';
    }
}
