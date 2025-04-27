package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Marque;
import java.util.ArrayList;

public interface MarqueDAO {

    public void ajouterMarque(Marque marque);
    public void modifierMarque(Marque marque);
    public void supprimerMarque(int idMarque);
    public Marque chercherMarqueParNom(String nomMarque);
    public ArrayList<Marque> getAllMarques();
    public Marque chercherMarquesByNom(String nomMarque);
    public Marque chercherMarqueById(int idMarque);
}