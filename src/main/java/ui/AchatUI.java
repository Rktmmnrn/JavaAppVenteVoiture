/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import model.Achat;
import model.Client;
import model.Voiture;
import dao.AchatDAO;
import dao.VoitureDAO;
import dao.ClientDAO;
import utils.EmailSender;
import utils.PDFGenerator;
import model.Facture;

import java.util.List;
import java.util.Date;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.demo.DateChooserPanel;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class AchatUI extends JPanel {
    private JComboBox<String> comboClient, comboVoiture;
    private JTextField txtQuantite;
    private JTextField txtNumAchat;
    private JButton btnAcheter;
    private JButton btnAnnuler;
    private JButton btnFilter;
    private JDateChooser Date1;
    private JDateChooser Date2;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnPDF;
    private JButton btnMail;
    private JDateChooser DateC; // Date
    private String idAchatSelectionne = null;
    private JButton btnRecette;
    private Boolean enModif = false;

    private AchatDAO achatDAO = new AchatDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private VoitureDAO voitureDAO = new VoitureDAO();

    public AchatUI() {
        setLayout(new BorderLayout());
        //add(new JLabel("Vue Achat", SwingConstants.CENTER), BorderLayout.CENTER);

        initComponents();
        tableauAchat();
        chargeAchat();
    }
    
    private void initComponents() {
        comboClient = new JComboBox<>();
        comboVoiture = new JComboBox<>();
        txtQuantite = new JTextField();
        txtNumAchat = new JTextField();
        DateC = new JDateChooser();
        DateC.setDateFormatString("yyyy-MM-dd");
        btnAcheter = createButton("Acheter", new Color(46, 204, 113));
        btnAnnuler = createButton("Annuler", new Color(231, 76, 60));
        btnPDF = createButton("PDF", Color.DARK_GRAY);
        btnPDF.setEnabled(false);
        btnMail = createButton("Envoyer email", new Color(52, 152, 219));
        btnFilter = createButton("Filtrer", new Color(155, 89, 182));
        Date1 = new JDateChooser();
        Date1.setDateFormatString("yyyy-MM-dd");
        Date2 = new JDateChooser();
        Date2.setDateFormatString("yyyy-MM-dd");
        btnRecette = createButton("Recette", new Color(241, 196, 15));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Formulaire d'Achat"));

        formPanel.add(new JLabel("NumAchat"));
        formPanel.add(txtNumAchat);
        formPanel.add(new JLabel("Client :"));
        formPanel.add(comboClient);
        formPanel.add(new JLabel("Voiture :"));
        formPanel.add(comboVoiture);
        formPanel.add(new JLabel("Date :"));
        formPanel.add(DateC);
        formPanel.add(new JLabel("Quantité :"));
        formPanel.add(txtQuantite);
        formPanel.add(btnAcheter);
        formPanel.add(btnAnnuler);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setBorder(new TitledBorder("Actions"));
        actionsPanel.add(btnPDF);
        actionsPanel.add(btnMail);
        actionsPanel.add(btnRecette);

        JPanel filtrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtrePanel.setBorder(new TitledBorder("Filtrer les achats par date"));
        filtrePanel.add(new JLabel("Du :"));
        filtrePanel.add(Date1);
        filtrePanel.add(new JLabel("au :"));
        filtrePanel.add(Date2);
        filtrePanel.add(btnFilter);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(formPanel);
        northPanel.add(actionsPanel);
        northPanel.add(filtrePanel);

        add(northPanel, BorderLayout.NORTH);

        chargerClient();
        chargerVoiture();

        btnAcheter.addActionListener(e -> effectuerAchat());
        btnAnnuler.addActionListener(e -> reinitialiser());
        btnPDF.addActionListener(e -> genererPdf());
        btnMail.addActionListener(e -> envoyerMail());
        btnFilter.addActionListener(e -> {
            if(Date1.getDate() == null || Date2.getDate() == null) {
                chargeAchat();
                return;
            }
            filtrageAchat(Date1.getDate(), Date2.getDate());
        });
        btnRecette.addActionListener(e -> afficherRecettes());
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private void chargerClient() {
        List<Client> clients = clientDAO.getAllClient();
        for (Client c : clients) {
            comboClient.addItem(c.getIdcli());
        }
    }

    private void chargerVoiture() {
        List<Voiture> voiture = voitureDAO.getAllVoiture();
        for (Voiture v : voiture) {
            comboVoiture.addItem(v.getIdvoit());
        }
    }
    
    private void chargeAchat() {
        tableModel.setRowCount(0);
        List<Achat> achat = new AchatDAO().getAllAchat();

        for (Achat a : achat) {
            tableModel.addRow(new Object[] { a.getNumachat(), a.getIdcli(), a.getIdvoit(), a.getDate(), a.getQte()});
        }
    }

    private void effectuerAchat() {
        try {
            String numachat = txtNumAchat.getText();
            String idcli = (String) comboClient.getSelectedItem();
            String idvoit = (String) comboVoiture.getSelectedItem();
            int qte = Integer.parseInt(txtQuantite.getText());
            Date Date = DateC.getDate();

            if (numachat.isEmpty() || idcli == null || idvoit == null || txtQuantite.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            if (Date == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date !");
                return;
            }

            // Conversion Date -> LocalDate
            Instant instant = Date.toInstant();
            LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            // Vérifier le stock
            Voiture voiture = voitureDAO.getVoitureById(idvoit);
            if (voiture != null) {
                if (voiture.getNombre() >= qte) {
                    if(!enModif) {
                        Achat achat = new Achat(numachat, idcli, idvoit, date, qte);
                        achatDAO.ajoutAchat(achat);

                        voiture.setNombre(voiture.getNombre() - qte);
                        voitureDAO.updateNombre(voiture);

                        JOptionPane.showMessageDialog(this, "Achat réussi !");
                        txtQuantite.setText("");
                    } else {
                        Achat achat = new Achat(idAchatSelectionne, idcli, idvoit, date, qte);
                        new AchatDAO().modifAchat(achat);
                        JOptionPane.showMessageDialog(this, "Achat Modifier !");
                        btnAcheter.setText("Acheter");
                        enModif=false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Stock insuffisant !");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Voiture introuvable !");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantité invalide !");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }

        chargeAchat();
        txtNumAchat.setText("");
        comboClient.setSelectedItem("");
        comboVoiture.setSelectedItem("");
        DateC.setDate(null);
        txtQuantite.setText("");
    }

    private void tableauAchat() {
        String[] col = {"N°_Achat", "idClient", "idVoiture", "date", "qte"};
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
                    String numachat = table.getValueAt(row, 0).toString();
                    String idcli = table.getValueAt(row, 1).toString();
                    String idvoit = table.getValueAt(row, 2).toString();
                    String date = table.getValueAt(row, 3).toString();
                    String qte = table.getValueAt(row, 4).toString();
                    System.out.println("ID sélectionné : " + idvoit);

                    txtNumAchat.setText(numachat);
                    comboClient.setSelectedItem(idcli);
                    comboVoiture.setSelectedItem(idvoit);
                    btnPDF.setEnabled(true); // met PDF actif
                    try{
                        java.util.Date d = java.sql.Date.valueOf(date); 
                        DateC.setDate(d);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    txtQuantite.setText(qte);
                    enModif = true;
                    idAchatSelectionne = numachat; // numachat selectionner
                    btnAcheter.setText("Modifier");
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);
    }

    private void reinitialiser() { // reinitialisation
        txtNumAchat.setText("");
        comboClient.setSelectedItem("");
        comboVoiture.setSelectedItem("");
        DateC.setDate(null);
        txtQuantite.setText("");
        btnAcheter.setText("Acheter");
        //enModif = false;
        idAchatSelectionne = null;
    }

    private void genererPdf() {
        String numachat = idAchatSelectionne;

        if (idAchatSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un achat !");
            return;
        }

        try {
            PDFGenerator.genPDF(numachat);
            JOptionPane.showMessageDialog(this, "PDF généré avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur génération du PDF : " + e.getMessage());
        }
    }

    private void envoyerMail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un achat.");
            return;
        }

        String numachat = table.getValueAt(row, 0).toString();
        String idcli = table.getValueAt(row, 1).toString();
        String idvoit = table.getValueAt(row, 2).toString();
        String date = table.getValueAt(row, 3).toString();
        String qte = table.getValueAt(row, 4).toString();

        String emailClient = achatDAO.getEmail(numachat);

        int conf = JOptionPane.showConfirmDialog(this, "Envoyer un email a '"+emailClient+"' !", "Confirmation", JOptionPane.YES_NO_OPTION);
        if(conf == JOptionPane.YES_OPTION) {
            if (emailClient != null && !emailClient.trim().isEmpty()) {
                String sujet = "Facture Achat N°" + numachat;
                String corps = "Bonjour, \n\nVoici le détail de votre achat :\n" +
                        "Numéro Achat : " + numachat + "\n" +
                        "ID Client : " + idcli + "\n" +
                        "ID Voiture : " + idvoit + "\n" +
                        "Date : " + date + "\n" +
                        "Quantité : " + qte + "\n\nMerci pour votre confiance !";

                try {
                    EmailSender.envoyerEmail(emailClient, sujet, corps);
                    JOptionPane.showMessageDialog(this, "Email envoyer avec succès");
                } catch(Exception e) {
                    JOptionPane.showMessageDialog(this, "Email non envoyer !! "+e);
                }
            }   
        }
    }

    private void filtrageAchat(Date datedeb, Date datefin) {
        tableModel.setRowCount(0);
        LocalDate date1 = datedeb.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = datefin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Achat> achat = new AchatDAO().searchAchat(date1, date2);

        for(Achat a : achat) {
            tableModel.addRow(new Object[] {a.getNumachat(), a.getIdcli(), a.getIdvoit(), a.getDate(), a.getQte()});
        }
    }

    public void afficherRecettes() { // recette accumulé
        JPanel contenuPanel = new JPanel();
        contenuPanel.setLayout(new BoxLayout(contenuPanel, BoxLayout.Y_AXIS));

        // TABLEAU DES RECETTES
        String[] columns = {"Date", "Montant"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableRecette = new JTable(model);
        JScrollPane scrollTable = new JScrollPane(tableRecette);
        scrollTable.setPreferredSize(new Dimension(600, 200));

        contenuPanel.add(scrollTable); // ajout du scrollTable à la panel

        // Remplissage tableau
        Map<YearMonth, Double> recettes = new AchatDAO().getRecette();
        for (Map.Entry<YearMonth, Double> entry : recettes.entrySet()) {
            String mois = entry.getKey().format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH));
            model.addRow(new Object[]{mois, entry.getValue()});
        }

        // JFREECHART
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<YearMonth, Double> entry : recettes.entrySet()) {
            String mois = entry.getKey().format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH));
            dataset.addValue(entry.getValue(), "Recette", mois);
        }

        // CRÉATION DU GRAPHIQUE
        JFreeChart chart = ChartFactory.createBarChart(
            "représentation graphique",  // titre
            "Mois",                         // axe des X
            "Montant",                      // axe des Y
            dataset,
            PlotOrientation.VERTICAL,
            true,                          // légende
            true,                           // tooltips
            false                           // URLs
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        contenuPanel.add(chartPanel); // ajout du graphique à la panel

        // AJOUT DU PANEL À LA FENÊTRE
        this.setLayout(new BorderLayout());
        this.add(contenuPanel, BorderLayout.CENTER);

        JFrame jframe = new JFrame("RECETTE");
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jframe.add(contenuPanel); // contenu panel qui va contenir les 2 tables (graphique et non graphique)

        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
    }
}
