package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.LigneCommande;
import java.util.List;

/**
 * DAO pour gérer les lignes de commande.
 */
public interface LigneCommandeDAO {

    /** Récupère une ligne de commande selon commande + article */
    LigneCommande chercherLigneCommande(int idCommande, int idArticle);

    /** Ajoute une ligne de commande */
    void ajouterLigneCommande(LigneCommande ligneCommande);

    /** Modifie une ligne de commande */
    void modifierLigneCommande(LigneCommande ligneCommande);

    /** Supprime une ligne de commande */
    void supprimerLigneCommande(int idCommande, int idArticle);

    /**
     * Liste toutes les lignes associées à la commande donnée
     * @param idCommande identifiant de la commande
     * @return liste de LigneCommande
     */
    List<LigneCommande> getLignesByCommande(int idCommande);
}
