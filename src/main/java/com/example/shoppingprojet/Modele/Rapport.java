package com.example.shoppingprojet.Modele;

public class Rapport {
    private int idRapport;
    private String typeRapport;
    private String donnees;

    public Rapport(int idRapport,String typeRapport, String donnees) {
        this.idRapport = idRapport;
        this.typeRapport = typeRapport;
        this.donnees = donnees;
    }

    public int getIdRapport() { return idRapport; }
    public String getTypeRapport() { return typeRapport; }
    public String getDonnees() { return donnees; }
}
