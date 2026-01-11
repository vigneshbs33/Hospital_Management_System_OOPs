package gui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

/**
 * Custom styled table with modern design.
 * Features alternating row colors, custom header, and clean aesthetics.
 */
public class StyledTable extends JTable {

    // Color constants
    private static final Color HEADER_BG = new Color(248, 250, 252);
    private static final Color HEADER_FG = new Color(71, 85, 105);
    private static final Color ROW_EVEN = Color.WHITE;
    private static final Color ROW_ODD = new Color(248, 250, 252);
    private static final Color ROW_SELECTED = new Color(219, 234, 254);
    private static final Color GRID_COLOR = new Color(226, 232, 240);
    private static final Color TEXT_COLOR = new Color(30, 41, 59);

    /**
     * Create a styled table with given model
     * 
     * @param model Table model
     */
    public StyledTable(TableModel model) {
        super(model);
        initializeStyle();
    }

    /**
     * Create a styled table
     */
    public StyledTable() {
        super();
        initializeStyle();
    }

    /**
     * Initialize table styling
     */
    private void initializeStyle() {
        // General table settings
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setRowHeight(40);
        setShowGrid(true);
        setGridColor(GRID_COLOR);
        setBackground(Color.WHITE);
        setForeground(TEXT_COLOR);
        setSelectionBackground(ROW_SELECTED);
        setSelectionForeground(TEXT_COLOR);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Header styling
        JTableHeader header = getTableHeader();
        header.setBackground(HEADER_BG);
        header.setForeground(HEADER_FG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GRID_COLOR));
        header.setReorderingAllowed(false);

        // Custom header renderer
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(HEADER_BG);
                label.setForeground(HEADER_FG);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setBorder(new EmptyBorder(0, 15, 0, 15));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                return label;
            }
        });

        // Custom cell renderer for alternating rows
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    label.setBackground(ROW_SELECTED);
                } else {
                    label.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                }
                label.setForeground(TEXT_COLOR);
                label.setBorder(new EmptyBorder(0, 15, 0, 15));
                return label;
            }
        });
    }

    /**
     * Set column widths
     * 
     * @param widths Array of column widths
     */
    public void setColumnWidths(int... widths) {
        TableColumnModel columnModel = getColumnModel();
        for (int i = 0; i < widths.length && i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Auto-resize columns to fit content
     */
    public void autoResizeColumns() {
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * Create a scroll pane containing this table
     * 
     * @return JScrollPane with styled table
     */
    public JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setBorder(BorderFactory.createLineBorder(GRID_COLOR));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    /**
     * Refresh table data
     */
    public void refresh() {
        ((AbstractTableModel) getModel()).fireTableDataChanged();
    }
}
