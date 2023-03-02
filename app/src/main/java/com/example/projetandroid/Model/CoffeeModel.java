package com.example.projetandroid.Model;

import com.google.firebase.firestore.DocumentId;

public class CoffeeModel {
    @DocumentId
    String coffeeid;
String description;
String coffeename;
String imageURL;
int price,quantity;

    public CoffeeModel() {
    }

    public CoffeeModel(String coffeeid, String description, String coffename, String imageURL,int price ,int quantity) {
        this.coffeeid = coffeeid;
        this.description = description;
        this.coffeename = coffename;
        this.price = price;
        this.imageURL=imageURL;
        this.quantity = quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCoffeeid() {
        return coffeeid;
    }

    public void setCoffeeid(String coffeeid) {
        this.coffeeid = coffeeid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoffeename() {
        return coffeename;
    }

    public void setCoffeename(String coffeename) {
        this.coffeename = coffeename;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "CoffeeModel{" +
                "coffeeid='" + coffeeid + '\'' +
                ", description='" + description + '\'' +
                ", coffeename='" + coffeename + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
