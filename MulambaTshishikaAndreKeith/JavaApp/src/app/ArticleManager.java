package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import dataBase.DbHelper;

public class ArticleManager extends JFrame {
    private DefaultTableModel tableModel;
    private JTable articleTable;
    private Map<String, Integer> boutiqueMap; // Associe un nom de boutique à son ID
    private JTextField searchField; // Champ de recherche pour filtrer les articles

    public ArticleManager() {
        setTitle("Gestion des Articles");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // *Panel Haut avec Titre + Barre de recherche*
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 150, 243)); // Bleu comme dans l'exemple
        topPanel.setPreferredSize(new Dimension(getWidth(), 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Titre de la page
        JLabel titleLabel = new JLabel("Les Articles", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        // Champ de recherche pour filtrer les articles
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setToolTipText("Rechercher un article par nom");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterArticles(searchField.getText());
            }
        });

        // Panneau de recherche (à droite)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("🔍"));
        searchPanel.add(searchField);

        // Panneau du titre (à gauche)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Création de la table des articles
        String[] columns = {"ID", "Nom", "Prix", "Quantité en Stock", "Boutique"};
        tableModel = new DefaultTableModel(columns, 0);
        articleTable = new JTable(tableModel);
        articleTable.setFont(new Font("Arial", Font.PLAIN, 16));
        articleTable.setRowHeight(30);
        loadArticles();

        // Ajouter la table dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(articleTable);
        add(scrollPane, BorderLayout.CENTER);

        // Boutons d'action
        JButton btnAdd = new JButton("Ajouter Article");
        JButton btnUpdate = new JButton("Mettre à jour Article");
        JButton btnDelete = new JButton("Supprimer Article");
        JButton btnBack = new JButton("Retour");

        btnAdd.addActionListener(e -> addArticle());
        btnUpdate.addActionListener(e -> updateArticle());
        btnDelete.addActionListener(e -> deleteArticle());
        btnBack.addActionListener(e -> dispose());

        JPanel panel = new JPanel();
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnBack);

        add(panel, BorderLayout.SOUTH);
    }

    private void loadArticles() {
        tableModel.setRowCount(0);
        boutiqueMap = new HashMap<>();

        try (Connection conn = DbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT a.id, a.nom, a.prix, a.quantite_stock, b.nom AS boutique_nom FROM article a JOIN boutique b ON a.boutique_id = b.id")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        rs.getString("boutique_nom") // Récupère le nom de la boutique
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addArticle() {
        JTextField nomField = new JTextField();
        JTextField prixField = new JTextField();
        JTextField quantiteField = new JTextField();
        JComboBox<String> boutiqueComboBox = getBoutiqueComboBox();

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nom de l'Article:"));
        panel.add(nomField);
        panel.add(new JLabel("Prix:"));
        panel.add(prixField);
        panel.add(new JLabel("Quantité en Stock:"));
        panel.add(quantiteField);
        panel.add(new JLabel("Boutique:"));
        panel.add(boutiqueComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un Article", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DbHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO article (nom, prix, quantite_stock, boutique_id) VALUES (?, ?, ?, ?)")) {

                stmt.setString(1, nomField.getText());
                stmt.setDouble(2, Double.parseDouble(prixField.getText()));
                stmt.setInt(3, Integer.parseInt(quantiteField.getText()));
                stmt.setInt(4, boutiqueMap.get(boutiqueComboBox.getSelectedItem().toString()));

                stmt.executeUpdate();
                loadArticles();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateArticle() {
        int selectedRow = articleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article à mettre à jour.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) articleTable.getValueAt(selectedRow, 0);
        JTextField nomField = new JTextField((String) articleTable.getValueAt(selectedRow, 1));
        JTextField prixField = new JTextField(articleTable.getValueAt(selectedRow, 2).toString());
        JTextField quantiteField = new JTextField(articleTable.getValueAt(selectedRow, 3).toString());
        JComboBox<String> boutiqueComboBox = getBoutiqueComboBox();
        boutiqueComboBox.setSelectedItem(articleTable.getValueAt(selectedRow, 4).toString());

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nom de l'Article:"));
        panel.add(nomField);
        panel.add(new JLabel("Prix:"));
        panel.add(prixField);
        panel.add(new JLabel("Quantité en Stock:"));
        panel.add(quantiteField);
        panel.add(new JLabel("Boutique:"));
        panel.add(boutiqueComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Mettre à jour l'Article", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DbHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE article SET nom=?, prix=?, quantite_stock=?, boutique_id=? WHERE id=?")) {

                stmt.setString(1, nomField.getText());
                stmt.setDouble(2, Double.parseDouble(prixField.getText()));
                stmt.setInt(3, Integer.parseInt(quantiteField.getText()));
                stmt.setInt(4, boutiqueMap.get(boutiqueComboBox.getSelectedItem().toString()));
                stmt.setInt(5, id);

                stmt.executeUpdate();
                loadArticles();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteArticle() {
        int selectedRow = articleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) articleTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cet article ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DbHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM article WHERE id=?")) {

                stmt.setInt(1, id);
                stmt.executeUpdate();
                loadArticles();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private JComboBox<String> getBoutiqueComboBox() {
        JComboBox<String> boutiqueComboBox = new JComboBox<>();
        boutiqueMap.clear();

        try (Connection conn = DbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nom FROM boutique")) {

            while (rs.next()) {
                String nomBoutique = rs.getString("nom");
                int idBoutique = rs.getInt("id");
                boutiqueComboBox.addItem(nomBoutique);
                boutiqueMap.put(nomBoutique, idBoutique);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return boutiqueComboBox;
    }

    private void filterArticles(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        articleTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 1)); // Filtre sur le nom de l'article
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArticleManager().setVisible(true));
    }
}