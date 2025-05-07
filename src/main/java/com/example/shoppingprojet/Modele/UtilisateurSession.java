package com.example.shoppingprojet.Modele;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Gère la session du client et sa commande en cours (panier).
 */
public class UtilisateurSession {
    private static Utilisateur user;
    private static Commande commande;

    /** Appelé au login pour fixer le client et initialiser un panier vide. */
    public static void setUtilisateur(Utilisateur u) {
        user = u;
        resetCommande();
    }

    /** Retourne le client connecté. */
    public static Utilisateur getUtilisateur() {
        return user;
    }

    /** Retourne la commande en cours. Si elle n'existe pas, on en crée une nouvelle. */
    public static Commande getCommande() {
        if (commande == null) {
            resetCommande();
        }
        return commande;
    }

    /** Remplace la commande courante par celle passée en paramètre. */
    public static void setCommande(Commande cmd) {
        commande = cmd;
    }

    /** Vide le panier : recrée une commande vierge. */
    public static void clearCommande() {
        resetCommande();
    }

    /** Initialise une nouvelle commande vide liée au client actuel. */
    private static void resetCommande() {
        // On passe un idCommande=0, date/heure=maintenant, montant=0f,
        // adresseLivraison préremplie avec celle du user, user, et panier vide
        commande = new Commande(
                0,
                LocalDate.now(),
                LocalTime.now(),
                0f,
                user.getAdressePostal(),     // ← 5ᵉ argument : adresseLivraison
                user,                         // ← 6ᵉ argument : l’utilisateur
                new ArrayList<>()             // ← 7ᵉ argument : la liste d’articles
        );
    }

}
