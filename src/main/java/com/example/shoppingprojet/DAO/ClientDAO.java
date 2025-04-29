package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Article;
import com.example.shoppingprojet.Modele.Marque;
import java.util.ArrayList;
import com.example.shoppingprojet.Modele.Client;

public interface ClientDAO {
    public ArrayList<Client> getAllClient();
    public void ajouterClient(Client client);
    public void modifierClient(Client client);
    public void supprimerClient(int idClient);
    public Client rechercherClient(int idClient);
    public Client rechercherClientParNom(String nomClient);
}