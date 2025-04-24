package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public boolean checkLogin(String email, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM utilisateur WHERE email = ? AND motDePasse = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean isAdmin(String email) {
        String sql = "SELECT * FROM administrateur a " +
                "JOIN utilisateur u ON a.idUtilisateur = u.idUtilisateur " +
                "WHERE u.email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Utilisateur findByEmailAndPassword(String email, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM utilisateur WHERE email = ? AND motDePasse = ?"
             )) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("idUtilisateur");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String motDePasse = rs.getString("motDePasse");
                LocalDate dateInscription = rs.getDate("dateInscription").toLocalDate();
                LocalTime heureInscription = rs.getTime("heureInscription").toLocalTime();

                Utilisateur u = new Utilisateur(id, nom, prenom, email, motDePasse, dateInscription, heureInscription);
                u.setRole(isAdmin(email) ? "admin" : "client"); // ✅ Déduction dynamique du rôle
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}
