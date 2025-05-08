package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private LocalDate dateInscription;
    private LocalTime heureInscription;
    private String adressePostal;
    private List<Commande> commandes;
    private String role;

    public Utilisateur(int idUtilisateur, String nom, String prenom, String email, String motDePasse, LocalDate dateInscription, LocalTime heureInscription,String adressePostal,List<Commande> commandes,String role) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = dateInscription;
        this.heureInscription = heureInscription;
        this.adressePostal = adressePostal;
        this.commandes = commandes != null ? commandes : new ArrayList<>();
        this.role = role;
    }

    public int getIdUtilisateur() { return idUtilisateur; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public LocalDate getDateInscription() { return dateInscription; }
    public LocalTime getHeureInscription() { return heureInscription; }
    public String getRole() {return role;}
    public String getAdressePostal() { return adressePostal; }
    public List<Commande> getCommandes() { return commandes; }
    public void setNom(String nom) {this.nom = nom;}
    public void setPrenom(String prenom) {this.prenom = prenom;}
    public void setEmail(String email) {this.email = email;}
    public void setAdressePostal(String adressePostal) {this.adressePostal = adressePostal;}
}
