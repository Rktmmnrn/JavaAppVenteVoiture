/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Client {
    private String idcli;
    private String nom;
    private String email;

    public Client(String idcli, String nom, String email) {
        this.idcli = idcli;
        this.nom = nom;
        this.email = email;
    }

    public String getIdcli() {
        return idcli;
    }
    public String getNom() {
        return nom;
    }
    public String getEmail() {
        return email;
    }
    
    public void setIdcli(String idcli) {
        this.idcli = idcli;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
