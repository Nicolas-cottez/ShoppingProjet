package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.Utilisateur;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    private boolean isAdmin(String role) {
        return "admin".equals(role);
    }

    @Override
    public Utilisateur findByEmailAndPassword(String email, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, adressePostal, role FROM utilisateur WHERE email = ? AND motDePasse = ?"
             )) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return ResulataUtilisateur(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createUser(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur "
                + "(nom, prenom, email, motDePasse, dateInscription, heureInscription, adressePostal, role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setDate(5, java.sql.Date.valueOf(utilisateur.getDateInscription()));
            stmt.setTime(6, java.sql.Time.valueOf(utilisateur.getHeureInscription()));
            stmt.setString(7, utilisateur.getAdressePostal());
            stmt.setString(8, "client"); // Le rôle est toujours "client" lors de la création via createUser

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Utilisateur ResulataUtilisateur(ResultSet rs) throws SQLException {
        int id = rs.getInt("idUtilisateur");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String email = rs.getString("email");
        String motDePasse = rs.getString("motDePasse");
        LocalDate dateInscription = rs.getDate("dateInscription") != null ? rs.getDate("dateInscription").toLocalDate() : null;
        LocalTime heureInscription = rs.getTime("heureInscription") != null ? rs.getTime("heureInscription").toLocalTime() : null;
        String adressePostal = rs.getString("adressePostal");
        String role = rs.getString("role");

        return new Utilisateur(id, nom, prenom, email, motDePasse, dateInscription, heureInscription, adressePostal, null, role);
    }

    @Override
    public ArrayList<Utilisateur> getAllUtilisateur() {
        ArrayList<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                utilisateurs.add(ResulataUtilisateur(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    @Override
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, motDePasse, dateInscription, heureInscription, adressePostal, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getEmail());
            pstmt.setString(4, utilisateur.getMotDePasse());
            pstmt.setDate(5, Date.valueOf(utilisateur.getDateInscription()));
            pstmt.setTime(6, Time.valueOf(utilisateur.getHeureInscription()));
            pstmt.setString(7, utilisateur.getAdressePostal());
            pstmt.setString(8, utilisateur.getRole()); // Le rôle est spécifié lors de l'appel à ajouterUtilisateur

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, motDePasse = ?, dateInscription = ?, heureInscription = ?, adressePostal = ?, role = ? WHERE idUtilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getEmail());
            pstmt.setString(4, utilisateur.getMotDePasse());
            pstmt.setDate(5, Date.valueOf(utilisateur.getDateInscription()));
            pstmt.setTime(6, Time.valueOf(utilisateur.getHeureInscription()));
            pstmt.setString(7, utilisateur.getAdressePostal());
            pstmt.setString(8, utilisateur.getRole());
            pstmt.setInt(9, utilisateur.getIdUtilisateur());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerUtilisateur(int idUtilisateur) {
        String sql = "DELETE FROM utilisateur WHERE idUtilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtilisateur);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Utilisateur rechercherUtilisateur(int idUtilisateur) {
        Utilisateur utilisateur = null;
        String sql = "SELECT * FROM utilisateur WHERE idUtilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtilisateur);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    utilisateur = ResulataUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    @Override
    public Utilisateur rechercherUtilisateurParNom(String nomUtilisateur) {
        Utilisateur utilisateur = null;
        String sql = "SELECT * FROM utilisateur WHERE nom = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomUtilisateur);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    utilisateur = ResulataUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }
}