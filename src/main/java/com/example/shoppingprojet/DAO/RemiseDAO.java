package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Remise;
import java.util.ArrayList;

public interface RemiseDAO {
    public ArrayList<Remise> getAllRemise();
    public Remise creerRemise(Remise remise);
    public void modifierRemise(Remise remise);
    public void supprimerRemise(int idRemise);
    public Remise rechercherRemise(int idRemise);
    Remise findByArticle(int idArticle);
}
