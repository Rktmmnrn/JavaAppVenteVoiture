/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import model.Client;        
import dao.ClientDAO;

import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.util.List;
import java.awt.event.*;
import javax.swing.table.*;

public class ClientUI extends javax.swing.JFrame {
    private JTextField txtId;
    private JTextField txtNom;
    private JTextField txtEmail;
    private JButton btnAjouter;
    private JButton btnSuppr;
    private JTable table;
    private DefaultTableModel tableModel;
    private Boolean enModif = false;
    private String idClientSelectionne = null;
    private JButton btnAnnuler;

    public ClientUI() { // Constructeur
        setTitle("Ajout Client");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setMinimumSize(new Dimension(500,400)); // taille minimum
        setLocationRelativeTo(null); // centre la fenêtre

        /*addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!table.getBounds().contains(e.getPoint())) {
                    reinitialiserFormulaire();
                }
            }
        });*/

        initComponents();
        tableauClient();
        chargeClient();
        setVisible(true);
    }

    private void reinitialiserFormulaire() { // reinitialisation
    txtId.setText("");
    txtNom.setText("");
    txtEmail.setText("");
    btnAjouter.setText("Ajouter");
    enModif = false;
    idClientSelectionne = null;
    }

    private void initComponents() { // initialisation de tous les composants
        txtId = new JTextField(20);
        txtNom = new JTextField(20);
        txtEmail = new JTextField(20);
        btnAjouter = new JButton("Ajouter");
        btnSuppr = new JButton("Supprimer");
        btnAnnuler = new JButton("Annuler");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 4, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panel.add(new JLabel("ID Client :"));
        panel.add(txtId);
        panel.add(new JLabel("Nom :"));
        panel.add(txtNom);
        panel.add(new JLabel("Email :"));
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnAjouter);
        panel.add(btnSuppr);
        panel.add(btnAnnuler);

        btnAjouter.addActionListener(e -> {
            btnAjouter(e);
                }); // Ajout des données dans le bd
        
        btnSuppr.addActionListener(e -> {
            supprClient(); // suppression d'un Client
        });

        btnAnnuler.addActionListener(e -> { // annuler modif
            reinitialiserFormulaire();
        });

        add(panel, BorderLayout.NORTH);
    }

    private void tableauClient() { // tableau Clients
        String[] col = {"Idclient", "Nom", "Email"};
        tableModel = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.addMouseListener(new java.awt.event.MouseAdapter() { // ecouteur d'evennement click
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String idcli = table.getValueAt(row, 0).toString();
                    String nom = table.getValueAt(row, 1).toString();
                    String email = table.getValueAt(row, 2).toString();
                    System.out.println("ID sélectionné : " + idcli);

                    txtId.setText(idcli);
                    txtNom.setText(nom);
                    txtEmail.setText(email);
                    enModif = true;
                    idClientSelectionne = idcli;
                    btnAjouter.setText("Modifier");
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);
    }

    private void chargeClient() {
        tableModel.setRowCount(0);
        List<Client> client = new ClientDAO().getAllClient();

        for (Client c : client) {
            tableModel.addRow(new Object[] { c.getIdcli(), c.getNom(), c.getEmail() });
        }
    }

    private void btnAjouter(java.awt.event.ActionEvent evt) {
            String id = txtId.getText();
            String nom = txtNom.getText();
            String email = txtEmail.getText();

            if (id.isEmpty() || nom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return; }

            if(enModif) {
                Client client = new Client(idClientSelectionne, nom, email);
                new ClientDAO().modifClient(client);
                JOptionPane.showMessageDialog(this, "Client Modifier !");
                enModif = false;
                btnAjouter.setText("Ajouter");
            } else {
                Client client = new Client(id, nom, email);
                new ClientDAO().ajouterClient(client);
                JOptionPane.showMessageDialog(this, "Client Ajouter !");
            }

            chargeClient(); // mettre à jour le tableau
            txtId.setText("");
            txtNom.setText("");
            txtEmail.setText("");
    }

    private void supprClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "veuillez selectionner un Client à supprimer !");
            return;
        }

        String idcli = table.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le Client "+idcli+" ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm ==  JOptionPane.YES_OPTION) {
            new ClientDAO().supprClient(idcli);
            chargeClient(); // maj du tableau
            reinitialiserFormulaire(); // reinitialisation formulaire
        }
    }
}
