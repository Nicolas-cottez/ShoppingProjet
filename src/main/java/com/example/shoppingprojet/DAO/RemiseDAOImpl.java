package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Remise;
import java.sql.*;
import java.util.ArrayList;

public class RemiseDAOImpl implements RemiseDAO {

    private Remise mapResultSetToRemise(ResultSet rs) throws SQLException {
        return new Remise(
                rs.getInt("idRemise"),
                rs.getInt("idArticle"),
                rs.getInt("seuil"),
                rs.getFloat("pourcentageDeRemise")
        );
    }

    @Override
    public ArrayList<Remise> getAllRemise() {
        ArrayList<Remise> remises = new ArrayList<>();
        String sql = "SELECT idRemise, idArticle, seuil, pourcentageDeRemise FROM remise";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                remises.add(mapResultSetToRemise(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remises;
    }

    @Override
    public Remise creerRemise(Remise remise) {
        String sql = "INSERT INTO remise (idRemise, idArticle, seuil, pourcentageDeRemise) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, remise.getIdRemise());
            pstmt.setInt(2, remise.getIdArticle());
            pstmt.setInt(3, remise.getSeuil());
            pstmt.setFloat(4, remise.getPourcentageDeRemise());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void modifierRemise(Remise remise) {
        String sql = "UPDATE remise SET idArticle = ?, seuil = ?, pourcentageDeRemise = ? WHERE idRemise = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, remise.getIdArticle());
            pstmt.setInt(2, remise.getSeuil());
            pstmt.setFloat(3, remise.getPourcentageDeRemise());
            pstmt.setInt(4, remise.getIdRemise());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerRemise(int idRemise) {
        String sql = "DELETE FROM remise WHERE idRemise = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idRemise);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Remise rechercherRemise(int idRemise) {
        Remise remise = null;
        String sql = "SELECT idRemise, idArticle, seuil, pourcentageDeRemise FROM remise WHERE idRemise = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idRemise);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    remise = mapResultSetToRemise(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return remise;
    }
    @Override
    public Remise findByArticle(int idArticle) {
        String sql = """
            SELECT idRemise, idArticle, seuil, pourcentageDeRemise
              FROM remise
             WHERE idArticle = ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idArticle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRemise(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}