package com.shopapp.model;

public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private String imageUrl;
    private String description;

    public Product(String id, String name, String category, double price, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getters and setters omitted for brevity
}
