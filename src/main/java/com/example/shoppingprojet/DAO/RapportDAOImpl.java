package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Rapport;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RapportDAOImpl implements RapportDAO {

    private Rapport mapResultSetToRapport(ResultSet rs) throws SQLException {
        int idRapport = rs.getInt("idRapport");
        String typeRapport = rs.getString("typeRapport");
        String donnees = rs.getString("donnees");
        return new Rapport(idRapport, typeRapport, donnees);
    }

    @Override
    public ArrayList<Rapport> getAllRapport() {
        ArrayList<Rapport> rapports = new ArrayList<>();
        String sql = "SELECT idRapport, typeRapport, donnees FROM rapport";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rapports.add(mapResultSetToRapport(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rapports;
    }

    @Override
    public Rapport creerRapport(Rapport rapport) {
        String sql = "INSERT INTO rapport (idRapport, typeRapport, donnees) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rapport.getIdRapport());
            pstmt.setString(2, rapport.getTypeRapport());
            pstmt.setString(3, rapport.getDonnees());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rapport;
    }

    @Override
    public void modifierRapport(Rapport rapport) {
        String sql = "UPDATE rapport SET typeRapport = ?, donnees = ? WHERE idRapport = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rapport.getTypeRapport());
            pstmt.setString(2, rapport.getDonnees());
            pstmt.setInt(3, rapport.getIdRapport());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerRapport(int idRapport) {
        String sql = "DELETE FROM rapport WHERE idRapport = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idRapport);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Rapport rechercherRapport(int idRapport) {
        Rapport rapport = null;
        String sql = "SELECT idRapport, typeRapport, donnees FROM rapport WHERE idRapport = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idRapport);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    rapport = mapResultSetToRapport(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rapport;
    }

    @Override
    public ArrayList<Rapport> getRapportsParPeriode(LocalDate dateDebut, LocalDate dateFin) {
        ArrayList<Rapport> rapports = new ArrayList<>();
        String sql = "SELECT idRapport, typeRapport, donnees FROM rapport WHERE date BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, dateDebut);
            pstmt.setObject(2, dateFin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rapports.add(mapResultSetToRapport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rapports;
    }
}