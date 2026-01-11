package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom styled button with modern design.
 * Features rounded corners, hover effects, and customizable colors.
 */
public class StyledButton extends JButton {

    // Color constants
    public static final Color PRIMARY = new Color(37, 99, 235); // Blue
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216);
    public static final Color SECONDARY = new Color(100, 116, 139); // Slate
    public static final Color SECONDARY_HOVER = new Color(71, 85, 105);
    public static final Color SUCCESS = new Color(22, 163, 74); // Green
    public static final Color SUCCESS_HOVER = new Color(21, 128, 61);
    public static final Color DANGER = new Color(220, 38, 38); // Red
    public static final Color DANGER_HOVER = new Color(185, 28, 28);
    public static final Color WARNING = new Color(217, 119, 6); // Amber
    public static final Color WARNING_HOVER = new Color(180, 83, 9);

    private Color backgroundColor;
    private Color hoverColor;
    private Color currentColor;
    private int cornerRadius = 8;
    private boolean isHovered = false;

    /**
     * Create a primary styled button
     * 
     * @param text Button text
     */
    public StyledButton(String text) {
        this(text, PRIMARY, PRIMARY_HOVER);
    }

    /**
     * Create a styled button with custom colors
     * 
     * @param text       Button text
     * @param bgColor    Background color
     * @param hoverColor Hover color
     */
    public StyledButton(String text, Color bgColor, Color hoverColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = hoverColor;
        this.currentColor = bgColor;

        initializeStyle();
        setupHoverEffect();
    }

    /**
     * Initialize button styling
     */
    private void initializeStyle() {
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 36));
    }

    /**
     * Setup hover effect
     */
    private void setupHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                currentColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                currentColor = backgroundColor;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                currentColor = hoverColor.darker();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentColor = isHovered ? hoverColor : backgroundColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setColor(currentColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Draw text
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    /**
     * Set button colors
     * 
     * @param bgColor    Background color
     * @param hoverColor Hover color
     */
    public void setColors(Color bgColor, Color hoverColor) {
        this.backgroundColor = bgColor;
        this.hoverColor = hoverColor;
        this.currentColor = bgColor;
        repaint();
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
     * Create a secondary button
     * 
     * @param text Button text
     * @return Secondary styled button
     */
    public static StyledButton secondary(String text) {
        return new StyledButton(text, SECONDARY, SECONDARY_HOVER);
    }

    /**
     * Create a success button
     * 
     * @param text Button text
     * @return Success styled button
     */
    public static StyledButton success(String text) {
        return new StyledButton(text, SUCCESS, SUCCESS_HOVER);
    }

    /**
     * Create a danger button
     * 
     * @param text Button text
     * @return Danger styled button
     */
    public static StyledButton danger(String text) {
        return new StyledButton(text, DANGER, DANGER_HOVER);
    }

    /**
     * Create a warning button
     * 
     * @param text Button text
     * @return Warning styled button
     */
    public static StyledButton warning(String text) {
        return new StyledButton(text, WARNING, WARNING_HOVER);
    }
}
