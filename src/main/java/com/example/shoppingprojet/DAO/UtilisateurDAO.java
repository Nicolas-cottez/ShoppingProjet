package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.Utilisateur;

public interface UtilisateurDAO {
    boolean checkLogin(String email, String password);
    Utilisateur findByEmailAndPassword(String email, String password);
}
