/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Voiture;
import utils.Connectiondb; // Connection a la bd

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.JOptionPane;

public class VoitureDAO {
    public void ajouterVoiture(Voiture voiture) {
        String sql = "INSERT INTO voiture (idvoit, design, prix, nombre) VALUES (?, ?, ?, ?)";

        try (Connection conn = Connectiondb.getconnection();
                PreparedStatement prp = conn.prepareStatement(sql)) {
            prp.setString(1, voiture.getIdvoit());
            prp.setString(2, voiture.getDesign());
            prp.setInt(3, voiture.getPrix());
            prp.setInt(4, voiture.getNombre());

            prp.executeUpdate();
            System.out.println("Voiture ajouter succés..");
        } catch (SQLException e) {
            System.out.println("Erreur ajout Voiture :"+e.getMessage());
        }
    }

    public List<Voiture> getAllVoiture() {
        List<Voiture> voiture = new ArrayList<>();
        String sql = "SELECT * FROM voiture";

        try (Connection conn = Connectiondb.getconnection();
              Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Voiture v = new Voiture(
                        rs.getString("idvoit"),
                        rs.getString("design"),
                        rs.getInt("prix"),
                        rs.getInt("nombre")
                );
               voiture.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur d'affichage :"+e.getMessage());
        }
        return voiture;
    }

    public void modifVoiture(Voiture voiture) {
        try(Connection conn = Connectiondb.getconnection()) {
            String sql = "UPDATE voiture SET design=?, prix=?, nombre=? WHERE idvoit=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, voiture.getDesign());
            ps.setInt(2, voiture.getPrix());
            ps.setInt(3, voiture.getNombre());
            ps.setString(4, voiture.getIdvoit());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprVoiture(String idvoit) {
        String sql = "DELETE FROM voiture WHERE idvoit = ?";

        try (Connection conn = Connectiondb.getconnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idvoit);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Voiture "+idvoit+" Supprimer !");
        } catch (SQLException e) {
                if (e.getSQLState().equals("23503")) { // Code PostgreSQL pour violation de clé étrangère
                JOptionPane.showMessageDialog(null,
                    "Impossible de supprimer cette voiture car elle est liée à un ou plusieurs achats.",
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

    public List<Voiture> searchVoiture(String motCle) {
        List<Voiture> voiture = new ArrayList<>();
        String sql = "SELECT * FROM voiture WHERE idvoit LIKE ? OR design LIKE ?";
        try(Connection conn = Connectiondb.getconnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%"+motCle+"%");
            ps.setString(2, "%"+motCle+"%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Voiture v = new Voiture(
                        rs.getString("idvoit"),
                        rs.getString("design"),
                        rs.getInt("prix"),
                        rs.getInt("nombre")
                );
                voiture.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voiture;
    }

    public Voiture getVoitureById(String idVoit) {
        Voiture v = null;
        try {
            Connection conn = Connectiondb.getconnection();
            String sql = "SELECT * FROM voiture WHERE idvoit=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idVoit);
            ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            v = new Voiture();
            v.setIdvoit(rs.getString("idvoit"));
            v.setDesign(rs.getString("design"));
            v.setPrix(rs.getInt("prix"));
            v.setNombre(rs.getInt("nombre"));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public void updateNombre(Voiture voiture) {
        try {
            Connection conn = Connectiondb.getconnection();
            String sql = "UPDATE voiture SET nombre=? WHERE idvoit=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, voiture.getNombre());
            ps.setString(2, voiture.getIdvoit());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Voiture> filtrerVoiture() {
        List<Voiture> voiture = new ArrayList<>();
        String sql = "SELECT * FROM voiture WHERE idvoit NOT IN (SELECT idvoit FROM achat)";

        try (Connection conn = Connectiondb.getconnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Voiture v = new Voiture(
                        rs.getString("idvoit"),
                        rs.getString("design"),
                        rs.getInt("prix"),
                        rs.getInt("nombre")
                );
                voiture.add(v);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return voiture;
    }

    public List<Voiture> filtrerVoitureWithDate(LocalDate datedeb, LocalDate datefin) {
        List<Voiture> voiture = new ArrayList<>();
        String sql = "SELECT * FROM voiture WHERE idvoit NOT IN (SELECT idvoit FROM achat WHERE date BETWEEN ? AND ?) AND nombre>0";

        try (Connection conn = Connectiondb.getconnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(datedeb));
            ps.setDate(2, java.sql.Date.valueOf(datefin));
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Voiture v = new Voiture(
                        rs.getString("idvoit"),
                        rs.getString("design"),
                        rs.getInt("prix"),
                        rs.getInt("nombre")
                );
                voiture.add(v);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return voiture;
    }

    public int nbStockVoiture() {
        int voit=0;
        String sql = "SELECT SUM(nombre) FROM voiture";
        try(Connection conn = Connectiondb.getconnection();
                PreparedStatement prpr = conn.prepareStatement(sql);
                ResultSet rs = prpr.executeQuery()) {
            if(rs.next()) {
                voit = rs.getInt(1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return voit;
    }
}
