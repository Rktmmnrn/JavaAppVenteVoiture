/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Client;
import utils.Connectiondb; // Connection a la bd

import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

public class ClientDAO {
    public void ajouterClient(Client client) {
        String sql = "INSERT INTO Client (idcli, nom, email) VALUES (?, ?, ?)";
        
        try (Connection conn = Connectiondb.getconnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, client.getIdcli());
            pst.setString(2, client.getNom());
            pst.setString(3, client.getEmail());

            pst.executeUpdate();
            System.out.println("Client ajouter succés..");
        } catch (SQLException e) {
            System.out.println("Erreur ajout Client :"+e.getMessage());
        }
    }
    
    public List<Client> getAllClient() {
        List<Client> client = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (Connection conn = Connectiondb.getconnection();
              Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client c = new Client(
                        rs.getString("idcli"),
                        rs.getString("nom"),
                        rs.getString("email")
                );
                client.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur d'affichage :"+e.getMessage());
        }
        return client;
    }

    public void modifClient(Client client) {
        try(Connection conn = Connectiondb.getconnection()) {
            String sql = "UPDATE client SET nom=?, email=? WHERE idcli=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getNom());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getIdcli());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprClient(String idcli) {
        String sql = "DELETE FROM Client WHERE idcli = ?";

        try (Connection conn = Connectiondb.getconnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idcli);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "CLient '"+idcli+"' suprimer avec succès");
        } catch (SQLException e) {
                if (e.getSQLState().equals("23503")) { // Code PostgreSQL pour violation de clé étrangère
                JOptionPane.showMessageDialog(null,
                    "Impossible de supprimer cette client car elle est liée à un ou plusieurs achats.",
                    "Suppression refusée",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                e.printStackTrace(); // autre erreur SQL
                JOptionPane.showMessageDialog(null,
                    "Erreur lors de la suppression : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public int getNbClient() {
        int total=0;
        String sql = "SELECT COUNT(*) FROM Client";
        try(Connection conn = Connectiondb.getconnection();
                PreparedStatement prpr = conn.prepareStatement(sql);
                ResultSet rs = prpr.executeQuery()) {
            if(rs.next()) {
                total = rs.getInt(1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return total;
    }
}
