package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.*;
import com.example.shoppingprojet.Modele.ArticlePanier;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAOImpl implements CommandeDAO {

    private Commande ResultatCommande(ResultSet rs) {
        try {
            int idCommande = rs.getInt("idCommande");
            Date dateCommandeSql = rs.getDate("dateCommande");
            LocalDate dateCommande = (dateCommandeSql != null)
                    ? dateCommandeSql.toLocalDate() : null;
            Time heureCommandeSql = rs.getTime("heureCommande");
            LocalTime heureCommande = (heureCommandeSql != null)
                    ? heureCommandeSql.toLocalTime() : null;
            float montantTotal = rs.getFloat("montantTotal");

            // 1) Récupérer le client (si besoin, implémente findById dans ClientDAO)
            Client client = null;
            // ... ou leave client à null si tu le charges ailleurs

            // 2) Charger les lignes de commande en ArticlePanier
            List<ArticlePanier> paniers = new ArrayList<>();
            String sqlLignes =
                    "SELECT lc.idArticle, lc.quantite, " +
                            "       a.idMarque, a.nom, a.description, a.prixUnitaire, a.stock, a.imageURL " +
                            "FROM ligne_commande lc " +
                            "JOIN article a ON lc.idArticle = a.idArticle " +
                            "WHERE lc.idCommande = ?";
            try (Connection conn2 = DBConnection.getConnection();
                 PreparedStatement ps2 = conn2.prepareStatement(sqlLignes)) {
                ps2.setInt(1, idCommande);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    while (rs2.next()) {
                        Article art = new Article(
                                rs2.getInt("idArticle"),        // idArticle
                                rs2.getInt("idMarque"),         // idMarque
                                rs2.getString("nom"),           // nom
                                rs2.getString("description"),   // description
                                rs2.getFloat("prixUnitaire"),   // prixUnitaire
                                rs2.getInt("stock"),            // stock
                                rs2.getString("imageURL")       // imageURL
                        );
                        int qte = rs2.getInt("quantite");
                        paniers.add(new ArticlePanier(art, qte));
                    }
                }
            }

            // 3) Construire et retourner la Commande
            return new Commande(
                    idCommande,
                    dateCommande,
                    heureCommande,
                    montantTotal,
                    client,
                    paniers
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void ajouterCommande(Commande commande) {
        String sql = "INSERT INTO commande (idCommande, dateCommande, heureCommande, montantTotal, idClient) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commande.getIdCommande());
            pstmt.setObject(2, commande.getDateCommande());
            pstmt.setObject(3, commande.getHeureCommande());
            pstmt.setFloat(4, commande.getMontantTotal());
            pstmt.setInt(5, commande.getClient().getIdUtilisateur());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierCommande(Commande commande) {
        String sql = "UPDATE commande SET dateCommande = ?, heureCommande = ?, montantTotal = ?, idClient = ? WHERE idCommande = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, commande.getDateCommande());
            pstmt.setObject(2, commande.getHeureCommande());
            pstmt.setFloat(3, commande.getMontantTotal());
            pstmt.setInt(4, commande.getClient().getIdUtilisateur());
            pstmt.setInt(5, commande.getIdCommande());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerCommande(int idCommande) {
        String sql = "DELETE FROM commande WHERE idCommande = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCommande);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Commande> getAllCommandes() {
        ArrayList<Commande> commandes = new ArrayList<>();
        String sql = "SELECT idCommande, dateCommande, heureCommande, montantTotal FROM commande";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                commandes.add(ResultatCommande(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    @Override
    public Commande rechercheCommande(int idCommande) {
        Commande commande = null;
        String sql = "SELECT idCommande, dateCommande, heureCommande, montantTotal FROM commande WHERE idCommande = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCommande);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    commande = ResultatCommande(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return commande;
    }

    @Override
    public Commande rechercheCommandeParDate(LocalDate date) {
        Commande commande = null;
        String sql = "SELECT idCommande, dateCommande, heureCommande, montantTotal FROM commande WHERE dateCommande = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    commande = ResultatCommande(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return commande;
    }

    @Override
    public double calculerPrixCommande(Commande commande) {
        double prixTotal = 0.0;
        if (commande != null && commande.getArticles() != null) {
            for (ArticlePanier ap : commande.getArticles()) {
                prixTotal += ap.getTotal();
            }
        }
        return prixTotal;
    }

}