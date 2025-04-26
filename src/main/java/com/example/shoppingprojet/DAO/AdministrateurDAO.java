package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.*;


import java.util.ArrayList;

public interface AdministrateurDAO {
    public Article chercherArticle(int idArticle);
    public void ajouterArticle(Article article);
    void modifierArticle(Article article);
    public void supprimerArticle(int idArticle);

    public ArrayList<Client> getAllClient();
    public void supprimerClient(int idClient);
    public Marque chercherMarqueParId(int idMarque);
    public void ajouterMarque(Marque marque);

    public Rapport creerRapport(Rapport rapport);
    public ArrayList<Rapport> getAllRapports();
    public void supprimerRapport(int idRapport);

    public ArrayList<Remise> getAllRemise();
    public Remise creerRemise(Remise remise);
    public void supprimerRemise(int idRemise);

    public ArrayList<Administrateur> getAllAdministrateurs();
    public void ajouterAdministrateur(Administrateur administrateur);
    public void supprimerAdministrateur(int idAdministrateur);
    public void modifierAdministrateur(Administrateur administrateur);
}
