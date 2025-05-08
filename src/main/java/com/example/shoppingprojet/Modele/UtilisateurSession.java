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
        if (u != null) {
            resetCommande();
        } else {
            // si on passe null, on vide tout
            commande = null;
        }
    }

    /** Retourne le client connecté. */
    public static Utilisateur getUtilisateur() {
        return user;
    }

    /** Retourne la commande en cours. Si elle n'existe pas, on en crée une nouvelle. */
    public static Commande getCommande() {
        if (commande == null) {
            // ne recrée que si user != null
            if (user != null) {
                resetCommande();
            }
        }
        return commande;
    }

    /** Remplace la commande courante par celle passée en paramètre. */
    public static void setCommande(Commande cmd) {
        commande = cmd;
    }

    /** Vide la session entière (user + panier). */
    public static void clearSession() {
        user     = null;
        commande = null;
    }
    public static void clearCommande() {
        resetCommande();
    }

    /** Initialise une nouvelle commande vide liée au client actuel. */
    private static void resetCommande() {
        // user est garanti non-null ici
        commande = new Commande(
                0,
                LocalDate.now(),
                LocalTime.now(),
                0f,
                user.getAdressePostal(),     // adresse du user
                user,                         // l’objet Utilisateur
                new ArrayList<>()             // panier vide
        );
    }
}
