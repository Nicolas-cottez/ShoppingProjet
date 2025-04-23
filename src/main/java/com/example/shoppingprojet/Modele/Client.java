package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class Client extends Utilisateur {
    private String typeClient;
    private String adressePostal;
    private List<Commande> commandes;

    public Client(int idUtilisateur, String nom, String prenom, String email, String motDePasse,
                  LocalDate dateInscription, LocalTime heureInscription,
                  String typeClient, String adressePostal, List<Commande> commandes) {

        super(idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription);
        this.typeClient = typeClient;
        this.adressePostal = adressePostal;
        this.commandes = commandes != null ? commandes : new ArrayList<>();
    }

    public String getTypeClient() { return typeClient; }
    public String getAdressePostal() { return adressePostal; }
    public List<Commande> getCommandes() { return commandes; }
}
