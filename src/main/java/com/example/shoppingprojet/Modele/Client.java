package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class Client extends Utilisateur {
    private String typeClient;
    private String adressePostal;
    private List<Commande> commandes;

    /**
     * Crée un client avec son type, son adresse et sa liste de commandes.
     */
    public Client(int idUtilisateur,
                  String nom,
                  String prenom,
                  String email,
                  String motDePasse,
                  LocalDate dateInscription,
                  LocalTime heureInscription,
                  String typeClient,
                  String adressePostal,
                  List<Commande> commandes) {
        super(idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription);
        this.typeClient   = typeClient;
        this.adressePostal= adressePostal;
        // si commandes == null, on crée une liste vide
        this.commandes    = (commandes != null) ? commandes : new ArrayList<>();
    }


    // Nouveau constructeur simplifié pour connexion rapide
    public Client(int idUtilisateur, String nom, String prenom, String email) {
        super(idUtilisateur, nom, prenom, email, null, null, null);
        this.typeClient = null;
        this.adressePostal = null;
        this.commandes = new ArrayList<>();
    }

    public String getTypeClient() { return typeClient; }
    public String getAdressePostal() { return adressePostal; }
    public List<Commande> getCommandes() { return commandes; }
}
