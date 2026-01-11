package gui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom styled text field with modern design.
 * Features rounded corners, focus effects, and placeholder text.
 */
public class StyledTextField extends JTextField {

    // Color constants
    private static final Color BORDER_NORMAL = new Color(226, 232, 240);
    private static final Color BORDER_FOCUS = new Color(37, 99, 235);
    private static final Color BACKGROUND = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(30, 41, 59);
    private static final Color PLACEHOLDER_COLOR = new Color(148, 163, 184);

    private String placeholder;
    private boolean showingPlaceholder = false;
    private int cornerRadius = 8;

    /**
     * Create a styled text field
     */
    public StyledTextField() {
        this("");
    }

    /**
     * Create a styled text field with placeholder
     * 
     * @param placeholder Placeholder text
     */
    public StyledTextField(String placeholder) {
        super();
        this.placeholder = placeholder;
        initializeStyle();
        setupPlaceholder();
    }

    /**
     * Create a styled text field with columns
     * 
     * @param columns Number of columns
     */
    public StyledTextField(int columns) {
        super(columns);
        initializeStyle();
    }

    /**
     * Initialize text field styling
     */
    private void initializeStyle() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(TEXT_COLOR);
        setBackground(BACKGROUND);
        setCaretColor(TEXT_COLOR);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setPreferredSize(new Dimension(200, 40));
        setOpaque(false);
    }

    /**
     * Setup placeholder functionality
     */
    private void setupPlaceholder() {
        if (placeholder != null && !placeholder.isEmpty()) {
            showPlaceholder();

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (showingPlaceholder) {
                        setText("");
                        setForeground(TEXT_COLOR);
                        showingPlaceholder = false;
                    }
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        showPlaceholder();
                    }
                    repaint();
                }
            });
        }
    }

    /**
     * Show placeholder text
     */
    private void showPlaceholder() {
        setText(placeholder);
        setForeground(PLACEHOLDER_COLOR);
        showingPlaceholder = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Draw border
        g2.setColor(hasFocus() ? BORDER_FOCUS : BORDER_NORMAL);
        g2.setStroke(new BasicStroke(hasFocus() ? 2 : 1));
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        g2.dispose();

        super.paintComponent(g);
    }

    /**
     * Get the actual text (not placeholder)
     * 
     * @return Text content
     */
    public String getActualText() {
        return showingPlaceholder ? "" : getText();
    }

    /**
     * Set placeholder text
     * 
     * @param placeholder Placeholder text
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (getText().isEmpty() || showingPlaceholder) {
            showPlaceholder();
        }
    }

    /**
     * Clear the text field
     */
    public void clear() {
        setText("");
        if (placeholder != null && !placeholder.isEmpty()) {
            showPlaceholder();
        }
    }

    /**
     * Set corner radius
     * 
     * @param radius Corner radius
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    /**
     * Check if field is empty (ignoring placeholder)
     * 
     * @return true if empty
     */
    public boolean isEmpty() {
        return getActualText().isEmpty();
    }
}
