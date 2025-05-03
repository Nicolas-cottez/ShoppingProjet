package com.example.shoppingprojet.DAO;
import com.example.shoppingprojet.Modele.*;
import java.sql.Connection;
import java.sql.*;

public class LigneCommandeDAOImpl implements LigneCommandeDAO {

    private LigneCommande ResultatLigneCom(ResultSet rs) {
        try {
            int idCommande = rs.getInt("idCommande");
            ArticleDAO articleDAO = new ArticleDAOImpl();
            int articleId = rs.getInt("idArticle");
            Article article = articleDAO.chercherArticleParId(articleId);
            int quantite = rs.getInt("quantite");
            float prixLigne = rs.getFloat("prixLigne");
            return new LigneCommande(idCommande, article, quantite, prixLigne);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Retourne null en cas d'erreur
        }
    }

    @Override
    public LigneCommande chercherLigneCommande(int idCommande, int idArticle) {
        LigneCommande ligneCommande = null;
        String sql = "SELECT idCommande, idArticle, quantite, prixLigne FROM ligne_commande WHERE idCommande = ? AND idArticle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCommande);
            pstmt.setInt(2, idArticle);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ligneCommande = ResultatLigneCom(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return ligneCommande;
    }

    @Override
    public void ajouterLigneCommande(LigneCommande ligneCommande) {
        String sql = """
            INSERT INTO ligne_commande
              (idCommande, idArticle, quantite, prixLigne)
            VALUES (?, ?, ?, ?)
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(   1, ligneCommande.getIdCommande());
            ps.setInt(   2, ligneCommande.getArticle().getIdArticle());
            ps.setInt(   3, ligneCommande.getQuantite());
            ps.setFloat( 4, ligneCommande.getPrixLigne());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierLigneCommande(LigneCommande ligneCommande) {
        String sql = "UPDATE ligne_commande SET quantite = ?, prixLigne = ? WHERE idCommande = ? AND idArticle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ligneCommande.getQuantite());
            pstmt.setFloat(2, ligneCommande.getPrixLigne());
            pstmt.setInt(3, ligneCommande.getIdCommande());
            pstmt.setInt(4, ligneCommande.getArticle().getIdArticle());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerLigneCommande(int idCommande, int idArticle) {
        String sql = "DELETE FROM ligne_commande WHERE idCommande = ? AND idArticle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCommande);
            pstmt.setInt(2, idArticle);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
