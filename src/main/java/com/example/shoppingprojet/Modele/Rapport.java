package com.example.shoppingprojet.Modele;

public class Rapport {
    private String typeRapport;
    private String donnees;

    public Rapport(String typeRapport, String donnees) {
        this.typeRapport = typeRapport;
        this.donnees = donnees;
    }

    public String getTypeRapport() { return typeRapport; }
    public String getDonnees() { return donnees; }
}
