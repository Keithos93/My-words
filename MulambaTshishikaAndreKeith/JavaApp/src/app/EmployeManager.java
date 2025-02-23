package app;

import app.UserListFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dataBase.DbHelper;

public class EmployeManager extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public EmployeManager() {
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

        // *Panel Bas avec les boutons d'actions (Ajouter, Mettre à jour, Supprimer, Retour)*
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Ajouter Utilisateur
        JButton addButton = new JButton("Ajouter Utilisateur");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(new Color(33, 150, 243));
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(180, 40));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addUser());

        // Mettre à jour
        JButton updateButton = new JButton("Mettre à jour");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setBackground(new Color(0, 204, 102)); // Vert
        updateButton.setForeground(Color.WHITE);
        updateButton.setPreferredSize(new Dimension(180, 40));
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        updateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(e -> updateUser());

        // Supprimer
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setBackground(new Color(204, 0, 0)); // Rouge
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(180, 40));
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteUser());

        // Retour
        JButton backButton = new JButton("Retour");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(33, 150, 243));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());

        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
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

    // Ajouter un utilisateur
    private void addUser() {
        JTextField nameField = new JTextField();
        JTextField pseudoField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField roleField = new JTextField();

        Object[] message = {
                "Nom:", nameField,
                "Pseudo:", pseudoField,
                "Email:", emailField,
                "Mot de passe:", passwordField,
                "Rôle:", roleField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Ajouter Utilisateur", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String pseudo = pseudoField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleField.getText();

            // Insérer dans la base de données
            String sql = "INSERT INTO users (lastName, pseudo, email, password, role) VALUES (?, ?, ?, SHA2(?, 256), ?)";
            try (Connection conn = DbHelper.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, pseudo);
                ps.setString(3, email);
                ps.setString(4, password);
                ps.setString(5, role);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Utilisateur ajouté !");
                model.addRow(new Object[]{email, name, pseudo, email, role});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Mettre à jour un utilisateur
    private void updateUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) table.getValueAt(selectedRow, 0);
            String userEmail = (String) table.getValueAt(selectedRow, 3);

            JTextField nameField = new JTextField((String) table.getValueAt(selectedRow, 1));
            JTextField pseudoField = new JTextField((String) table.getValueAt(selectedRow, 2));
            JTextField emailField = new JTextField(userEmail);
            JPasswordField passwordField = new JPasswordField();
            JTextField roleField = new JTextField((String) table.getValueAt(selectedRow, 4));

            Object[] message = {
                    "Nom:", nameField,
                    "Pseudo:", pseudoField,
                    "Email:", emailField,
                    "Mot de passe:", passwordField,
                    "Rôle:", roleField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Mettre à jour Utilisateur", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String pseudo = pseudoField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = roleField.getText();

                // Mettre à jour dans la base de données
                String sql = "UPDATE users SET lastName = ?, pseudo = ?, email = ?, password = SHA2(?, 256), role = ? WHERE id = ?";
                try (Connection conn = DbHelper.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2, pseudo);
                    ps.setString(3, email);
                    ps.setString(4, password);
                    ps.setString(5, role);
                    ps.setInt(6, userId);

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Utilisateur mis à jour !");
                    model.setValueAt(name, selectedRow, 1);
                    model.setValueAt(pseudo, selectedRow, 2);
                    model.setValueAt(email, selectedRow, 3);
                    model.setValueAt(role, selectedRow, 4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur à mettre à jour.");
        }
    }

    // Supprimer un utilisateur
    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) table.getValueAt(selectedRow, 0);
            String userEmail = (String) table.getValueAt(selectedRow, 3);

            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Voulez-vous vraiment supprimer l'utilisateur : " + userEmail + " ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM users WHERE id = ?";
                try (Connection conn = DbHelper.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Utilisateur supprimé !");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur à supprimer.");
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new UserListFrame().setVisible(true));
//    }
}