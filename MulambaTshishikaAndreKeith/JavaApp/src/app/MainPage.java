package app;

import app.BoutiqueFrame;
import app.CRUDFrame;
import app.ListBlancheFrame;
import auth.LogIn;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {
    private JLabel titleLabel;

    public MainPage() {
        setTitle("Menu Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
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

        // 🔹 PANEL DU HEADER (TITRE + LOGOUT)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(800, 60));

        // Titre animé
        titleLabel = new JLabel("BIENVENUE CHEZ ISTORE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);

        // Ajout du titre au header
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // 🔴 BOUTON LOGOUT À DROITE
        JButton logoutButton = new JButton(logoutImage);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> {
            dispose(); // Ferme la fenêtre actuelle
            new LogIn().setVisible(true); // 🔹 Ouvre LogIn.java
        });

        // Panel pour placer le logout à droite
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutButton);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        // 🔹 PANEL PRINCIPAL (AVEC IMAGE DE FOND)
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 200));
        mainPanel.setOpaque(false);

        // Boutons de navigation
        JButton btnBoutiques = createStyledButton("Boutiques", new Color(173, 216, 230));
        JButton btnCRUD = createStyledButton("CRUD", new Color(144, 238, 144));
        JButton btnListesBlanche = createStyledButton("Listes Blanche", new Color(255, 182, 193));

        btnBoutiques.addActionListener(e -> {
            dispose();
            new BoutiqueFrame().setVisible(true);
        });

        btnCRUD.addActionListener(e -> {
            dispose();
            new CRUDFrame().setVisible(true);
        });

        btnListesBlanche.addActionListener(e -> {
            dispose();
            new ListBlancheFrame().setVisible(true);
        });

        mainPanel.add(btnBoutiques);
        mainPanel.add(btnCRUD);
        mainPanel.add(btnListesBlanche);

        // Ajout des panels à la fenêtre
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 60));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainPage().setVisible(true));
    }
}