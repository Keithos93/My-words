package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dataBase.DbHelper;

public class UserListFrame extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public UserListFrame() {
        setTitle("Utilisateurs");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // *Panel Haut (Titre + Barre de recherche + Logo)*
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 150, 243)); // Bleu comme sur l'image
        topPanel.setPreferredSize(new Dimension(getWidth(), 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Logo
        ImageIcon logoIcon = new ImageIcon("path/to/logo.png"); // Remplacez "path/to/logo.png" par le chemin vers votre logo
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setPreferredSize(new Dimension(40, 40)); // Ajustez la taille de votre logo

        // Titre
        JLabel titleLabel = new JLabel("Utilisateurs", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        // *Champ de recherche*
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setToolTipText("Rechercher par nom ou email");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterUsers(searchField.getText());
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("🔍"));
        searchPanel.add(searchField);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(logoLabel);
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // *Tableau des utilisateurs*
        String[] columns = {"Identifiant", "Nom complet", "Pseudo", "Courriel", "Rôle"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // *Chargement des utilisateurs*
        loadUsers();

        // *Ajout du tableau dans un scroll pane*
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // *Panel Bas avec bouton retour*
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Retour");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(33, 150, 243));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Action pour le bouton retour (fermer la fenêtre)
        backButton.addActionListener(e -> dispose());

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        String sql = "SELECT id, lastName, pseudo, email, role FROM users";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String lastName = rs.getString("lastName");
                String pseudo = rs.getString("pseudo");
                String email = rs.getString("email");
                String role = rs.getString("role");

                model.addRow(new Object[]{id, lastName, pseudo, email, role});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterUsers(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 1, 3)); // Filtre sur le nom et l'email
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserListFrame().setVisible(true));
    }
}