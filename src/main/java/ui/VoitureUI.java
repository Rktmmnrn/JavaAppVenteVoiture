/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import model.Client;
import dao.ClientDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class VoitureUI extends JFrame {
    private JTable table;

    public VoitureUI() {
        setTitle("liste des Voitures");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        
    }
}
