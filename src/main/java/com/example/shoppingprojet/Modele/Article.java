package com.example.shoppingprojet.Modele;

public class Article {
    private int idArticle;
    private int idMarque;
    private String nom;
    private String description;
    private float prixUnitaire;
    private int stock;
    private String imageURL; // Nouvel attribut pour le lien de l'image

    // Constructeur mis à jour pour inclure l'imageURL
    public Article(int idArticle, int idMarque, String nom, String description, float prixUnitaire, int stock, String imageURL) {
        this.idArticle = idArticle;
        this.nom = nom;
        this.description = description;
        this.idMarque = idMarque;
        this.prixUnitaire = prixUnitaire;
        this.stock = stock;
        this.imageURL = imageURL;
    }

    public int getIdArticle() { return idArticle; }
    public int getIdMarque() { return idMarque; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public float getPrixUnitaire() { return prixUnitaire; }
    public int getStock() { return stock; }
    public String getImageURL() { return imageURL; } // Getter pour l'imageURL

    // Vous pouvez également ajouter un setter si nécessaire
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}