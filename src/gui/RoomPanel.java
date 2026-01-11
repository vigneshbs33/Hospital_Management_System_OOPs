package gui;

import gui.components.*;
import managers.HospitalManager;
import managers.RoomManager;
import models.Room;
import models.Room.RoomStatus;
import models.Room.RoomType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Room management panel with visual grid view.
 * Shows room availability, types, and allows allocation.
 */
public class RoomPanel extends JPanel {

    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    // Room status colors
    private static final Color AVAILABLE_COLOR = new Color(34, 197, 94);
    private static final Color OCCUPIED_COLOR = new Color(239, 68, 68);
    private static final Color MAINTENANCE_COLOR = new Color(245, 158, 11);
    private static final Color CLEANING_COLOR = new Color(59, 130, 246);

    private RoomManager roomManager;
    private JPanel roomsGrid;

    public RoomPanel() {
        this.roomManager = HospitalManager.getInstance().getRoomManager();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createLegend(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        header.setBorder(new EmptyBorder(0, 0, 25, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(BACKGROUND);

        JLabel titleLabel = new JLabel("Room Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        leftPanel.add(titleLabel);

        header.add(leftPanel, BorderLayout.WEST);

        // Stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statsPanel.setBackground(BACKGROUND);

        int available = roomManager.getAvailableCount();
        int occupied = roomManager.getOccupiedCount();
        int total = roomManager.getTotalCount();

        statsPanel.add(createStatLabel("Total: " + total, TEXT_SECONDARY));
        statsPanel.add(createStatLabel("Available: " + available, AVAILABLE_COLOR));
        statsPanel.add(createStatLabel("Occupied: " + occupied, OCCUPIED_COLOR));
        statsPanel.add(
                createStatLabel("Occupancy: " + String.format("%.0f%%", roomManager.getOccupancyRate()), TEXT_PRIMARY));

        StyledButton refreshBtn = StyledButton.secondary("Refresh");
        refreshBtn.addActionListener(e -> refresh());
        statsPanel.add(refreshBtn);

        header.add(statsPanel, BorderLayout.EAST);

        return header;
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(color);
        return label;
    }

    private JScrollPane createContent() {
        roomsGrid = new JPanel();
        roomsGrid.setLayout(new BoxLayout(roomsGrid, BoxLayout.Y_AXIS));
        roomsGrid.setBackground(BACKGROUND);

        loadRoomsByFloor();

        JScrollPane scrollPane = new JScrollPane(roomsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getViewport().setBackground(BACKGROUND);

        return scrollPane;
    }

    private void loadRoomsByFloor() {
        roomsGrid.removeAll();

        // Group rooms by floor
        for (int floor = 0; floor <= 5; floor++) {
            List<Room> floorRooms = roomManager.getRoomsByFloor(floor);
            if (!floorRooms.isEmpty()) {
                roomsGrid.add(createFloorSection(floor, floorRooms));
                roomsGrid.add(Box.createVerticalStrut(20));
            }
        }

        roomsGrid.revalidate();
        roomsGrid.repaint();
    }

    private JPanel createFloorSection(int floor, List<Room> rooms) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(BACKGROUND);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Floor label
        String floorName = floor == 0 ? "Ground Floor (Emergency)"
                : "Floor " + floor + " (" + getFloorType(floor) + ")";
        JLabel floorLabel = new JLabel(floorName);
        floorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        floorLabel.setForeground(TEXT_PRIMARY);
        floorLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        section.add(floorLabel, BorderLayout.NORTH);

        // Rooms grid
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        grid.setBackground(BACKGROUND);

        for (Room room : rooms) {
            grid.add(createRoomCard(room));
        }

        section.add(grid, BorderLayout.CENTER);

        return section;
    }

    private String getFloorType(int floor) {
        switch (floor) {
            case 1:
                return "General Ward";
            case 2:
                return "Semi-Private";
            case 3:
                return "Private";
            case 4:
                return "Deluxe";
            case 5:
                return "ICU";
            default:
                return "";
        }
    }

    private JPanel createRoomCard(Room room) {
        Color statusColor = getStatusColor(room.getStatus());

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                // Status indicator bar
                g2.setColor(statusColor);
                g2.fillRect(0, getHeight() - 4, getWidth(), 4);

                g2.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(100, 90));
        card.setBorder(new EmptyBorder(10, 12, 10, 12));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Room number
        JLabel roomNumLabel = new JLabel(room.getRoomNumber());
        roomNumLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomNumLabel.setForeground(TEXT_PRIMARY);
        roomNumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(roomNumLabel);

        card.add(Box.createVerticalStrut(5));

        // Room type
        JLabel typeLabel = new JLabel(
                room.getType().getDisplayName().substring(0, Math.min(room.getType().getDisplayName().length(), 8)));
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        typeLabel.setForeground(TEXT_SECONDARY);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(typeLabel);

        card.add(Box.createVerticalStrut(5));

        // Status
        JLabel statusLabel = new JLabel(room.getStatus().getDisplayName().substring(0,
                Math.min(room.getStatus().getDisplayName().length(), 10)));
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        statusLabel.setForeground(statusColor);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statusLabel);

        // Click handler
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showRoomDetails(room);
            }
        });

        return card;
    }

    private Color getStatusColor(RoomStatus status) {
        switch (status) {
            case AVAILABLE:
                return AVAILABLE_COLOR;
            case OCCUPIED:
                return OCCUPIED_COLOR;
            case MAINTENANCE:
                return MAINTENANCE_COLOR;
            case CLEANING:
                return CLEANING_COLOR;
            case RESERVED:
                return new Color(139, 92, 246);
            default:
                return TEXT_SECONDARY;
        }
    }

    private void showRoomDetails(Room room) {
        String[] options;
        if (room.getStatus() == RoomStatus.AVAILABLE) {
            options = new String[] { "Allocate", "Maintenance", "Close" };
        } else if (room.getStatus() == RoomStatus.OCCUPIED) {
            options = new String[] { "Release", "View Patient", "Close" };
        } else if (room.getStatus() == RoomStatus.CLEANING || room.getStatus() == RoomStatus.MAINTENANCE) {
            options = new String[] { "Mark Available", "Close" };
        } else {
            options = new String[] { "Close" };
        }

        StringBuilder info = new StringBuilder();
        info.append("Room: ").append(room.getRoomNumber()).append("\n");
        info.append("Type: ").append(room.getType().getDisplayName()).append("\n");
        info.append("Status: ").append(room.getStatus().getDisplayName()).append("\n");
        info.append("Price: ₹").append(String.format("%.0f", room.getPricePerDay())).append("/day\n");
        info.append("Beds: ").append(room.getBedCount()).append("\n");

        if (room.getStatus() == RoomStatus.OCCUPIED && room.getCurrentPatientName() != null) {
            info.append("Patient: ").append(room.getCurrentPatientName()).append("\n");
        }

        int choice = JOptionPane.showOptionDialog(this, info.toString(), "Room " + room.getRoomNumber(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[options.length - 1]);

        if (room.getStatus() == RoomStatus.AVAILABLE && choice == 0) {
            // Allocate room
            String patientId = JOptionPane.showInputDialog(this, "Enter Patient ID:");
            String patientName = JOptionPane.showInputDialog(this, "Enter Patient Name:");
            if (patientId != null && patientName != null) {
                roomManager.allocateRoom(room.getRoomNumber(), patientId, patientName);
                refresh();
            }
        } else if (room.getStatus() == RoomStatus.AVAILABLE && choice == 1) {
            // Set maintenance
            roomManager.setMaintenance(room.getRoomNumber());
            refresh();
        } else if (room.getStatus() == RoomStatus.OCCUPIED && choice == 0) {
            // Release room
            roomManager.releaseRoom(room.getRoomNumber());
            refresh();
        } else if ((room.getStatus() == RoomStatus.CLEANING || room.getStatus() == RoomStatus.MAINTENANCE)
                && choice == 0) {
            // Mark available
            roomManager.markRoomAvailable(room.getRoomNumber());
            refresh();
        }
    }

    private JPanel createLegend() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        legend.setBackground(BACKGROUND);
        legend.setBorder(new EmptyBorder(15, 0, 0, 0));

        legend.add(createLegendItem("Available", AVAILABLE_COLOR));
        legend.add(createLegendItem("Occupied", OCCUPIED_COLOR));
        legend.add(createLegendItem("Maintenance", MAINTENANCE_COLOR));
        legend.add(createLegendItem("Cleaning", CLEANING_COLOR));

        return legend;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        item.setBackground(BACKGROUND);

        JPanel colorBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 4, 4));
                g2.dispose();
            }
        };
        colorBox.setPreferredSize(new Dimension(16, 16));
        colorBox.setOpaque(false);
        item.add(colorBox);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_SECONDARY);
        item.add(label);

        return item;
    }

    public void refresh() {
        loadRoomsByFloor();
        revalidate();
        repaint();
    }
}
