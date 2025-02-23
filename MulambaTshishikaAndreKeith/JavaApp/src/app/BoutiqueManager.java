package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BoutiqueManager extends JFrame {
    private DefaultTableModel tableModel;
    private JTable boutiqueTable;

    public BoutiqueManager() {
        setTitle("Gestion des Boutiques");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tableau des boutiques
        String[] columns = {"ID", "Nom", "Date de Création"};
        tableModel = new DefaultTableModel(columns, 0);
        boutiqueTable = new JTable(tableModel);
        loadBoutiques();

        // Boutons d'action
        JButton btnAdd = new JButton("Créer Boutique");
        JButton btnUpdate = new JButton("Mettre à jour Boutique");
        JButton btnDelete = new JButton("Supprimer Boutique");
        JButton btnBack = new JButton("Retour");

        btnAdd.addActionListener(e -> addBoutique());
        btnUpdate.addActionListener(e -> updateBoutique());
        btnDelete.addActionListener(e -> deleteBoutique());
        btnBack.addActionListener(e -> dispose());

        JPanel panel = new JPanel();
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnBack);

        add(new JScrollPane(boutiqueTable), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    private void loadBoutiques() {
        tableModel.setRowCount(0);
        try (Connection conn = dataBase.DbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM boutique")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDate("date_creation")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBoutique() {
        JTextField nomField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Nom de la Boutique:"));
        panel.add(nomField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Créer une Boutique", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = dataBase.DbHelper.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO boutique (nom, date_creation) VALUES (?, NOW())")) {

                stmt.setString(1, nomField.getText());
                stmt.executeUpdate();
                loadBoutiques();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBoutique() {
        int selectedRow = boutiqueTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) boutiqueTable.getValueAt(selectedRow, 0);
            JTextField nomField = new JTextField((String) boutiqueTable.getValueAt(selectedRow, 1));

            JPanel panel = new JPanel(new GridLayout(1, 2));
            panel.add(new JLabel("Nom de la Boutique:"));
            panel.add(nomField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Mettre à jour", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = dataBase.DbHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("UPDATE boutique SET nom=? WHERE id=?")) {

                    stmt.setString(1, nomField.getText());
                    stmt.setInt(2, id);
                    stmt.executeUpdate();
                    loadBoutiques();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteBoutique() {
        int selectedRow = boutiqueTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) boutiqueTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette boutique ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = dataBase.DbHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM boutique WHERE id=?")) {

                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    loadBoutiques();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
  }
}

}