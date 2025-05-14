/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import ui.Acceuil;
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
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        JButton btnAcceuil = new JButton();
        JButton btnClient = new JButton();
        JButton btnVoiture = new JButton();
        JButton btnAchat = new JButton();
        JButton quit = new JButton();

        // cree le style des btn
        btnAcceuil = createStyledButton("Acceuil");
        btnClient = createStyledButton("CLi");
        btnVoiture = createStyledButton("VOIT");
        btnAchat = createStyledButton("ACHAT");
        quit = createStyledQuit("Quitter");

        menuPanel.add(btnAcceuil);
        menuPanel.add(btnClient);
        menuPanel.add(btnVoiture);
        menuPanel.add(btnAchat);
        menuPanel.add(quit);
        add(menuPanel, BorderLayout.WEST);

        // Panel central avec CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new Acceuil(), "Acceuil");
        cardPanel.add(new ClientUI(), "client");
        cardPanel.add(new VoitureUI(), "voiture");
        cardPanel.add(new AchatUI(), "achat");

        add(cardPanel, BorderLayout.CENTER);

        // Action des boutons
        btnAcceuil.addActionListener(e -> chargerAvecLoading("Chargement Acceuils...", () -> {
            JPanel acceuilPanel = new Acceuil();
            SwingUtilities.invokeLater(() -> showInMainPanel(acceuilPanel));
        }));
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
        // quitter l'application
        quit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                null,
                "Voulez-vous vraiment quitter l'application ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        add(menuPanel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(45, 45, 45));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet survol (hover)
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setForeground(new Color(45, 45, 45));
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(45, 45, 45));
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }
    private JButton createStyledQuit(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(Color.RED);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet survol (hover)
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setForeground(new Color(45, 45, 45));
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(Color.RED);
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    // mettre à jour le contenu central
    private void showInMainPanel(JPanel newPanel) {
        cardPanel.removeAll();
        cardPanel.add(newPanel, BorderLayout.CENTER);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // loading
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
