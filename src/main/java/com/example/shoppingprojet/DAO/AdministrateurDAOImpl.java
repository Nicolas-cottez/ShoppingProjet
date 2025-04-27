package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.*;
import java.sql.*;
import java.util.ArrayList;
import java.time.*;

public class AdministrateurDAOImpl implements AdministrateurDAO {

    @Override
    public ArrayList<Administrateur> getAllAdministrateurs() {
        ArrayList<Administrateur> listeAdmins = new ArrayList<>();
        String sql = "SELECT idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, idAdmin " +
                "FROM Utilisateur WHERE typeUtilisateur = 'Administrateur'";

        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql);
             ResultSet resultats = preparedStatement.executeQuery()) {

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
    public void ajouterAdministrateur(Administrateur admin) {
        String sql = "INSERT INTO Utilisateur (nom, prenom, email, motDePasse, dateInscription, heureInscription, typeUtilisateur, idAdmin) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'Administrateur', ?)";

        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, admin.getNom());
            preparedStatement.setString(2, admin.getPrenom());
            preparedStatement.setString(3, admin.getEmail());
            preparedStatement.setString(4, admin.getMotDePasse());
            preparedStatement.setDate(5, Date.valueOf(admin.getDateInscription()));
            preparedStatement.setTime(6, Time.valueOf(admin.getHeureInscription()));
            preparedStatement.setInt(7, admin.getIdAdmin());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierAdministrateur(Administrateur admin) {
        String sql = "UPDATE Utilisateur SET nom = ?, prenom = ?, email = ?, motDePasse = ?, dateInscription = ?, " +
                "heureInscription = ?, idAdmin = ? WHERE idUtilisateur = ? AND typeUtilisateur = 'Administrateur'";

        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, admin.getNom());
            preparedStatement.setString(2, admin.getPrenom());
            preparedStatement.setString(3, admin.getEmail());
            preparedStatement.setString(4, admin.getMotDePasse());
            preparedStatement.setDate(5, Date.valueOf(admin.getDateInscription()));
            preparedStatement.setTime(6, Time.valueOf(admin.getHeureInscription()));
            preparedStatement.setInt(7, admin.getIdAdmin());
            preparedStatement.setInt(8, admin.getIdUtilisateur());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerAdministrateur(int idAdmin) {
        String sql = "DELETE FROM Utilisateur WHERE idAdmin = ? AND typeUtilisateur = 'Administrateur'";

        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idAdmin);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Administrateur rechercherAdministrateur(int idAdmin) {
        Administrateur administrateur = null;
        String sql = "SELECT idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, idAdmin " +
                "FROM Utilisateur WHERE idAdmin = ? AND typeUtilisateur = 'Administrateur'";

        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idAdmin);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int idUtilisateur = resultSet.getInt("idUtilisateur");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String email = resultSet.getString("email");
                    String motDePasse = resultSet.getString("motDePasse");
                    LocalDate dateInscription = resultSet.getDate("dateInscription").toLocalDate();
                    LocalTime heureInscription = resultSet.getTime("heureInscription").toLocalTime();
                    int adminId = resultSet.getInt("idAdmin");
                    administrateur = new Administrateur(idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, adminId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return administrateur;
    }

    @Override
    public Administrateur rechercherAdministrateurParNom(String nomAdmin) {
        Administrateur administrateur = null;
        String sql = "SELECT idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, idAdmin " +
                "FROM Utilisateur WHERE nom = ? AND typeUtilisateur = 'Administrateur'";

        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, nomAdmin);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int idUtilisateur = resultSet.getInt("idUtilisateur");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String email = resultSet.getString("email");
                    String motDePasse = resultSet.getString("motDePasse");
                    LocalDate dateInscription = resultSet.getDate("dateInscription").toLocalDate();
                    LocalTime heureInscription = resultSet.getTime("heureInscription").toLocalTime();
                    int idAdmin = resultSet.getInt("idAdmin");
                    administrateur = new Administrateur(idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, idAdmin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return administrateur;
    }
}