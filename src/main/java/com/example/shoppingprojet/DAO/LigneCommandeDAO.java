package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.LigneCommande;


public interface LigneCommandeDAO {
    public LigneCommande chercherLigneCommande(int idCommande, int idArticle);
    public void ajouterLigneCommande(LigneCommande ligneCommande);
    public void modifierLigneCommande(LigneCommande ligneCommande);
    public void supprimerLigneCommande(int idCommande, int idArticle);
}