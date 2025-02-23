package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import app.MainPage;
import dataBase.DbHelper;

public class SignUp extends JFrame {

    private JTextField fullNameField;
    private JTextField pseudoField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignUp() {
        this.setTitle("Sign Up");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel formulaire = new JPanel();
        formulaire.setLayout(new GridBagLayout());
        formulaire.setBorder(new EmptyBorder(20, 20, 20, 20));
        formulaire.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nouveau label "Welcome to the iStore"
        JLabel welcomeLabel = new JLabel("Welcome to the iStore", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(33, 150, 243));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formulaire.add(welcomeLabel, gbc);

        // Label pour "Sign Up"
        JLabel titre = new JLabel("Sign Up", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 25));
        titre.setForeground(new Color(33, 150, 243));
        gbc.gridwidth = 1;
        gbc.gridy++;
        formulaire.add(titre, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        formulaire.add(createLabel("Full Name:"), gbc);
        fullNameField = createTextField();
        gbc.gridx = 1;
        formulaire.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formulaire.add(createLabel("Pseudo:"), gbc);
        pseudoField = createTextField();
        gbc.gridx = 1;
        formulaire.add(pseudoField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formulaire.add(createLabel("Email:"), gbc);
        emailField = createTextField();
        gbc.gridx = 1;
        formulaire.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formulaire.add(createLabel("Password:"), gbc);
        passwordField = createPasswordField();
        gbc.gridx = 1;
        formulaire.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formulaire.add(createLabel("Confirm Password:"), gbc);
        confirmPasswordField = createPasswordField();
        gbc.gridx = 1;
        formulaire.add(confirmPasswordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JButton signUpButton = new JButton("SIGN UP");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setBackground(new Color(33, 150, 243));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.addActionListener(e -> handleSignUp());
        formulaire.add(signUpButton, gbc);

        // Ajout du lien vers la page de connexion
        JLabel loginLink = new JLabel("<html><font color='blue'><u>Oups! Vous avez déjà un compte ? Connectez-vous ici.</u></font></html>");
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setFont(new Font("Arial", Font.PLAIN, 14));
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new LogIn();
                dispose();
            }
        });

        gbc.gridy++;
        formulaire.add(loginLink, gbc);

        this.add(formulaire);
        this.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(300, 30));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 30));
        return passwordField;
    }

    private void handleSignUp() {
        String fullName = fullNameField.getText();
        String pseudo = pseudoField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || pseudo.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DbHelper.getConnection()) {
            String checkEmailSql = "SELECT email FROM listeBlanche WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql);
            checkStmt.setString(1, email);
            boolean emailExistsInWhitelist = checkStmt.executeQuery().next();
            if (!emailExistsInWhitelist) {
                JOptionPane.showMessageDialog(this, "Your email is not in the whitelist.", "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String hashedPassword = PasswordUtils.hashPassword(password);
            String sql = "INSERT INTO users (lastname, pseudo, email, password, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fullName);
            stmt.setString(2, pseudo);
            stmt.setString(3, email);
            stmt.setString(4, hashedPassword);
            stmt.setString(5, "client");
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new MainPage();
            this.dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
