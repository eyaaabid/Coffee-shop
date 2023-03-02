package com.example.projetandroid;

import android.graphics.Bitmap;

public class Coffee {
    private String type;
    private double prix;
    private int quantite;
    private Bitmap image;
    private String docID;

    public Coffee(String type, double prix, int quantite) {
        this.type = type;
        this.prix = prix;
        this.quantite = quantite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }



}
