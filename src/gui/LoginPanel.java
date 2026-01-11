package gui;

import gui.components.*;
import managers.UserManager;
import models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Login panel for the Hospital Management System.
 * Features real authentication with username and password.
 */
public class LoginPanel extends JPanel {

    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private LoginListener loginListener;
    private UserManager userManager;

    /**
     * Interface for login callbacks
     */
    public interface LoginListener {
        void onLoginSuccess(User user);
    }

    public LoginPanel() {
        this.userManager = new UserManager();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        // Login card
        JPanel loginCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fill(new RoundRectangle2D.Float(4, 4, getWidth() - 4, getHeight() - 4, 16, 16));

                // Card
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 8, getHeight() - 8, 16, 16));

                g2.dispose();
            }
        };
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setOpaque(false);
        loginCard.setPreferredSize(new Dimension(420, 520));
        loginCard.setBorder(new EmptyBorder(40, 45, 40, 45));

        // Hospital logo/icon
        JLabel iconLabel = new JLabel("\u2695");
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 56));
        iconLabel.setForeground(PRIMARY_COLOR);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(iconLabel);
        loginCard.add(Box.createVerticalStrut(15));

        // Title
        JLabel titleLabel = new JLabel("MedCare Hospital");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(subtitleLabel);

        loginCard.add(Box.createVerticalStrut(35));

        // Username field
        JPanel usernamePanel = createFormField("Username");
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(12, 15, 12, 15)));
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        loginCard.add(usernamePanel);
        loginCard.add(Box.createVerticalStrut(20));

        // Password field
        JPanel passwordPanel = createFormField("Password");
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(12, 15, 12, 15)));
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        loginCard.add(passwordPanel);
        loginCard.add(Box.createVerticalStrut(10));

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(errorLabel);
        loginCard.add(Box.createVerticalStrut(15));

        // Login button
        StyledButton loginButton = new StyledButton("Sign In");
        loginButton.setPreferredSize(new Dimension(330, 48));
        loginButton.setMaximumSize(new Dimension(330, 48));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> attemptLogin());
        loginCard.add(loginButton);

        // Enter key to login
        passwordField.addActionListener(e -> attemptLogin());
        usernameField.addActionListener(e -> passwordField.requestFocus());

        add(loginCard);
    }

    private JPanel createFormField(String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(330, 75));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComp.setForeground(TEXT_SECONDARY);
        labelComp.setBorder(new EmptyBorder(0, 0, 8, 0));
        panel.add(labelComp, BorderLayout.NORTH);

        return panel;
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }

        // Authenticate user
        User user = userManager.authenticate(username, password);

        if (user != null) {
            errorLabel.setText(" ");
            if (loginListener != null) {
                loginListener.onLoginSuccess(user);
            }
        } else {
            errorLabel.setText("Invalid username or password");
            passwordField.setText("");
        }
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Reset the form for logout
     */
    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText(" ");
    }
}
