package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ListBlancheFrame extends JFrame {
    private ImageIcon logoIcon;
    private JLabel logoLabel;
    private JTextField emailField;
    private JButton btnAjouter;
    private JButton btnRetour;

    public ListBlancheFrame() {
        setTitle("Liste Blanche");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Style global de la fenêtre
        setBackground(new Color(250, 250, 250)); // Fond blanc cassé
        setFont(new Font("Segoe UI", Font.PLAIN, 18));

        // Logo (optionnel)
        logoIcon = new ImageIcon("carnet_logo.png"); // Remplace par ton logo
        logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, BorderLayout.NORTH);

        // Titre de la fenêtre
        JLabel title = new JLabel("Liste Blanche", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(new Color(65, 105, 225)); // Bleu vif
        add(title, BorderLayout.NORTH);

        // Panel central avec du padding pour centrer les éléments
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(250, 250, 250));

        // Champ de texte pour l'email
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(65, 105, 225), 2, true));
        emailField.setHorizontalAlignment(JTextField.CENTER);
        emailPanel.add(new JLabel("Entrez un email : "));
        emailPanel.add(emailField);
        emailPanel.setBackground(new Color(250, 250, 250));
        centerPanel.add(emailPanel);

        // Bouton Ajouter avec un design moderne
        btnAjouter = new JButton("Ajouter à la liste blanche");
        styleButton(btnAjouter, new Color(144, 238, 144)); // Vert doux
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();

                // Vérification de l'email
                if (email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    showErrorMessage("Veuillez entrer un email valide.");
                    return;
                }

                // Vérifier si l'email existe déjà dans la table 'liste_blanche'
                if (emailExistsInDatabase(email)) {
                    showErrorMessage("Cet email existe déjà dans la liste blanche.");
                } else {
                    // Ajouter l'email à la base de données
                    addEmailToDatabase(email);
                    emailField.setText(""); // Réinitialiser le champ de texte
                    showSuccessMessage("L'email a été ajouté avec succès.");
                }
            }
        });

        // Bouton retour avec un design attrayant
        btnRetour = new JButton("Retour");
        styleButton(btnRetour, new Color(255, 99, 71)); // Rouge doux
        btnRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre actuelle
                new MainPage().setVisible(true); // Ouvre la fenêtre principale
            }
        });

        // Ajouter les boutons côte à côte avec un espacement entre eux
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Espacement entre les boutons
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.add(btnRetour);
        buttonPanel.add(btnAjouter);

        // Finalisation de la fenêtre
        centerPanel.add(buttonPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Afficher la fenêtre
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Méthode pour appliquer le style aux boutons
    private void styleButton(JButton button, Color bgColor) {
        button.setPreferredSize(new Dimension(250, 50));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));
        button.setOpaque(true);
        button.setFocusable(false);

        // Animation au survol du bouton
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    // Méthode pour afficher un message d'erreur
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(ListBlancheFrame.this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // Méthode pour afficher un message de succès
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(ListBlancheFrame.this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    // Méthode pour vérifier si l'email existe dans la base de données
    private boolean emailExistsInDatabase(String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/istore", "root", "")) {
            String query = "SELECT COUNT(*) FROM listeblanche WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode pour ajouter l'email à la base de données
    private void addEmailToDatabase(String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/istore", "root", "")) {
            String insertQuery = "INSERT INTO listeblanche (email) VALUES (?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setString(1, email);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode principale pour lancer la fenêtre
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ListBlancheFrame::new);
    }
}
