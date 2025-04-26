package com.example.shoppingprojet.Modele;

public class Article {
    private int idArticle;
    private int idMarque;
    private String nom;
    private String description;
    private float prixUnitaire;
    private int stock;

    public Article(int idArticle, int idMarque, String nom, String description, float prixUnitaire, int stock) {
        this.idArticle = idArticle;
        this.nom = nom;
        this.description = description;
        this.idMarque = idMarque;
        this.prixUnitaire = prixUnitaire;
        this.stock = stock;
    }

    public int getIdArticle() { return idArticle; }
    public int getIdMarque() { return idMarque; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public float getPrixUnitaire() { return prixUnitaire; }
    public int getStock() { return stock; }
}
