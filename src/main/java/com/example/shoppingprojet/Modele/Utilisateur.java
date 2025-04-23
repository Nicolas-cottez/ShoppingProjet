package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;

public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private LocalDate dateInscription;
    private LocalTime heureInscription;

    public Utilisateur(int idUtilisateur, String nom, String prenom, String email, String motDePasse, LocalDate dateInscription, LocalTime heureInscription) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = dateInscription;
        this.heureInscription = heureInscription;
    }

    public int getIdUtilisateur() { return idUtilisateur; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public LocalDate getDateInscription() { return dateInscription; }
    public LocalTime getHeureInscription() { return heureInscription; }
}
