package com.example.shoppingprojet.Modele;

public class Marque {
    private int idMarque;
    private String nomMarque;

    public Marque(int idMarque, String nomMarque) {
        this.idMarque = idMarque;
        this.nomMarque = nomMarque;
    }

    public int getIdMarque() { return idMarque; }
    public String getNomMarque() { return nomMarque; }
}

