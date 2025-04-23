package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;

public class Facture {
    private int idFacture;
    private int idCommande;
    private LocalDate dateFacture;
    private LocalTime heureFacture;
    private float montantFacture;

    public Facture(int idFacture, int idCommande, LocalDate dateFacture, LocalTime heureFacture, float montantFacture) {
        this.idFacture = idFacture;
        this.idCommande = idCommande;
        this.dateFacture = dateFacture;
        this.heureFacture = heureFacture;
        this.montantFacture = montantFacture;
    }

    public int getIdFacture() { return idFacture; }
    public int getIdCommande() { return idCommande; }
    public LocalDate getDateFacture() { return dateFacture; }
    public LocalTime getHeureFacture() { return heureFacture; }
    public float getMontantFacture() { return montantFacture; }
}
