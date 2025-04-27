package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.*;


import java.util.ArrayList;

public interface AdministrateurDAO {
    public ArrayList<Administrateur> getAllAdministrateurs();
    public void ajouterAdministrateur(Administrateur admin);
    public void modifierAdministrateur(Administrateur admin);
    public void supprimerAdministrateur(int idAdmin);
    public Administrateur rechercherAdministrateur(int idAdmin);
    public Administrateur rechercherAdministrateurParNom(String nomAdmin);
}
