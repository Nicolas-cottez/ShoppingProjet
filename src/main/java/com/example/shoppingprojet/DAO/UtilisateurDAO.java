package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Article;
import  com.example.shoppingprojet.Modele.Utilisateur;
import  com.example.shoppingprojet.Modele.Marque;
import java.util.ArrayList;

public interface UtilisateurDAO {
    boolean checkLogin(String email, String password);
    Utilisateur findByEmailAndPassword(String email, String password);
    boolean createUser(Utilisateur utilisateur);
    public ArrayList<Utilisateur> getAllUtilisateur();
    //Pour l'administrateur
    public void ajouterUtilisateur(Utilisateur utilisateur);
    public void modifierUtilisateur(Utilisateur utilisateur);
    public void supprimerUtilisateur(int idUtilisateur);
    public Utilisateur rechercherUtilisateur(int idUtilisateur);
    public Utilisateur rechercherUtilisateurParNom(String nomUtilisateur);

}
