/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import com.toedter.calendar.JDateChooser;
import model.Voiture;
import dao.VoitureDAO;

import java.util.List;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class VoitureUI extends JPanel {
    private JTextField txtId;
    private JTextField txtDesign;
    private JTextField txtPrix;
    private JTextField txtNombre;
    private JTextField search;
    private JButton btnSearch;
    private JButton btnAjouter;
    private JButton btnSuppr;
    private JTable table;
    private DefaultTableModel tableModel;
    private Boolean enModif = false;
    private String idVoitureSelectionne = null;
    private JButton btnAnnuler;
    private JPanel jpv;
    private JPanel panelVoitureNV;

    private JDateChooser Datedeb;
    private JDateChooser Datefin;
    private JButton btnFilter;
    private JTable tableVoitureNV;
    private DefaultTableModel tableModelVoitureNV;

    public VoitureUI() {
        setLayout(new BorderLayout());
        JPanel centrePanel = new JPanel();
        add(new JLabel("Vue Voiture", SwingConstants.CENTER), BorderLayout.CENTER);

        initComponents();
        tableauVoiture();
        chargeVoiture();
        voitureNonVendue();

        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));
        centrePanel.add(jpv);
        centrePanel.add(Box.createVerticalStrut(10)); // petit espace
        centrePanel.add(panelVoitureNV);
        add(centrePanel, BorderLayout.CENTER);
    }

    private void reinitialiserFormulaire() { // reinitialisation
        txtId.setText("");
        txtDesign.setText("");
        txtPrix.setText("");
        txtNombre.setText("");
        btnAjouter.setText("Ajouter");
        enModif = false;
        idVoitureSelectionne = null;
    }

    private void chargeVoiture() {
        tableModel.setRowCount(0);
        List<Voiture> voiture = new VoitureDAO().getAllVoiture();

        for (Voiture v : voiture) {
            tableModel.addRow(new Object[] { v.getIdvoit(), v.getDesign(), v.getPrix(), v.getNombre()});
        }
    }

    private void initComponents() { // initialisation de tous les composants
        txtId = new JTextField(20);
        txtDesign = new JTextField(20);
        txtPrix = new JTextField(20);
        txtNombre = new JTextField(20);
        btnAjouter = createButton("Ajouter", new Color(46, 204, 113));
        btnSuppr = createButton("Supprimer", new Color(231, 76, 60));
        btnAnnuler = createButton("Annuler", new Color(52, 152, 219));
        search = new JTextField(20);
        btnSearch = new JButton("Rechercher");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 4, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panel.add(new JLabel("ID Voiture:"));
        panel.add(txtId);
        panel.add(new JLabel("Design :"));
        panel.add(txtDesign);
        panel.add(new JLabel("Prix :"));
        panel.add(txtPrix);
        panel.add(new JLabel("Nombre :"));
        panel.add(txtNombre);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnAjouter);
        panel.add(btnSuppr);
        panel.add(btnAnnuler);
        panel.add(search);
        panel.add(btnSearch);

        btnAjouter.addActionListener(e -> {
            btnAjouter(e);
                }); // Ajout des données dans le bd
        
        btnSuppr.addActionListener(e -> {
            supprVoiture(); // suppression d'un Voiture
        });

        btnAnnuler.addActionListener(e -> { // annuler modif
            reinitialiserFormulaire();
        });

        btnSearch.addActionListener(e -> { // boutton recherche
            String motcle = search.getText().trim();
            if(!motcle.isEmpty()) {
                searchVoiture(motcle);
            } else {
                chargeVoiture();
            }
        });
        add(panel, BorderLayout.NORTH); // formulaire d'ajout
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private void btnAjouter(java.awt.event.ActionEvent evt) {
        String id = txtId.getText();
        String design = txtDesign.getText();
        int prix = Integer.parseInt(txtPrix.getText());
        int nombre = Integer.parseInt(txtNombre.getText());

        if (id.isEmpty() || design.isEmpty() || txtPrix.getText().isEmpty() || txtNombre.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
        return; }

        if(enModif) {
            Voiture voiture = new Voiture(idVoitureSelectionne, design, prix, nombre);
            new VoitureDAO().modifVoiture(voiture);
            JOptionPane.showMessageDialog(this, "Voiture Modifier !");
            enModif = false;
            btnAjouter.setText("Ajouter");
        } else {
            Voiture voiture = new Voiture(id, design, prix, nombre);
            new VoitureDAO().ajouterVoiture(voiture);
            JOptionPane.showMessageDialog(this, "Voiture Ajouter !");
        }

        chargeVoiture(); // mettre à jour le tableau
        txtId.setText("");
        txtDesign.setText("");
        txtPrix.setText("");
        txtNombre.setText("");
    }

    private void supprVoiture() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "veuillez selectionner un Voiture à supprimer !");
            return;
        }

        String idvoit = table.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer la voiture "+idvoit+" ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm ==  JOptionPane.YES_OPTION) {
            new VoitureDAO().supprVoiture(idvoit);
            chargeVoiture(); // maj du tableau
            reinitialiserFormulaire(); // reinitialisation formulaire
        }
    }

    private void tableauVoiture() { // tableau des voitures
        String[] col = {"Voiture_N°", "Design", "Prix", "Nombres"};
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
                    String idvoit = table.getValueAt(row, 0).toString();
                    String design = table.getValueAt(row, 1).toString();
                    String prix = table.getValueAt(row, 2).toString();
                    String nombre = table.getValueAt(row, 3).toString();
                    System.out.println("ID sélectionné : " + idvoit);

                    txtId.setText(idvoit);
                    txtDesign.setText(design);
                    txtPrix.setText(prix);
                    txtNombre.setText(nombre);
                    enModif = true;
                    idVoitureSelectionne = idvoit;
                    btnAjouter.setText("Modifier");
                }
            }
        });

        JPanel jpv = new JPanel(new BorderLayout());
        JScrollPane sp = new JScrollPane(table);
        jpv.add(sp, BorderLayout.CENTER);
        this.jpv = jpv;
    }

    private void searchVoiture(String motcle) { // recherche Voiture
        tableModel.setRowCount(0); // vider la table
        List<Voiture> v = new VoitureDAO().searchVoiture(motcle);
        for(Voiture vt : v) {
            tableModel.addRow(new Object[] {vt.getIdvoit(), vt.getDesign(), vt.getPrix(), vt.getNombre()});
        }
    }

    private void voitureNonVendue() { // voiture non vendu
        Datedeb = new JDateChooser();
        Datedeb.setDateFormatString("yyyy-MM-dd");
        Datefin = new JDateChooser();
        Datefin.setDateFormatString("yyyy-MM-dd");
        btnFilter = new JButton("Filtrer");

        JPanel panv = new JPanel();
        panv.setLayout(new GridLayout(4, 2, 5, 5));
        panv.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panv.add(new JLabel("Date début"));
        panv.add(Datedeb);
        panv.add(new JLabel("Date fin"));
        panv.add(Datefin);
        panv.add(btnFilter);

        btnFilter.addActionListener(e -> {
            Date dateD = Datedeb.getDate();
            Date dateF = Datefin.getDate();
            if(dateD==null || dateF==null) {
                chargeVoitureNV();
                System.out.println("aucune date entrer !!");
                JOptionPane.showMessageDialog(this, "Aucune date entrer !");
                return;
            }
            filterVoitureNV(dateD, dateF);
        });

        add(panv, BorderLayout.SOUTH); // ici
        tableauVoitureNV();
        chargeVoitureNV();
    }

    private void tableauVoitureNV() {
        String[] colonne = {"Voiture_N°", "Design", "Prix", "Nombres"};
        tableModelVoitureNV = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableVoitureNV = new JTable(tableModelVoitureNV);

        JScrollPane spp = new JScrollPane(tableVoitureNV); // ici aussii
        panelVoitureNV = new JPanel(new BorderLayout());
        panelVoitureNV.setBorder(BorderFactory.createTitledBorder("Voitures non vendues"));
        panelVoitureNV.add(spp, BorderLayout.CENTER);
        this.panelVoitureNV = panelVoitureNV;
    }

    private void chargeVoitureNV() {
        tableModelVoitureNV.setRowCount(0);
        List<Voiture> voiture = new VoitureDAO().filtrerVoiture();

        for (Voiture v : voiture) {
            tableModelVoitureNV.addRow(new Object[] { v.getIdvoit(), v.getDesign(), v.getPrix(), v.getNombre()});
        }
    }

    private void filterVoitureNV(Date deb, Date fin) {
        tableModelVoitureNV.setRowCount(0);
        LocalDate date1 = deb.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Voiture> voiture = new VoitureDAO().filtrerVoitureWithDate(date1, date2);

        for(Voiture v : voiture) {
            tableModelVoitureNV.addRow(new Object[] {v.getIdvoit(), v.getDesign(), v.getPrix(), v.getNombre()});
        }
    }
}
