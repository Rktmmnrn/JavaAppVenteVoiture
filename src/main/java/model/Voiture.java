/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Voiture {
    private String idvoit;
    private String design;
    private int prix;
    private int nombre;

    public Voiture(String idvoit, String design, int prix, int nombre) {
        this.idvoit = idvoit;
        this.design = design;
        this.prix = prix;
        this.nombre = nombre;
    }
    public Voiture() {
        // Surcharge de constructeur
    }
    
    public String getIdvoit() {
        return idvoit;
    }
    public String getDesign() {
        return design;
    }
    public int getPrix() {
        return prix;
    }
    public int getNombre() {
        return nombre;
    }
    
    public void setIdvoit(String idvoit) {
        this.idvoit = idvoit;
    }
    public void setDesign(String design) {
        this.design = design;
    }
    public void setPrix(int prix) {
        this.prix = prix;
    }
    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
}
