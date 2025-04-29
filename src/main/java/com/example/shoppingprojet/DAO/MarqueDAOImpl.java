package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Marque;
import java.sql.*;
import java.util.ArrayList;

public class MarqueDAOImpl implements MarqueDAO {

    private Marque mapResultSetToMarque(ResultSet rs) throws SQLException {
        int idMarque = rs.getInt("idMarque");
        String nomMarque = rs.getString("nomMarque");
        return new Marque(idMarque, nomMarque);
    }

    @Override
    public void ajouterMarque(Marque marque) {
        String sql = "INSERT INTO marques (idMarque, nomMarque) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, marque.getIdMarque());
            pstmt.setString(2, marque.getNomMarque());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierMarque(Marque marque) {
        String sql = "UPDATE marques SET nomMarque = ? WHERE idMarque = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, marque.getNomMarque());
            pstmt.setInt(2, marque.getIdMarque());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerMarque(int idMarque) {
        String sql = "DELETE FROM marques WHERE idMarque = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMarque);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Marque chercherMarqueParNom(String nomMarque) {
        Marque marque = null;
        String sql = "SELECT idMarque, nomMarque FROM marques WHERE nomMarque = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomMarque);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    marque = mapResultSetToMarque(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return marque;
    }

    @Override
    public ArrayList<Marque> getAllMarques() {
        ArrayList<Marque> marques = new ArrayList<>();
        String sql = "SELECT idMarque, nomMarque FROM marques";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                marques.add(mapResultSetToMarque(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marques;
    }

    @Override
    public Marque chercherMarquesByNom(String nomMarque) {
        return chercherMarqueParNom(nomMarque);
    }

    @Override
    public Marque chercherMarqueById(int idMarque) {
        Marque marque = null;
        String sql = "SELECT idMarque, nomMarque FROM marques WHERE idMarque = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMarque);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    marque = mapResultSetToMarque(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return marque;
    }
}