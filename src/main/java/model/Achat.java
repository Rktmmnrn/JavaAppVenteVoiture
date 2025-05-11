/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

public class Achat {
    private String numachat;
    private String idcli;
    private String idvoit;
    private LocalDate date;
    private int qte;

    public Achat(String numachat, String idcli, String idvoit, LocalDate date, int qte) {
        this.numachat = numachat;
        this.idcli = idcli;
        this.idvoit = idvoit;
        this.date = date;   
        this.qte = qte;
    }
    public Achat() {
        // surcharge de constructeur
    }

    public String getNumachat() {
        return numachat;
    }
    public String getIdcli() {
        return idcli;
    }
    public String getIdvoit() {
        return idvoit;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getQte() {
        return qte;
    }

    public void setNumachat(String numachat) {
        this.numachat = numachat;
    }
    public void setIdcli(String idcli) {
        this.idcli = idcli;
    }
    public void setIdvoit(String idvoit) {
        this.idvoit = idvoit;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setQte(int qte) {
        this.qte = qte;
    }
}
