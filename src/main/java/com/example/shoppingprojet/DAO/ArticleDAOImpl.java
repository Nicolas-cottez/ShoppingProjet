package com.example.shoppingprojet.DAO;

import com.example.shoppingprojet.Modele.*;
import java.sql.*;
import java.util.ArrayList;


public class ArticleDAOImpl implements ArticleDAO {

    private Article ResultatArticle(ResultSet resultats) {
        try {
            int id = resultats.getInt("articleID");
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
        Article article = null;
        String sql = "SELECT articleID, nom, prixUnitaire, idMarque, description, stock, imageURL FROM articles WHERE nom = ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, nomArticle);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    article = ResultatArticle(resultats);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return article;
    }

    @Override
    public Article chercherArticleParId(int idArticle) {
        Article article = null;
        String sql = "SELECT articleID, nom, prixUnitaire, idMarque, description, stock, imageURL FROM articles WHERE articleID = ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idArticle);
            try (ResultSet resultats = preparedStatement.executeQuery()) {
                if (resultats.next()) {
                    article = ResultatArticle(resultats);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return article;
    }

    @Override
    public void ajouterArticle(Article article) {
        String sql = "INSERT INTO articles(nom, prixUnitaire, idMarque, description, stock, imageURL) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, article.getNom());
            preparedStatement.setFloat(2, article.getPrixUnitaire());
            preparedStatement.setInt(3, article.getIdMarque());
            preparedStatement.setString(4, article.getDescription());
            preparedStatement.setInt(5, article.getStock());
            preparedStatement.setString(6, article.getImageURL());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierArticle(Article article) {
        String sql = "UPDATE articles SET nom = ?, prixUnitaire = ?, idMarque = ?, description = ?, stock = ?, imageURL = ? WHERE articleID = ?";
        try (Connection connexion = DBConnection.getConnection();
             PreparedStatement preparedStatement = connexion.prepareStatement(sql)) {
            preparedStatement.setString(1, article.getNom());
            preparedStatement.setFloat(2, article.getPrixUnitaire());
            preparedStatement.setInt(3, article.getIdMarque());
            preparedStatement.setString(4, article.getDescription());
            preparedStatement.setInt(5, article.getStock());
            preparedStatement.setString(6, article.getImageURL());
            preparedStatement.setInt(7, article.getIdArticle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerArticle(int idArticle) {
        String sql = "DELETE FROM articles WHERE articleID = ?";
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
        String sql = "SELECT articleID, nom, prixUnitaire, idMarque, description, stock, imageURL FROM articles";
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
        String sql = "SELECT articleID, nom, prixUnitaire, idMarque, description, stock, imageURL FROM articles WHERE idMarque = ?";
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
        String sql = "SELECT articleID, nom, prixUnitaire, idMarque, description, stock, imageURL FROM articles WHERE nom LIKE ?";
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
        String sql = "SELECT articleID, nom, prixUnitaire, idMarque, description, stock, imageURL FROM articles WHERE prixUnitaire BETWEEN ? AND ?";
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
        String sql = "SELECT articleID, nom, prixUnitaire, idMarque, description, stock, imageURL FROM articles ORDER BY prixUnitaire ASC";
        if (!croissant) {
            sql = "SELECT articleID, nom, prixUnitaire, idMarque, description, stock, imageURL FROM articles ORDER BY prixUnitaire DESC";
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
}
