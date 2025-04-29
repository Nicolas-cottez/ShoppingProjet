package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.Facture;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class FactureDAOImpl implements FactureDAO {

    private Facture ResulatFacture(ResultSet rs) {
        try {
            int idFacture = rs.getInt("idFacture");
            int idCommande = rs.getInt("idCommande");
            Date dateFactureSql = rs.getDate("dateFacture");
            LocalDate dateFacture = (dateFactureSql != null) ? dateFactureSql.toLocalDate() : null;
            Time heureFactureSql = rs.getTime("heureFacture");
            LocalTime heureFacture = (heureFactureSql != null) ? heureFactureSql.toLocalTime() : null;
            float montantFacture = rs.getFloat("montantFacture");
            return new Facture(idFacture, idCommande, dateFacture, heureFacture, montantFacture);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Facture chercherFacture(int idFacture) {
        Facture facture = null;
        String sql = "SELECT idFacture, idCommande, dateFacture, heureFacture, montantFacture FROM factures WHERE idFacture = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFacture);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    facture = ResulatFacture(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return facture;
    }

    @Override
    public void creerFacture(Facture facture) {
        String sql = "INSERT INTO factures (idFacture, idCommande, dateFacture, heureFacture, montantFacture) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, facture.getIdFacture());
            pstmt.setInt(2, facture.getIdCommande());
            pstmt.setObject(3, facture.getDateFacture());
            pstmt.setObject(4, facture.getHeureFacture());
            pstmt.setFloat(5, facture.getMontantFacture());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerFacture(int idFacture) {
        String sql = "DELETE FROM factures WHERE idFacture = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFacture);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Facture> getAllFactures() {
        ArrayList<Facture> factures = new ArrayList<>();
        String sql = "SELECT idFacture, idCommande, dateFacture, heureFacture, montantFacture FROM factures";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                factures.add(ResulatFacture(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    @Override
    public ArrayList<Facture> getFacturesParClient(int idClient) {
        ArrayList<Facture> factures = new ArrayList<>();
        String sql = "SELECT f.idFacture, f.idCommande, f.dateFacture, f.heureFacture, f.montantFacture " +
                "FROM factures f " +
                "JOIN commandes c ON f.idCommande = c.idCommande " +
                "WHERE c.idClient = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idClient);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    factures.add(ResulatFacture(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    @Override
    public ArrayList<Facture> getFacturesParDate(LocalDate date) {
        ArrayList<Facture> factures = new ArrayList<>();
        String sql = "SELECT idFacture, idCommande, dateFacture, heureFacture, montantFacture FROM factures WHERE dateFacture = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    factures.add(ResulatFacture(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    @Override
    public ArrayList<Facture> getFacturesParPeriode(LocalDate dateDebut, LocalDate dateFin) {
        ArrayList<Facture> factures = new ArrayList<>();
        String sql = "SELECT idFacture, idCommande, dateFacture, heureFacture, montantFacture FROM factures WHERE dateFacture BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, dateDebut);
            pstmt.setObject(2, dateFin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    factures.add(ResulatFacture(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }
}