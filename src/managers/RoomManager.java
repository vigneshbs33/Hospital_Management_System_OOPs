package managers;

import models.Room;
import models.Room.RoomStatus;
import models.Room.RoomType;
import utils.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager class for Room operations.
 * Handles room allocation, release, and status management.
 */
public class RoomManager {

    private List<Room> rooms;

    /**
     * Constructor - loads rooms from file or initializes default rooms
     */
    public RoomManager() {
        this.rooms = FileHandler.loadRooms();
        if (this.rooms == null || this.rooms.isEmpty()) {
            initializeDefaultRooms();
        }
    }

    /**
     * Initialize default rooms for the hospital
     */
    private void initializeDefaultRooms() {
        rooms = new ArrayList<>();

        // Floor 1 - General Ward (10 rooms)
        for (int i = 1; i <= 10; i++) {
            rooms.add(new Room(String.format("1-R%02d", i), RoomType.GENERAL, 1));
        }

        // Floor 2 - Semi-Private (8 rooms)
        for (int i = 1; i <= 8; i++) {
            rooms.add(new Room(String.format("2-R%02d", i), RoomType.SEMI_PRIVATE, 2));
        }

        // Floor 3 - Private (6 rooms)
        for (int i = 1; i <= 6; i++) {
            rooms.add(new Room(String.format("3-R%02d", i), RoomType.PRIVATE, 3));
        }

        // Floor 4 - Deluxe (4 rooms)
        for (int i = 1; i <= 4; i++) {
            rooms.add(new Room(String.format("4-R%02d", i), RoomType.DELUXE, 4));
        }

        // Floor 5 - ICU (6 rooms)
        for (int i = 1; i <= 6; i++) {
            rooms.add(new Room(String.format("5-R%02d", i), RoomType.ICU, 5));
        }

        // Emergency rooms on Ground Floor
        for (int i = 1; i <= 4; i++) {
            rooms.add(new Room(String.format("0-E%02d", i), RoomType.EMERGENCY, 0));
        }

        saveData();
    }

    /**
     * Add a new room
     * 
     * @param room Room to add
     */
    public void addRoom(Room room) {
        rooms.add(room);
        saveData();
    }

    /**
     * Update room details
     * 
     * @param room Room with updated data
     * @return true if successful
     */
    public boolean updateRoom(Room room) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber().equals(room.getRoomNumber())) {
                rooms.set(i, room);
                saveData();
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a room
     * 
     * @param roomNumber Room number to delete
     * @return true if successful
     */
    public boolean deleteRoom(String roomNumber) {
        boolean removed = rooms.removeIf(r -> r.getRoomNumber().equals(roomNumber));
        if (removed) {
            saveData();
        }
        return removed;
    }

    /**
     * Allocate room to a patient
     * 
     * @param roomNumber  Room number
     * @param patientId   Patient ID
     * @param patientName Patient name
     * @return true if successful
     */
    public boolean allocateRoom(String roomNumber, String patientId, String patientName) {
        Room room = getRoomByNumber(roomNumber);
        if (room != null && room.isAvailable()) {
            room.allocate(patientId, patientName);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Release a room
     * 
     * @param roomNumber Room number
     * @return true if successful
     */
    public boolean releaseRoom(String roomNumber) {
        Room room = getRoomByNumber(roomNumber);
        if (room != null && room.getStatus() == RoomStatus.OCCUPIED) {
            room.release();
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Mark room as available after cleaning
     * 
     * @param roomNumber Room number
     * @return true if successful
     */
    public boolean markRoomAvailable(String roomNumber) {
        Room room = getRoomByNumber(roomNumber);
        if (room != null) {
            room.setStatus(RoomStatus.AVAILABLE);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Set room under maintenance
     * 
     * @param roomNumber Room number
     * @return true if successful
     */
    public boolean setMaintenance(String roomNumber) {
        Room room = getRoomByNumber(roomNumber);
        if (room != null) {
            room.setStatus(RoomStatus.MAINTENANCE);
            saveData();
            return true;
        }
        return false;
    }

    /**
     * Get room by number
     * 
     * @param roomNumber Room number
     * @return Room or null if not found
     */
    public Room getRoomByNumber(String roomNumber) {
        return rooms.stream()
                .filter(r -> r.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all rooms
     * 
     * @return List of all rooms
     */
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    /**
     * Get available rooms
     * 
     * @return List of available rooms
     */
    public List<Room> getAvailableRooms() {
        return rooms.stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Get available rooms by type
     * 
     * @param type Room type
     * @return Available rooms of that type
     */
    public List<Room> getAvailableRoomsByType(RoomType type) {
        return rooms.stream()
                .filter(r -> r.getType() == type && r.isAvailable())
                .collect(Collectors.toList());
    }

    /**
     * Get rooms by status
     * 
     * @param status Room status
     * @return Matching rooms
     */
    public List<Room> getRoomsByStatus(RoomStatus status) {
        return rooms.stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Get rooms by type
     * 
     * @param type Room type
     * @return Matching rooms
     */
    public List<Room> getRoomsByType(RoomType type) {
        return rooms.stream()
                .filter(r -> r.getType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Get rooms by floor
     * 
     * @param floor Floor number
     * @return Rooms on that floor
     */
    public List<Room> getRoomsByFloor(int floor) {
        return rooms.stream()
                .filter(r -> r.getFloor() == floor)
                .collect(Collectors.toList());
    }

    /**
     * Get room occupied by patient
     * 
     * @param patientId Patient ID
     * @return Room or null
     */
    public Room getRoomByPatient(String patientId) {
        return rooms.stream()
                .filter(r -> patientId.equals(r.getCurrentPatientId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get total room count
     * 
     * @return Total number of rooms
     */
    public int getTotalCount() {
        return rooms.size();
    }

    /**
     * Get available room count
     * 
     * @return Number of available rooms
     */
    public int getAvailableCount() {
        return (int) rooms.stream().filter(Room::isAvailable).count();
    }

    /**
     * Get occupied room count
     * 
     * @return Number of occupied rooms
     */
    public int getOccupiedCount() {
        return (int) rooms.stream()
                .filter(r -> r.getStatus() == RoomStatus.OCCUPIED)
                .count();
    }

    /**
     * Get occupancy rate
     * 
     * @return Occupancy rate as percentage
     */
    public double getOccupancyRate() {
        if (rooms.isEmpty())
            return 0;
        return (getOccupiedCount() * 100.0) / rooms.size();
    }

    /**
     * Save data to file
     */
    private void saveData() {
        FileHandler.saveRooms(rooms);
    }

    /**
     * Reload data from file
     */
    public void reloadData() {
        this.rooms = FileHandler.loadRooms();
        if (this.rooms == null || this.rooms.isEmpty()) {
            initializeDefaultRooms();
        }
    }
}
