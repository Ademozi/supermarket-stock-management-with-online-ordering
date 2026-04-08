package com.example.finalappli11;

public class Product {
    private int id;
    private String nom, category;
    private double price;

    public Product(int id, String nom, String category, double price) {
        this.id = id;
        this.nom = nom;
        this.category = category;
        this.price = price;
    }

    public String getNom() { return nom; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
}