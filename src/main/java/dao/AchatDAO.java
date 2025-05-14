/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Achat;
import utils.Connectiondb; // Connection a la bd

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.Date;
import model.Facture;

public class AchatDAO {
    public void ajoutAchat(Achat achat) {
        String sql = "INSERT INTO achat (numachat, idcli, idvoit, date, qte) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Connectiondb.getconnection();
                PreparedStatement prp = conn.prepareStatement(sql)) {
            prp.setString(1, achat.getNumachat());
            prp.setString(2, achat.getIdcli());
            prp.setString(3, achat.getIdvoit());
            prp.setDate(4, java.sql.Date.valueOf(achat.getDate()));
            prp.setInt(5, achat.getQte());

            prp.executeUpdate();
            System.out.println("Achat ajouter succés..");
        } catch (SQLException e) {
            System.out.println("Erreur ajout Achat :"+e.getMessage());
        }
    }

    public void modifAchat(Achat achat) {
        try(Connection conn = Connectiondb.getconnection()) {
            String sql = "UPDATE achat SET idcli=?, idvoit=?, date=?, qte=? WHERE numachat=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, achat.getIdcli());
            ps.setString(2, achat.getIdvoit());
            ps.setDate(3, java.sql.Date.valueOf(achat.getDate()));
            ps.setInt(4, achat.getQte());
            ps.setString(5, achat.getNumachat());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprAchat(String numachat) {
        String sql = "DELETE FROM achat WHERE numachat = ?";

        try (Connection conn = Connectiondb.getconnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numachat);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Achat> getAllAchat() {
        List<Achat> achat = new ArrayList<>();
        String sql = "SELECT * FROM achat";

        try (Connection conn = Connectiondb.getconnection();
              Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Achat a = new Achat(
                        rs.getString("numachat"),
                        rs.getString("idcli"),
                        rs.getString("idvoit"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("qte")
                );
               achat.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur d'affichage :"+e.getMessage());
        }
        return achat;
    }

    public List<Achat> searchAchat(LocalDate date1, LocalDate date2) {
        List<Achat> achat = new ArrayList<>();
        String sql = "SELECT * FROM achat WHERE date BETWEEN ? AND ?";
        
        try (Connection conn = Connectiondb.getconnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(date1));
            ps.setDate(2, java.sql.Date.valueOf(date2));
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Achat a = new Achat(
                        rs.getString("numachat"),
                        rs.getString("idcli"),
                        rs.getString("idvoit"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("qte")
                );
                achat.add(a);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return achat;
    }

    public Map<YearMonth, Double> getRecette() {
        Map<YearMonth, Double> recette = new LinkedHashMap<>();

        LocalDate maintenant = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            YearMonth mois = YearMonth.from(maintenant.minusMonths(i));
            recette.put(mois, 0.0); // Valeur par défaut
        }

        String sql = "SELECT EXTRACT(YEAR FROM date) AS annee, EXTRACT(MONTH FROM date) AS mois, SUM(prix) AS total " +
                     "FROM achat JOIN voiture ON achat.idvoit=voiture.idvoit WHERE date >= ? GROUP BY annee, mois ORDER BY annee, mois";

        try (Connection conn = Connectiondb.getconnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setDate(1, java.sql.Date.valueOf(maintenant.minusMonths(5).withDayOfMonth(1)));

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int annee = rs.getInt("annee");
                int mois = rs.getInt("mois");
                double total = rs.getDouble("total");
                recette.put(YearMonth.of(annee, mois), total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recette;
    }

    public List<Facture> factureAchat(String numachat) {
        List<Facture> facture = new ArrayList<>();
        String sql = "SELECT numachat, nom, date, email, design, qte, prix FROM achat "
                + "JOIN client ON achat.idcli=client.idcli "
                + "JOIN voiture ON achat.idvoit=voiture.idvoit WHERE numachat = ?";

        try (Connection conn = Connectiondb.getconnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numachat);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Facture f = new Facture(
                        rs.getString("numachat"),
                        rs.getString("nom"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("email"),
                        rs.getString("design"),
                        rs.getInt("qte"),
                        rs.getInt("prix")
                );
                facture.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facture;
    }

    public String getEmail(String numachat) {
        String sql = "SELECT email FROM achat JOIN client ON achat.idcli=client.idcli WHERE numachat=?";

        try (Connection conn = Connectiondb.getconnection();
              PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numachat);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur d'affichage :"+e.getMessage());
        }
        return null;
    }

    public int nbAchat() {
        int achat=0;
        String sql = "SELECT COUNT(*) FROM achat";
        try(Connection conn = Connectiondb.getconnection();
                PreparedStatement prpr = conn.prepareStatement(sql);
                ResultSet rs = prpr.executeQuery()) {
            if(rs.next()) {
                achat = rs.getInt(1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return achat;
    }
}
