package app;

import auth.LogIn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dataBase.DbHelper;

public class UserMainPage extends JFrame {
    private String userEmail;
    private String userRole;
    private String userName;
    private double angle1 = 0, angle2 = Math.PI / 4;
    private Timer animationTimer;

    public UserMainPage(String email) {
        this.userEmail = email;
        getUserInfo();

        setTitle("Welcome " + userName);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Charger l'image de fond
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/assets/backgroundAdmin.jpg"));

        // Charger et redimensionner le logo logout
        ImageIcon logoutImage = new ImageIcon(getClass().getResource("/assets/logout.jpg"));
        if (logoutImage.getIconWidth() == -1) {
            System.out.println("Erreur : Image logout.png introuvable !");
        } else {
            logoutImage = new ImageIcon(logoutImage.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        }

        // 🔹 HEADER AVEC TITRE + LOGOUT
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(800, 60));

        // Titre stylisé
        JLabel titleLabel = new JLabel("BIENVENUE " + userName.toUpperCase(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // 🔴 BOUTON LOGOUT À DROITE
        JButton logoutButton = new JButton(logoutImage);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> {
            dispose(); // Ferme UserMainPage
            new LogIn().setVisible(true); // 🔹 Ouvre LogIn.java
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutButton);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        // 🔹 PANEL PRINCIPAL AVEC ANIMATION
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int squareSize = 60;
                int centerX = getWidth() / 4;
                int centerY = getHeight() / 2;

                // Carreau rouge
                g2d.setColor(Color.RED);
                g2d.rotate(angle1, centerX, centerY);
                g2d.fillRect(centerX - squareSize / 2, centerY - squareSize / 2, squareSize, squareSize);
                g2d.rotate(-angle1, centerX, centerY);

                // Carreau bleu
                int centerX2 = getWidth() * 3 / 4;
                g2d.setColor(Color.BLUE);
                g2d.rotate(angle2, centerX2, centerY);
                g2d.fillRect(centerX2 - squareSize / 2, centerY - squareSize / 2, squareSize, squareSize);
                g2d.rotate(-angle2, centerX2, centerY);
            }
        };
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 200));
        panel.setOpaque(false);

        animationTimer = new Timer(30, e -> {
            angle1 += 0.05;
            angle2 -= 0.05;
            panel.repaint();
        });
        animationTimer.start();

        // 🔹 BOUTONS SELON LE ROLE
        JButton btnBoutiques = createStyledButton("Boutiques", new Color(173, 216, 230));
        btnBoutiques.addActionListener(e -> new BoutiqueFrame().setVisible(true));

        if ("admin".equals(userRole)) {
            JButton btnCRUD = createStyledButton("CRUD", new Color(144, 238, 144));
            btnCRUD.addActionListener(e -> new CRUDFrame().setVisible(true));

            JButton btnUsers = createStyledButton("Gestion Utilisateurs", new Color(255, 182, 193));
            btnUsers.addActionListener(e -> new UserListFrame().setVisible(true));

            panel.add(btnBoutiques);
            panel.add(btnCRUD);
            panel.add(btnUsers);
        } else {
            JButton btnUsers = createStyledButton("Les Utilisateurs", new Color(255, 165, 0));
            btnUsers.addActionListener(e -> new UserListFrame().setVisible(true));

            JButton btnProfile = createStyledButton("Profil", new Color(100, 149, 237));
            btnProfile.addActionListener(e -> new UserProfileFrame(userEmail).setVisible(true));

            panel.add(btnBoutiques);
            panel.add(btnUsers);
            panel.add(btnProfile);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 60));
        return button;
    }

    private void getUserInfo() {
        String sql = "SELECT lastName, role FROM users WHERE email = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userName = rs.getString("lastName");
                userRole = rs.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserMainPage("benji@example.com").setVisible(true));
    }
}