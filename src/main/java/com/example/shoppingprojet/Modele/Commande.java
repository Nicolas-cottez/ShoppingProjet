package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class Commande {
    private int idCommande;
    private LocalDate dateCommande;
    private LocalTime heureCommande;
    private float montantTotal;
    private Client client;
    private List<Article> articles;

    public Commande(int idCommande, LocalDate dateCommande, LocalTime heureCommande,
                    float montantTotal, Client client, List<Article> articles) {
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.heureCommande = heureCommande;
        this.montantTotal = montantTotal;
        this.client = client;
        this.articles = articles != null ? articles : new ArrayList<>();
    }

    public int getIdCommande() { return idCommande; }
    public LocalDate getDateCommande() { return dateCommande; }
    public LocalTime getHeureCommande() { return heureCommande; }
    public float getMontantTotal() { return montantTotal; }
    public Client getClient() { return client; }
    public List<Article> getArticles() { return articles; }
}

