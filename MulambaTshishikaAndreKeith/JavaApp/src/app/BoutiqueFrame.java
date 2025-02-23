package app;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BoutiqueFrame extends JFrame {
    public BoutiqueFrame() {
        setTitle("Les Boutiques IStore");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Titre défilant
        JLabel titleLabel = new JLabel("Les Boutiques IStore", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Panel des boutons boutiques
        JPanel panel = new JPanel(new GridLayout(0, 3, 20, 20));
        panel.setBackground(Color.WHITE);

        List<Boutique> boutiques = getBoutiques();
        for (Boutique boutique : boutiques) {
            JButton button = createHexagonButton(boutique.getNom(), new Color(173, 216, 230)); // Bleu clair
            button.addActionListener(e -> {
                dispose();
                new ArticleFrame(boutique.getId(), boutique.getNom()).setVisible(true);
            });
            panel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);

        // Bouton Retour
        JButton backButton = new JButton("Retour");
        backButton.setFont(new Font("Arial", Font.BOLD, 30));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setPreferredSize(new Dimension(200, 100));
        backButton.addActionListener(e -> {
            dispose();
            new MainPage().setVisible(true);

        });

        JPanel backPanel = new JPanel();
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);
    }

    private JButton createHexagonButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int[] xPoints = {w / 4, 3 * w / 4, w, 3 * w / 4, w / 4, 0};
                int[] yPoints = {0, 0, h / 2, h, h, h / 2};

                g2.setColor(color);
                g2.fillPolygon(xPoints, yPoints, 6);
                g2.setColor(Color.BLACK);
                g2.drawPolygon(xPoints, yPoints, 6);

                // Dessin du texte
                g2.setFont(new Font("Arial", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(text)) / 2;
                int textY = (h + fm.getAscent()) / 2 - 10;
                g2.setColor(Color.BLACK);
                g2.drawString(text, textX, textY);
            }
        };

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 300));
        return button;
    }

    private List<Boutique> getBoutiques() {
        List<Boutique> boutiques = new ArrayList<>();
        try (Connection connection = dataBase.DbHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM boutique")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                boutiques.add(new Boutique(id, nom));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boutiques;
    }
}