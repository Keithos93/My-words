package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.MainPage;
import app.UserMainPage;
import dataBase.DbHelper;

public class LogIn extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LogIn() {
        this.setTitle("Login");
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
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(33, 150, 243));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formulaire.add(welcomeLabel, gbc);

        // Label pour "Log In"
        JLabel titre = new JLabel("Log In", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(new Color(33, 150, 243));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 1;
        gbc.gridy++;
        formulaire.add(titre, gbc);

        gbc.gridwidth = 1;
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

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JButton logInButton = new JButton("LOG IN");
        logInButton.setFont(new Font("Arial", Font.BOLD, 16));
        logInButton.setBackground(new Color(33, 150, 243));
        logInButton.setForeground(Color.WHITE);
        logInButton.setFocusPainted(false);
        logInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logInButton.addActionListener(e -> handleLogIn());
        formulaire.add(logInButton, gbc);

        gbc.gridy++;
        JLabel signUpLink = new JLabel("Vous n'avez pas de compte ? Inscrivez-vous ici");
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLink.setForeground(new Color(33, 150, 243));
        signUpLink.setHorizontalAlignment(SwingConstants.CENTER);
        signUpLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new SignUp();
                dispose();
            }
        });
        formulaire.add(signUpLink, gbc);

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
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return passwordField;
    }

    private void handleLogIn() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DbHelper.getConnection()) {
            String sql = "SELECT pseudo, password, role FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                String pseudo = resultSet.getString("pseudo");
                String role = resultSet.getString("role");

                if (PasswordUtils.checkPassword(password, storedPassword)) {
                    JOptionPane.showMessageDialog(this, "Connexion réussie !");
                    this.dispose();

                    SwingUtilities.invokeLater(() -> {
                        if ("admin".equalsIgnoreCase(role)) {
                            new MainPage().setVisible(true);
                        } else {
                            new UserMainPage(email).setVisible(true);
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Ce compte n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
