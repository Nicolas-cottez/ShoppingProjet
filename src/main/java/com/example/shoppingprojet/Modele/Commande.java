package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int idCommande;
    private LocalDate dateCommande;
    private LocalTime heureCommande;
    private float montantTotal;
    private Client client;
    private List<ArticlePanier> articles;

    public Commande(int idCommande, LocalDate dateCommande, LocalTime heureCommande,
                    float montantTotal, Client client, List<ArticlePanier> articles) {
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.heureCommande = heureCommande;
        this.montantTotal = montantTotal;
        this.client = client;
        this.articles = articles != null ? articles : new ArrayList<>();
    }
    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }
    public int getIdCommande() { return idCommande; }
    public LocalDate getDateCommande() { return dateCommande; }
    public LocalTime getHeureCommande() { return heureCommande; }
    public float getMontantTotal() { return montantTotal; }
    public Client getClient() { return client; }
    public List<ArticlePanier> getArticles() { return articles; }
    public void setDateCommande(LocalDate date) {
        this.dateCommande = date;
    }
    public void setHeureCommande(LocalTime time) {
        this.heureCommande = time;
    }
    public void setMontantTotal(float montant) {
        this.montantTotal = montant;
    }

    public void ajouterArticle(Article article, int quantite) {
        // 1) cherche si l'article est déjà dans le panier
        for (ArticlePanier ap : articles) {
            if (ap.getArticle().getIdArticle() == article.getIdArticle()) {
                // 2) si oui, incrémente simplement la quantité
                ap.setQuantite(ap.getQuantite() + quantite);
                return;
            }
        }
        // 3) sinon, on ajoute une nouvelle ligne
        articles.add(new ArticlePanier(article, quantite));
    }

}
