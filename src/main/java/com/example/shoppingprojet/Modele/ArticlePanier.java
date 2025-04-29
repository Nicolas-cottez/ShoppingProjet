package com.example.shoppingprojet.Modele;

public class ArticlePanier {
    private Article article;
    private int quantite;

    public ArticlePanier(Article article, int quantite) {
        this.article = article;
        this.quantite = quantite;
    }



    public Article getArticle() {
        return article;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public float getTotal() {
        return article.getPrixUnitaire() * quantite;
    }
}
