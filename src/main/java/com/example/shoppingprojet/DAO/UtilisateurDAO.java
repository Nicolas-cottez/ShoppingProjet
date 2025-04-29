package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Article;
import  com.example.shoppingprojet.Modele.Utilisateur;
import  com.example.shoppingprojet.Modele.Marque;
import java.util.ArrayList;

public interface UtilisateurDAO {
    boolean checkLogin(String email, String password);
    Utilisateur findByEmailAndPassword(String email, String password);
    boolean createUser(Utilisateur utilisateur);

    //public ArrayList<Article> getAllArticles();
    //public ArrayList<Marque> getAllMarques();

    //public Article chercherArticle(int id);

    //public Marque chercherMarque(int id);

}