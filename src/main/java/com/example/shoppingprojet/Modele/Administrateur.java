package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;

public class Administrateur extends Utilisateur {
    private int idAdmin;

    public Administrateur(int idUtilisateur, String nom, String prenom, String email, String motDePasse,
                          LocalDate dateInscription, LocalTime heureInscription,
                          int idAdmin) {
        super(idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription);
        this.idAdmin = idAdmin;
    }

    public int getIdAdmin() {
        return idAdmin;
    }
}
