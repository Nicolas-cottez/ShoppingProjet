package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Rapport;
import java.util.ArrayList;
import java.time.LocalDate;

public interface RapportDAO {
    public ArrayList<Rapport> getAllRapport();
    public Rapport creerRapport(Rapport rapport);
    public void modifierRapport(Rapport rapport);
    public void supprimerRapport(int idRapport);
    public Rapport rechercherRapport(int idRapport);
    public ArrayList<Rapport> getRapportsParPeriode(LocalDate dateDebut, LocalDate dateFin);
}