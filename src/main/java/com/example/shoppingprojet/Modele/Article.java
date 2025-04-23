package com.example.shoppingprojet.Modele;

public class Article {
    private int idArticle;
    private int idMarque;
    private String nom;
    private String description;
    private float prixUnitaire;
    private float prixEnVrac;
    private int quantiteEnVrac;
    private int stock;

    public Article(int idArticle, String nom, String description, int idMarque, float prixUnitaire, float prixEnVrac, int quantiteEnVrac, int stock) {
        this.idArticle = idArticle;
        this.nom = nom;
        this.description = description;
        this.idMarque = idMarque;
        this.prixUnitaire = prixUnitaire;
        this.prixEnVrac = prixEnVrac;
        this.quantiteEnVrac = quantiteEnVrac;
        this.stock = stock;
    }

    public int getIdArticle() { return idArticle; }
    public int getIdMarque() { return idMarque; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public float getPrixUnitaire() { return prixUnitaire; }
    public float getPrixEnVrac() { return prixEnVrac; }
    public int getQuantiteEnVrac() { return quantiteEnVrac; }
    public int getStock() { return stock; }
}
