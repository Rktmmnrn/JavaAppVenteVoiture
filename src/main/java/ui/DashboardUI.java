/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import ui.ClientUI;
import ui.VoitureUI;
import ui.AchatUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.LoadingDialog;

public class DashboardUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public DashboardUI() {
        setTitle("Gestion Vente Voiture");
        setSize(800, 550);
        setMinimumSize(new Dimension(600, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Menu boutons
        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton btnClient = new JButton("Client");
        JButton btnVoiture = new JButton("Voiture");
        JButton btnAchat = new JButton("Achat");

        // cree le style des btn
        btnClient = createStyledButton("Client");
        btnVoiture = createStyledButton("Voiture");
        btnAchat = createStyledButton("Achat");

        menuPanel.add(btnClient);
        menuPanel.add(btnVoiture);
        menuPanel.add(btnAchat);
        add(menuPanel, BorderLayout.WEST);

        // Panel central avec CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new ClientUI(), "client");
        cardPanel.add(new VoitureUI(), "voiture");
        cardPanel.add(new AchatUI(), "achat");

        add(cardPanel, BorderLayout.CENTER);

        // Action des boutons
        btnClient.addActionListener(e -> chargerAvecLoading("Chargement des clients...", () -> {
        JPanel clientPanel = new ClientUI();
        SwingUtilities.invokeLater(() -> showInMainPanel(clientPanel));
        }));
        btnVoiture.addActionListener(e -> chargerAvecLoading("Chargement des voitures...", () -> {
        JPanel voiturePanel = new VoitureUI();
        SwingUtilities.invokeLater(() -> showInMainPanel(voiturePanel));
        }));
        btnAchat.addActionListener(e -> chargerAvecLoading("Chargement des achats...", () -> {
        JPanel achatPanel = new AchatUI();
        SwingUtilities.invokeLater(() -> showInMainPanel(achatPanel));
        }));

        // === AJOUT AUX COMPOSANTS ===
        add(menuPanel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(45, 45, 45));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet survol (hover)
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(70, 70, 70));
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(45, 45, 45));
            }
        });

        return btn;
    }

    // Méthode pour mettre à jour le contenu central
    private void showInMainPanel(JPanel newPanel) {
        cardPanel.removeAll();
        cardPanel.add(newPanel, BorderLayout.CENTER);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void chargerAvecLoading(String message, Runnable action) {
        LoadingDialog loading = new LoadingDialog(this, message);

        // Affiche le loading dans le thread UI
        SwingUtilities.invokeLater(loading::showLoading);

        // Lance le traitement dans un thread séparé
        new Thread(() -> {
            try {
                // Exécute l’action passée (traitement ou affichage d’un panel)
                action.run();
            } finally {
                // Ferme le loading dans le thread UI
                SwingUtilities.invokeLater(loading::hideLoading);
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardUI::new);
    }
}
