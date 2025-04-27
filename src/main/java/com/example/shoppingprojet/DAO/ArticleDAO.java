package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Administrateur;
import com.example.shoppingprojet.Modele.Article;

import java.util.ArrayList;

public interface ArticleDAO {
    public Article chercherArticleParNom(String nomMarque);
    public Article chercherArticleParId(int idArticle);
    public void ajouterArticle(Article article);
    public void modifierArticle(Article article);
    public void supprimerArticle(int idArticle);
    public ArrayList<Article> getAllArticle();
    public ArrayList<Article> getAllArticleByMarque(int idMarque);
    public ArrayList<Article> getAllArticleByNom(String nomArticle);
    public ArrayList<Article> getAllArticlesByPrix(double prixMin, double prixMax);
    public ArrayList<Article> getAllArticlesTriesPrixCroissant(boolean croissant);
    public ArrayList<Article> getAllArticlesTriesPrixDecroissant(boolean decroissant);
}
