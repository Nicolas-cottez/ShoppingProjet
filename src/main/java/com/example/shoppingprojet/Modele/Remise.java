package com.example.shoppingprojet.Modele;

public class Remise {
    private int idRemise;
    private int idArticle;
    private int seuil;
    private float pourcentageDeRemise;

    public Remise(int idRemise, int idArticle, int seuil, float pourcentageDeRemise) {
        this.idRemise = idRemise;
        this.idArticle = idArticle;
        this.seuil = seuil;
        this.pourcentageDeRemise = pourcentageDeRemise;
    }

    public int getIdRemise() { return idRemise; }
    public int getIdArticle() { return idArticle; }
    public int getSeuil() { return seuil; }
    public float getPourcentageDeRemise() { return pourcentageDeRemise; }

}
