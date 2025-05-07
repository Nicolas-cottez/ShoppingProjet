package com.example.shoppingprojet.DAO;
import java.util.List;
import java.util.ArrayList;
import com.example.shoppingprojet.Modele.Commande;

import java.time.LocalDate;

public interface CommandeDAO {
    public int ajouterCommande(Commande commande);
    public void modifierCommande(Commande commande);
    public void supprimerCommande(int idCommande);
    public ArrayList<Commande> getAllCommandes();
    public Commande rechercheCommande(int idCommande);
    public Commande rechercheCommandeParDate(LocalDate date);
    public double calculerPrixCommande(Commande commande);
    List<Commande> getCommandesByUtilisateur(int idUtilisateur);
}
