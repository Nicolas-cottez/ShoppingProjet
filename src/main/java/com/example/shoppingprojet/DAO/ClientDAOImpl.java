package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Client;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImpl implements ClientDAO {

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        int idUtilisateur = rs.getInt("idUtilisateur");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String email = rs.getString("email");
        String motDePasse = rs.getString("motDePasse");
        Date dateInscriptionSql = rs.getDate("dateInscription");
        LocalDate dateInscription = (dateInscriptionSql != null) ? dateInscriptionSql.toLocalDate() : null;
        Time heureInscriptionSql = rs.getTime("heureInscription");
        LocalTime heureInscription = (heureInscriptionSql != null) ? heureInscriptionSql.toLocalTime() : null;
        String typeClient = rs.getString("typeClient");
        String adressePostal = rs.getString("adressePostal");
        return new Client(idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, typeClient, adressePostal, null);
    }

    @Override
    public ArrayList<Client> getAllClient() {
        ArrayList<Client> clients = new ArrayList<>();
        String sql = "SELECT idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, typeClient, adressePostal FROM clients";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public void ajouterClient(Client client) {
        String sql = "INSERT INTO clients (idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, typeClient, adressePostal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, client.getIdUtilisateur());
            pstmt.setString(2, client.getNom());
            pstmt.setString(3, client.getPrenom());
            pstmt.setString(4, client.getEmail());
            pstmt.setString(5, client.getMotDePasse());
            pstmt.setObject(6, client.getDateInscription());
            pstmt.setObject(7, client.getHeureInscription());
            pstmt.setString(8, client.getTypeClient());
            pstmt.setString(9, client.getAdressePostal());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ?, motDePasse = ?, dateInscription = ?, heureInscription = ?, typeClient = ?, adressePostal = ? WHERE idUtilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getMotDePasse());
            pstmt.setObject(5, client.getDateInscription());
            pstmt.setObject(6, client.getHeureInscription());
            pstmt.setString(7, client.getTypeClient());
            pstmt.setString(8, client.getAdressePostal());
            pstmt.setInt(9, client.getIdUtilisateur());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerClient(int idClient) {
        String sql = "DELETE FROM clients WHERE idUtilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idClient);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client rechercherClient(int idClient) {
        Client client = null;
        String sql = "SELECT idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, typeClient, adressePostal FROM clients WHERE idUtilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idClient);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    client = mapResultSetToClient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return client;
    }

    @Override
    public Client rechercherClientParNom(String nomClient) {
        Client client = null;
        String sql = "SELECT idUtilisateur, nom, prenom, email, motDePasse, dateInscription, heureInscription, typeClient, adressePostal FROM clients WHERE nom = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomClient);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    client = mapResultSetToClient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return client;
    }
}

