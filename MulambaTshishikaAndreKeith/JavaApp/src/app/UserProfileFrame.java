package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dataBase.DbHelper;

public class UserProfileFrame extends JFrame {
    private String userEmail;
    private JTextField txtLastName, txtPseudo, txtEmail;
    private JPasswordField txtPassword;

    public UserProfileFrame(String email) {
        this.userEmail = email;

        setTitle("Mon Profil");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Maximize window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));  // Changed to BoxLayout
        setBackground(new Color(255, 255, 255)); // White background

        // Title label - "Mon Profil"
        JLabel lblTitle = new JLabel("Mon Profil");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));  // Larger bold font for title
        lblTitle.setForeground(new Color(33, 150, 243)); // Soft blue
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblTitle);

        // Add space
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel to hold text fields
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBackground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);

        // Fields for User Profile
        panel.add(new JLabel("Nom:"));
        txtLastName = new JTextField(20);
        txtLastName.setFont(font);
        panel.add(txtLastName);

        panel.add(new JLabel("Pseudo:"));
        txtPseudo = new JTextField(20);
        txtPseudo.setFont(font);
        panel.add(txtPseudo);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField(20);
        txtEmail.setFont(font);
        txtEmail.setEditable(false);
        panel.add(txtEmail);

        panel.add(new JLabel("Nouveau mot de passe:"));
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(font);
        panel.add(txtPassword);

        add(panel);

        // Add space
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Save Button (Blue)
        JButton btnSave = new JButton("Mettre à jour");
        btnSave.setFont(font);
        btnSave.setBackground(new Color(33, 150, 243));  // Light Blue
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(180, 40));
        btnSave.addActionListener(e -> updateProfile());
        buttonPanel.add(btnSave);

        // Delete Button (Red)
        JButton btnDelete = new JButton("Supprimer mon compte");
        btnDelete.setFont(font);
        btnDelete.setBackground(new Color(204, 0, 0));  // Red
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(180, 40));
        btnDelete.addActionListener(e -> deleteUser());
        buttonPanel.add(btnDelete);

        // Back Button (Soft blue)
        JButton backButton = new JButton("Retour");
        backButton.setFont(font);
        backButton.setBackground(new Color(0, 123, 255));  // Custom blue
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(180, 40));
        backButton.addActionListener(e -> dispose());  // Close the frame or go back
        buttonPanel.add(backButton);

        add(buttonPanel);

        loadUserProfile();
    }

    private void loadUserProfile() {
        String sql = "SELECT lastName, pseudo, email FROM users WHERE email = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtLastName.setText(rs.getString("lastName"));
                txtPseudo.setText(rs.getString("pseudo"));
                txtEmail.setText(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        String sql = "UPDATE users SET lastName = ?, pseudo = ?, password = SHA2(?, 256) WHERE email = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtLastName.getText());
            ps.setString(2, txtPseudo.getText());
            ps.setString(3, new String(txtPassword.getPassword()));
            ps.setString(4, userEmail);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Profil mis à jour !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer votre compte ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM users WHERE email = ?";
            try (Connection conn = DbHelper.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, userEmail);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Compte supprimé !");
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}