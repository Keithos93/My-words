package app;

import javax.swing.*;
import java.awt.*;

public class CRUDFrame extends JFrame {
    public CRUDFrame() {
        setTitle("Gestion CRUD");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(2, 2, 40, 40));

        // Création des boutons avec nouvelle forme et couleurs pastel
        JButton btnEmployes = createHexagonButton("Gestion des Employés", new Color(255, 182, 193)); // Rose clair
        JButton btnBoutiques = createHexagonButton("Créer Boutique", new Color(173, 216, 230)); // Bleu clair
        JButton btnArticles = createHexagonButton("Gérer Articles", new Color(144, 238, 144)); // Vert clair
        JButton btnBack = new JButton("Retour"); // Bouton de retour classique

        btnEmployes.addActionListener(e -> new EmployeManager().setVisible(true));
        btnBoutiques.addActionListener(e -> new BoutiqueManager().setVisible(true));
        btnArticles.addActionListener(e -> new ArticleManager().setVisible(true));
        btnBack.addActionListener(e -> {
            dispose(); // Ferme la fenêtre actuelle
            new MainPage().setVisible(true); // Ouvre la MainPage
        });


        // Style du bouton retour
        btnBack.setFont(new Font("Arial", Font.BOLD, 30));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(Color.DARK_GRAY);
        btnBack.setPreferredSize(new Dimension(200, 100));

        // Ajout des boutons à la grille
        add(btnEmployes);
        add(btnBoutiques);
        add(btnArticles);
        add(btnBack);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CRUDFrame().setVisible(true));
    }
}