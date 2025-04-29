package com.example.shoppingprojet.Modele;

public class ClientSession {
    private static Client client;
    private static Commande commande;

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        ClientSession.client = client;
    }

    public static Commande getCommande() {
        return commande;
    }

    public static void setCommande(Commande commande) {
        ClientSession.commande = commande;
    }
}
