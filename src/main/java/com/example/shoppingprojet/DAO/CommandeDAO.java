package com.example.shoppingprojet.DAO;
import java.util.List;
import java.util.ArrayList;
import com.example.shoppingprojet.Modele.Commande;

import java.time.LocalDate;

public interface CommandeDAO {
    public void ajouterCommande(Commande commande);
    public void modifierCommande(Commande commande);
    public void supprimerCommande(int idCommande);
    public ArrayList<Commande> getAllCommandes();
    public Commande rechercheCommande(int idCommande);
    public Commande rechercheCommandeParDate(LocalDate date);
    public double calculerPrixCommande(Commande commande);  // Recherche une commande par date et client
}