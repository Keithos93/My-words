package app;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArticleFrame extends JFrame {
    public ArticleFrame(int boutiqueId, String boutiqueNom) {
        setTitle("Articles de " + boutiqueNom);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel("Articles de " + boutiqueNom, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        add(titleLabel, BorderLayout.NORTH);

        // Récupération des articles
        String[] columnNames = {"Nom", "Prix (€)", "Quantité en Stock"};
        List<String[]> data = getArticles(boutiqueId);
        String[][] rowData = data.toArray(new String[0][]);

        // Création du tableau
        JTable table = new JTable(rowData, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 20));
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bouton Retour
        JButton backButton = new JButton("Retour");
        backButton.setFont(new Font("Arial", Font.BOLD, 30));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setPreferredSize(new Dimension(250, 100));
        backButton.addActionListener(e -> {
            dispose();
            new BoutiqueFrame().setVisible(true);
        });

        add(backButton, BorderLayout.SOUTH);
    }

    private List<String[]> getArticles(int boutiqueId) {
        List<String[]> articles = new ArrayList<>();
        String sql = "SELECT nom, prix, quantite_stock FROM article WHERE boutique_id = ?";

        try (Connection connection = dataBase.DbHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, boutiqueId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prix = resultSet.getString("prix") + "€";
                String quantite = String.valueOf(resultSet.getInt("quantite_stock"));
                articles.add(new String[]{nom, prix, quantite});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }
}