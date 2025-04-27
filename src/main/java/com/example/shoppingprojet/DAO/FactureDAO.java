package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Facture;

import java.time.LocalDate;
import java.util.ArrayList;

public interface FactureDAO {
    public Facture chercherFacture(int idFacture);
    public void creerFacture(Facture facture);
    public void supprimerFacture(int idFacture);
    public ArrayList<Facture> getAllFactures();
    public ArrayList<Facture> getFacturesParClient(int idClient);
    public ArrayList<Facture> getFacturesParDate(LocalDate date);
    public ArrayList<Facture> getFacturesParPeriode(LocalDate dateDebut, LocalDate dateFin);
}
