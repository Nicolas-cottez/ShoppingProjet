package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ArticleDAOImpl implements ArticleDAO {

    private Article ResultatArticle(ResultSet resultats) {
        try {
            int id = resultats.getInt("idArticle");
            int idMarque = resultats.getInt("idMarque");
            String nom = resultats.getString("nom");
            String description = resultats.getString("description");
            float prixUnitaire = resultats.getFloat("prixUnitaire");
            int stock = resultats.getInt("stock");
            String imageURL = resultats.getString("imageURL");
            return new Article(id, idMarque, nom, description, prixUnitaire, stock, imageURL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Article chercherArticleParNom(String nomArticle) {
        Article Article = null;
        String sql = "SELECT idArticle, nom, prixUnitaire, idMarque, description, stock, imageURL FROM Article WHERE nom = ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, nomArticle);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    Article = ResultatArticle(resultats);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return Article;
    }

    @Override
    public Article chercherArticleParId(int idArticle) {
        Article Article = null;
        String sql = "SELECT idArticle, nom, prixUnitaire, idMarque, description, stock, imageURL FROM Article WHERE idArticle = ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idArticle);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    Article = ResultatArticle(resultats);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return Article;
    }

    @Override
    public void ajouterArticle(Article Article) {
        String sql = "INSERT INTO Article(nom, prixUnitaire, idMarque, description, stock, imageURL) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, Article.getNom());
            preparedStatement.setFloat(2, Article.getPrixUnitaire());
            preparedStatement.setInt(3, Article.getIdMarque());
            preparedStatement.setString(4, Article.getDescription());
            preparedStatement.setInt(5, Article.getStock());
            preparedStatement.setString(6, Article.getImageURL());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierArticle(Article Article) {
        String sql = "UPDATE Article SET nom = ?, prixUnitaire = ?, idMarque = ?, description = ?, stock = ?, imageURL = ? WHERE idArticle = ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, Article.getNom());
            preparedStatement.setFloat(2, Article.getPrixUnitaire());
            preparedStatement.setInt(3, Article.getIdMarque());
            preparedStatement.setString(4, Article.getDescription());
            preparedStatement.setInt(5, Article.getStock());
            preparedStatement.setString(6, Article.getImageURL());
            preparedStatement.setInt(7, Article.getIdArticle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerArticle(int idArticle) {
        String sql = "DELETE FROM Article WHERE idArticle = ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idArticle);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Article> getAllArticle() {
        ArrayList<Article> listeArticles = new ArrayList<>();
        String sql = "SELECT idArticle, nom, prixUnitaire, idMarque, description, stock, imageURL FROM Article";
        try (Connection connexion = DBConnection.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(sql)) {
            while (resultats.next()) {
                listeArticles.add(ResultatArticle(resultats));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeArticles;
    }

    @Override
    public ArrayList<Article> getAllArticleByMarque(int idMarque) {
        ArrayList<Article> listeArticles = new ArrayList<>();
        String sql = "SELECT idArticle, nom, prixUnitaire, idMarque, description, stock, imageURL FROM Article WHERE idMarque = ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idMarque);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                while (resultats.next()) {
                    listeArticles.add(ResultatArticle(resultats));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeArticles;
    }

    @Override
    public ArrayList<Article> getAllArticleByNom(String nomArticle) {
        ArrayList<Article> listeArticles = new ArrayList<>();
        String sql = "SELECT idArticle, nom, prixUnitaire, idMarque, description, stock, imageURL FROM Article WHERE nom LIKE ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + nomArticle + "%");
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                while (resultats.next()) {
                    listeArticles.add(ResultatArticle(resultats));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeArticles;
    }


    @Override
    public ArrayList<Article> getAllArticlesByPrix(double prixMin, double prixMax) {
        ArrayList<Article> listeArticles = new ArrayList<>();
        String sql = "SELECT idArticle, nom, prixUnitaire, idMarque, description, stock, imageURL FROM Article WHERE prixUnitaire BETWEEN ? AND ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setDouble(1, prixMin);
            preparedStatement.setDouble(2, prixMax);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                while (resultats.next()) {
                    listeArticles.add(ResultatArticle(resultats));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeArticles;
    }

    @Override
    public ArrayList<Article> getAllArticlesTriesPrixCroissant(boolean croissant) {
        ArrayList<Article> listeArticles = new ArrayList<>();
        String sql = "SELECT idArticle, nom, prixUnitaire, idMarque, description, stock, imageURL FROM Article ORDER BY prixUnitaire ASC";
        if (!croissant) {
            sql = "SELECT idArticle, nom, prixUnitaire, idMarque, description, stock, imageURL FROM Article ORDER BY prixUnitaire DESC";
        }
        try (Connection connexion = DBConnection.getConnection();
             Statement statement = connexion.createStatement();
             ResultSet resultats = statement.executeQuery(sql)) {
            while (resultats.next()) {
                listeArticles.add(ResultatArticle(resultats));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeArticles;
    }

    @Override
    public ArrayList<Article> getAllArticlesTriesPrixDecroissant(boolean decroissant) {
        return getAllArticlesTriesPrixCroissant(!decroissant);
    }

    @Override
    public ArrayList<Article> getAllArticles() {
        ArrayList<Article> listeArticles = new ArrayList<>();
        String sql =
                "SELECT a.idArticle, a.nom, a.prixUnitaire, a.idMarque, a.description, a.stock, a.imageURL, m.nomMarque " +
                        "FROM Article a " +
                        "JOIN Marque m ON a.idMarque = m.idMarque";
        try (Connection connexion = DBConnection.getConnection();
             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Article art = new Article(
                        rs.getInt("idArticle"),
                        rs.getInt("idMarque"),         // colonne de a
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getFloat("prixUnitaire"),
                        rs.getInt("stock"),
                        rs.getString("imageURL")
                );
                // on injecte le nom de la marque
                art.setNomMarque(rs.getString("nomMarque"));
                listeArticles.add(art);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeArticles;
    }
    @Override
    public void decreaseStock(int idArticle, int quantity) {
        String sql = """
            UPDATE article
               SET stock = stock - ?
             WHERE idArticle = ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, idArticle);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
