/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

public class Facture {
    private String numachat;
    private String nom;
    private LocalDate date;
    private String email;
    private String design;
    private int qte;
    private int prix;

    public Facture(String numachat, String nom, LocalDate date, String email, String design, int qte, int prix) {
        this.numachat = numachat;
        this.nom = nom;
        this.date = date;
        this.email = email;
        this.design = design;
        this.qte = qte;
        this.prix = prix;
    }
    
    public String getNumachat() {
        return numachat;
    }
    public String getNom() {
        return nom;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getEmail() {
        return email;
    }
    public String getDesign() {
        return design;
    }
    public int getQte() {
        return qte;
    }
    public int getPrix() {
        return prix;
    }

    public void setNumachat(String numachat) {
        this.numachat = numachat;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setDesign(String design) {
        this.design = design;
    }
    public void setQte(int qte) {
        this.qte = qte;
    }
    public void setPrix(int prix) {
        this.prix = prix;
    }
}
