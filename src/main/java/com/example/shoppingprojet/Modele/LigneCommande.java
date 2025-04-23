package com.example.shoppingprojet.Modele;

public class LigneCommande {
    private int idCommande;
    private Article article;
    private int quantite;
    private float prixLigne;

    public LigneCommande(int idCommande, Article article, int quantite, float prixLigne) {
        this.idCommande = idCommande;
        this.article = article;
        this.quantite = quantite;
        this.prixLigne = prixLigne;
    }

    public int getIdCommande() { return idCommande; }
    public Article getArticle() { return article; }
    public int getQuantite() { return quantite; }
    public float getPrixLigne() { return prixLigne; }
}
