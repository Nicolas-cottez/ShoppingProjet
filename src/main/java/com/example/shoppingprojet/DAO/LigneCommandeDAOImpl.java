package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.Article;
import com.example.shoppingprojet.Modele.LigneCommande;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation JDBC de LigneCommandeDAO.
 */
public class LigneCommandeDAOImpl implements LigneCommandeDAO {

    /** Mappe un ResultSet en LigneCommande */
    private LigneCommande map(ResultSet rs) throws SQLException {
        int idCommande  = rs.getInt("idCommande");
        int articleId   = rs.getInt("idArticle");
        Article article = new ArticleDAOImpl().chercherArticleParId(articleId);
        int quantite    = rs.getInt("quantite");
        float prixLigne = rs.getFloat("prixLigne");
        return new LigneCommande(idCommande, article, quantite, prixLigne);
    }

    @Override
    public LigneCommande chercherLigneCommande(int idCommande, int idArticle) {
        String sql = "SELECT idCommande, idArticle, quantite, prixLigne "
                + "FROM ligne_commande "
                + "WHERE idCommande = ? AND idArticle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ps.setInt(2, idArticle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            ps.setInt(1, ligneCommande.getIdCommande());
            ps.setInt(2, ligneCommande.getArticle().getIdArticle());
            ps.setInt(3, ligneCommande.getQuantite());
            ps.setFloat(4, ligneCommande.getPrixLigne());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierLigneCommande(LigneCommande ligneCommande) {
        String sql = "UPDATE ligne_commande "
                + "SET quantite = ?, prixLigne = ? "
                + "WHERE idCommande = ? AND idArticle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ligneCommande.getQuantite());
            ps.setFloat(2, ligneCommande.getPrixLigne());
            ps.setInt(3, ligneCommande.getIdCommande());
            ps.setInt(4, ligneCommande.getArticle().getIdArticle());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerLigneCommande(int idCommande, int idArticle) {
        String sql = "DELETE FROM ligne_commande "
                + "WHERE idCommande = ? AND idArticle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ps.setInt(2, idArticle);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LigneCommande> getLignesByCommande(int idCommande) {
        List<LigneCommande> liste = new ArrayList<>();
        String sql = """
            SELECT idCommande, idArticle, quantite, prixLigne
              FROM ligne_commande
             WHERE idCommande = ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public List<LigneCommande> getByCommande(int idCommande) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = """
            SELECT lc.idCommande, lc.idArticle, lc.quantite, lc.prixLigne,
                   a.nom, a.description, a.prixUnitaire, a.stock, a.idMarque, a.imageURL
              FROM ligne_commande lc
              JOIN article a ON lc.idArticle = a.idArticle
             WHERE lc.idCommande = ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Article art = new Article(
                            rs.getInt("idArticle"),
                            rs.getInt("idMarque"),
                            rs.getString("nom"),
                            rs.getString("description"),
                            rs.getFloat("prixUnitaire"),
                            rs.getInt("stock"),
                            rs.getString("imageURL")
                    );
                    int qte = rs.getInt("quantite");
                    float prixLigne = rs.getFloat("prixLigne");
                    lignes.add(new LigneCommande(idCommande, art, qte, prixLigne));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignes;
    }
}
