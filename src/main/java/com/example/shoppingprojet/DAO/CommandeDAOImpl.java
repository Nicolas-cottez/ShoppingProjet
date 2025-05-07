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
            String adresseLivraison = rs.getString("adresseLivraison");

            // 1) Récupérer le user (si besoin, implémente findById dans userDAO)
            Utilisateur user = null;
            // ... ou leave user à null si tu le charges ailleurs

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
                    adresseLivraison,
                    user,
                    paniers
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int ajouterCommande(Commande commande) {
        String sql = """
    INSERT INTO commande
      (dateCommande, heureCommande, montantTotal, adresseLivraison, idUtilisateur)
    VALUES (?, ?, ?, ?, ?)
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, commande.getDateCommande());
            ps.setObject(2, commande.getHeureCommande());
            ps.setFloat( 3, commande.getMontantTotal());
            // 4 = adresse de livraison
            ps.setString(4, commande.getAdresseLivraison());
            // 5 = id de l’utilisateur
            ps.setInt(   5, commande.getUtilisateur().getIdUtilisateur());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    commande.setIdCommande(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public void modifierCommande(Commande commande) {
        String sql = """
    UPDATE commande
       SET dateCommande      = ?,
           heureCommande     = ?,
           montantTotal      = ?,
           adresseLivraison  = ?,
           idUtilisateur     = ?
     WHERE idCommande = ?
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, commande.getDateCommande());
            pstmt.setObject(2, commande.getHeureCommande());
            pstmt.setFloat(3, commande.getMontantTotal());
            // 4 = adresse de livraison
            pstmt.setString(4, commande.getAdresseLivraison());
            // 5 = id de l’utilisateur
            pstmt.setInt(   5, commande.getUtilisateur().getIdUtilisateur());
            // 6 = id de la commande à modifier
            pstmt.setInt(   6, commande.getIdCommande());

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
        if (commande == null || commande.getArticles() == null) {
            return 0.0;
        }
        // DAO de remise
        RemiseDAO remiseDAO = new RemiseDAOImpl();

        for (ArticlePanier ap : commande.getArticles()) {
            float prixUnitaire = ap.getArticle().getPrixUnitaire();
            int qte = ap.getQuantite();

            // Prix sans remise
            double totalLigne = prixUnitaire * qte;

            // Chargement de la remise pour cet article
            Remise r = remiseDAO.findByArticle(ap.getArticle().getIdArticle());
            if (r != null && qte >= r.getSeuil()) {
                // Applique un pourcentage de remise sur la ligne
                double taux = r.getPourcentageDeRemise() / 100.0;
                totalLigne = totalLigne * (1.0 - taux);
            }

            prixTotal += totalLigne;
        }
        return prixTotal;
    }
    @Override
    public List<Commande> getCommandesByUtilisateur(int idUtilisateur) {
        List<Commande> commandes = new ArrayList<>();
        String sql = """
        SELECT idCommande, dateCommande, heureCommande, montantTotal, adresseLivraison
          FROM commande
         WHERE idUtilisateur = ?
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    commandes.add(ResultatCommande(rs));  // charge aussi les ArticlePanier
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }


}