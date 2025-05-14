/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dao.ClientDAO;
import dao.VoitureDAO;
import dao.AchatDAO;
import ui.ClientUI;
import ui.VoitureUI;
import ui.AchatUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.*;

/**
 *
 * @author fenohery
 */
public class Acceuil extends JPanel {
    //private JPanel cardPanel;
    ClientDAO daocli = new ClientDAO();
    int nbCli = daocli.getNbClient();
    VoitureDAO daovoit = new VoitureDAO();
    int nbvoit = daovoit.nbStockVoiture();
    AchatDAO daoachat = new AchatDAO();
    int nbachat = daoachat.nbAchat();

    public Acceuil() {
        setLayout(new BorderLayout());
        JLabel titre = new JLabel("Bienvenue dans l'application de vente de voitures", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        add(titre, BorderLayout.NORTH);

        JButton btnCli = new JButton("Détails!");
        JButton btnVoit = new JButton("Détails!");
        JButton btnAchat = new JButton("Détails!");

        // Panel central avec statistiques
        JPanel statPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        statPanel.setBackground(Color.WHITE);

        statPanel.add(createStatCard(btnCli,"Nombre de Clients", ""+nbCli));
        statPanel.add(createStatCard(btnVoit, "Voitures en stock", ""+nbvoit));
        statPanel.add(createStatCard(btnAchat, "Total Achats", ""+nbachat));

        btnCli.addActionListener(e -> {
            JPanel clientPanel = new ClientUI();
            SwingUtilities.invokeLater(() -> showInMainPanel(clientPanel));
        });
        btnVoit.addActionListener(e -> {
            JPanel voiturePanel = new VoitureUI();
            SwingUtilities.invokeLater(() -> showInMainPanel(voiturePanel));
        });
        btnAchat.addActionListener(e -> {
            JPanel achatPanel = new AchatUI();
            SwingUtilities.invokeLater(() -> showInMainPanel(achatPanel));
        });

        add(statPanel, BorderLayout.CENTER);

        // Panel de raccourcis (bas)
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.WHITE);

        JButton btnActualiser = new JButton("Actualiser !");
        btnActualiser.addActionListener(e -> {
            JPanel acceuilPanel = new Acceuil();
            SwingUtilities.invokeLater(() -> showInMainPanel(acceuilPanel));
        });

        btnActualiser.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actionPanel.add(btnActualiser);

        add(actionPanel, BorderLayout.SOUTH);
    }

    // Méthode utilitaire pour créer une carte statistique
    private JPanel createStatCard(JButton btn, String label, String value) {
        btn.setVisible(true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setPreferredSize(new Dimension(150, 100));

        JLabel lblValue = new JLabel(value, JLabel.CENTER);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblValue.setForeground(new Color(60, 60, 60));

        JLabel lblLabel = new JLabel(label, JLabel.CENTER);
        lblLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblLabel.setForeground(new Color(100, 100, 100));

        panel.add(lblValue, BorderLayout.CENTER);
        panel.add(lblLabel, BorderLayout.SOUTH);
        panel.add(btn, BorderLayout.NORTH);

        return panel;
    }

    private void showInMainPanel(JPanel newPanel) {
        this.removeAll();
        this.add(newPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
