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
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class ClientUI extends JPanel {
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
        setLayout(new BorderLayout());
        add(new JLabel("Vue Client", SwingConstants.CENTER), BorderLayout.CENTER);

        initComponents();
        tableauClient();
        chargeClient();
    }

    private void reinitialiserFormulaire() { // reinitialisation
    txtId.setText("");
    txtNom.setText("");
    txtEmail.setText("");
    btnAjouter.setText("Ajouter");
    enModif = false;
    idClientSelectionne = null;
    }

    private void initComponents() {
        txtId = new JTextField();
        txtNom = new JTextField();
        txtEmail = new JTextField();
        btnAjouter = createButton("Ajouter", new Color(46, 204, 113));
        btnSuppr = createButton("Supprimer", new Color(231, 76, 60));
        btnAnnuler = createButton("Annuler", new Color(52, 152, 219));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Formulaire Client"));

        formPanel.add(new JLabel("ID Client:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(txtNom);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(btnAjouter);
        formPanel.add(btnSuppr);
        formPanel.add(btnAnnuler);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setBorder(new TitledBorder("Actions"));
        actionsPanel.add(btnAjouter);
        actionsPanel.add(btnSuppr);
        actionsPanel.add(btnAnnuler);

        add(formPanel, BorderLayout.NORTH);
        add(actionsPanel, BorderLayout.SOUTH);

        btnAjouter.addActionListener(e -> btnAjouter(e));
        btnSuppr.addActionListener(e -> supprClient());
        btnAnnuler.addActionListener(e -> reinitialiserFormulaire());
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        return btn;
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

    private void chargeClient() { // charger la liste des Clients
        tableModel.setRowCount(0);
        List<Client> client = new ClientDAO().getAllClient();

        for (Client c : client) {
            tableModel.addRow(new Object[] { c.getIdcli(), c.getNom(), c.getEmail() });
        }
    }

    private void btnAjouter(java.awt.event.ActionEvent evt) { // boutton ajouter
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

    private void supprClient() { // supprimer Client
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
