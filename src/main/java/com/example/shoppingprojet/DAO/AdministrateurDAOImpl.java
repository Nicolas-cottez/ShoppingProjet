package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.*;
import java.sql.*;
import java.util.ArrayList;
import java.time.*;



public class AdministrateurDAOImpl implements AdministrateurDAO {
    private void supprimerUtilisateur(int idUtilisateur) {
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(
                     "DELETE FROM utilisateurs WHERE idUtilisateur = ?")) {
            preparedStatement.setInt(1, idUtilisateur);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerClient(int idClient) {
        supprimerUtilisateur(idClient);
    }

    @Override
    public void supprimerAdministrateur(int idAdministrateur) {
        supprimerUtilisateur(idAdministrateur);
    }
    @Override
    public Article chercherArticle(int idArticle) {
        Article article = null;
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "SELECT * FROM articles WHERE articleID = ?";
            try (PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
                preparedStatement.setInt(1, idArticle);
                try (ResultSet resultats = preparedStatement.executeQuery()) {
                    if (resultats.next()) {
                        int id = resultats.getInt("articleID");
                        String nom = resultats.getString("nom");
                        float prixUnitaire = resultats.getFloat("prixUnitaire");
                        int idMarque = resultats.getInt("idMarque");
                        String description = resultats.getString("description");
                        int stock = resultats.getInt("stock");
                        article = new Article(id, idMarque, nom, description, prixUnitaire, stock);
                    }
                }
            }
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public void ajouterArticle(Article article) {
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(
                     "INSERT INTO articles(nom, prixUnitaire, idMarque, description, stock) VALUES (?, ?, ?, ?, ?)"
             )) {
            preparedStatement.setString(1, article.getNom());
            preparedStatement.setFloat(2, article.getPrixUnitaire());
            preparedStatement.setInt(3, article.getIdMarque());
            preparedStatement.setString(4, article.getDescription());
            preparedStatement.setInt(5, article.getStock());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierArticle(Article article) {
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(
                     "UPDATE articles SET nom = ?, prixUnitaire = ?, idMarque = ?, description = ?, stock = ? WHERE articleID = ?"
             )) {
            preparedStatement.setString(1, article.getNom());
            preparedStatement.setFloat(2, article.getPrixUnitaire());
            preparedStatement.setInt(3, article.getIdMarque());
            preparedStatement.setString(4, article.getDescription());
            preparedStatement.setInt(5, article.getStock());
            preparedStatement.setInt(6, article.getIdArticle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerArticle(int idArticle) {
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "DELETE FROM articles WHERE articleID = ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setInt(1, idArticle);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Client> getAllClient() {
        ArrayList<Client> listeClients = new ArrayList<>();
        try (Connection connexion = DBConnection.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery("SELECT * FROM clients")) {
            while (resultats.next()) {
                int idUtilisateur = resultats.getInt("idUtilisateur");
                String nom = resultats.getString("nom");
                String prenom = resultats.getString("prenom");
                String email = resultats.getString("email");
                String motDePasse = resultats.getString("motDePasse");
                LocalDate dateInscription = resultats.getDate("dateInscription").toLocalDate();
                LocalTime heureInscription = resultats.getTime("heureInscription").toLocalTime();
                String typeClient = resultats.getString("typeClient");
                String adressePostal = resultats.getString("adressePostal");
                // Note: La liste des commandes nécessiterait une jointure ou une requête séparée,
                // elle n'est généralement pas récupérée directement avec les informations du client.
                Client client = new Client(idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, typeClient, adressePostal, null);
                listeClients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeClients;
    }


    @Override
    public Marque chercherMarqueParId(int idMarque) {
        Marque marque = null;
        try {
            Connection connexion = DBConnection.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultats = statement.executeQuery("SELECT * FROM marques WHERE idMarque = " + idMarque);
            if (resultats.next()) {
                int id = resultats.getInt(1);
                String nom = resultats.getString(2);
                marque = new Marque(id, nom);
            }
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marque;
    }

    @Override
    public void ajouterMarque(Marque marque) {
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "INSERT INTO marques(marqueNom) VALUES (?)";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setString(1, marque.getNomMarque());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Rapport creerRapport(Rapport rapport) {
        Rapport nouveauRapport = null;
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "INSERT INTO rapports(typeRapport, donnees) VALUES (?, ?)";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, rapport.getTypeRapport());
            preparedStatement.setString(2, rapport.getDonnees());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int idGeneree = rs.getInt(1);
                nouveauRapport = new Rapport(idGeneree, rapport.getTypeRapport(), rapport.getDonnees());
            }
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nouveauRapport;
    }

    @Override
    public ArrayList<Rapport> getAllRapports() {
        ArrayList<Rapport> listeRapports = new ArrayList<>();
        try {
            Connection connexion = DBConnection.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultats = statement.executeQuery("SELECT * FROM rapports");
            while (resultats.next()) {
                int id = resultats.getInt(1);
                String titre = resultats.getString(2);
                String contenu = resultats.getString(3);
                Rapport rapport = new Rapport(id, titre, contenu);
                listeRapports.add(rapport);
            }
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeRapports;
    }

    @Override
    public void supprimerRapport(int idRapport) {
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "DELETE FROM rapports WHERE idRapport = ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setInt(1, idRapport);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public ArrayList<Remise> getAllRemise() {
        ArrayList<Remise> listeRemises = new ArrayList<>();
        try {
            Connection connexion = DBConnection.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultats = statement.executeQuery("SELECT idRemise, idArticle, seuil, pourcentageDeRemise FROM remises");
            while (resultats.next()) {
                int id = resultats.getInt("idRemise");
                int idArticle = resultats.getInt("idArticle");
                int seuil = resultats.getInt("seuil");
                float pourcentage = resultats.getFloat("pourcentageDeRemise");
                Remise remise = new Remise(id, idArticle, seuil, pourcentage);
                listeRemises.add(remise);
            }
            statement.close(); // Fermer le statement
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeRemises;
    }

    @Override
    public Remise creerRemise(Remise remise) {
        Remise nouvelleRemise = null;
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "INSERT INTO remises(idArticle, seuil, pourcentageDeRemise) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, remise.getIdArticle());
            preparedStatement.setInt(2, remise.getSeuil());
            preparedStatement.setFloat(3, remise.getPourcentageDeRemise());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                nouvelleRemise = new Remise(idGenere, remise.getIdArticle(), remise.getSeuil(), remise.getPourcentageDeRemise());
            }
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nouvelleRemise;
    }

    @Override
    public void supprimerRemise(int idRemise) {
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "DELETE FROM remises WHERE idRemise = ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setInt(1, idRemise);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public ArrayList<Administrateur> getAllAdministrateurs() {
        ArrayList<Administrateur> listeAdmins = new ArrayList<>();
        try (Connection connexion = DBConnection.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery("SELECT * FROM administrateurs")) {
            while (resultats.next()) {
                int idUtilisateur = resultats.getInt("idUtilisateur");
                String nom = resultats.getString("nom");
                String prenom = resultats.getString("prenom");
                String email = resultats.getString("email");
                String motDePasse = resultats.getString("motDePasse");
                LocalDate dateInscription = resultats.getDate("dateInscription").toLocalDate();
                LocalTime heureInscription = resultats.getTime("heureInscription").toLocalTime();
                int idAdmin = resultats.getInt("idAdmin");
                Administrateur admin = new Administrateur(idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, idAdmin);
                listeAdmins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeAdmins;
    }
    @Override
    public void ajouterAdministrateur(Administrateur administrateur) {
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "INSERT INTO administrateurs(adminNom, adminMail) VALUES (?, ?)";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setString(1, administrateur.getNom());
            preparedStatement.setString(2, administrateur.getEmail());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void modifierAdministrateur(Administrateur administrateur) {
        try {
            Connection connexion = DBConnection.getConnection();
            String sql = "UPDATE administrateurs SET adminNom = ?, adminMail = ? WHERE adminID = ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setString(1, administrateur.getNom());
            preparedStatement.setString(2, administrateur.getEmail());
            preparedStatement.setInt(3, administrateur.getIdAdmin());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
