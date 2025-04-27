package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Remise;
import java.sql.*;
import java.util.ArrayList;

public class RemiseDAOImpl implements RemiseDAO {

    private Remise ResulatRemise(ResultSet rs) throws SQLException {
        int idRemise = rs.getInt("idRemise");
        int idArticle = rs.getInt("idArticle");
        int seuil = rs.getInt("seuil");
        float pourcentageDeRemise = rs.getFloat("pourcentageDeRemise");
        return new Remise(idRemise, idArticle, seuil, pourcentageDeRemise);
    }

    @Override
    public ArrayList<Remise> getAllRemise() {
        ArrayList<Remise> remises = new ArrayList<>();
        String sql = "SELECT idRemise, idArticle, seuil, pourcentageDeRemise FROM remise";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                remises.add(ResulatRemise(rs));
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
                    remise = ResulatRemise(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return remise;
    }
}
