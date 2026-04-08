package com.example.finalappli11;
import java.util.ArrayList;
import java.util.List;

public class ProductData {
    public static List<Product> getProducts() {
        List<Product> list = new ArrayList<>();

        // 1. Produits de Base
        list.add(new Product(1, "Huile Elio (5L)", "Produits de Base", 650.0));
        list.add(new Product(2, "Sucre Blanc (1kg)", "Produits de Base", 95.0));
        list.add(new Product(3, "Semoule Sim (25kg)", "Produits de Base", 1050.0));
        list.add(new Product(4, "Farine Mama (1kg)", "Produits de Base", 65.0));
        list.add(new Product(5, "Lait Loya (500g)", "Produits de Base", 550.0));

        // 2. Pâtes et Féculents
        list.add(new Product(6, "Couscous Mama (1kg)", "Pâtes et Féculents", 180.0));
        list.add(new Product(7, "Pâtes Benamor (500g)", "Pâtes et Féculents", 85.0));
        list.add(new Product(8, "Lentilles (1kg)", "Pâtes et Féculents", 320.0));
        list.add(new Product(9, "Riz Bapi (1kg)", "Pâtes et Féculents", 210.0));

        // 3. Produits Laitiers
        list.add(new Product(10, "Vache qui rit (16pc)", "Produits Laitiers", 280.0));
        list.add(new Product(11, "Camembert Berbère", "Produits Laitiers", 210.0));
        list.add(new Product(12, "Yaourt Soummam", "Produits Laitiers", 35.0));

        // 4. Épicerie Salée
        list.add(new Product(13, "Tomate Benamor (800g)", "Épicerie", 320.0));
        list.add(new Product(14, "Thon El Manar", "Épicerie", 160.0));
        list.add(new Product(15, "Chips Mahbouba", "Épicerie", 60.0));

        // 5. Boissons & P'tit Dej
        list.add(new Product(16, "Pâte El Mordjene", "Boissons & P'tit Dej", 480.0));
        list.add(new Product(17, "Café Facto (250g)", "Boissons & P'tit Dej", 290.0));
        list.add(new Product(18, "Hamoud Boualem (2L)", "Boissons & P'tit Dej", 160.0));
        list.add(new Product(19, "Eau Guedila (1.5L)", "Boissons & P'tit Dej", 40.0));

        // 6. Hygiène & Cosmétique
        list.add(new Product(20, "Shampoing Venus", "Hygiène", 350.0));
        list.add(new Product(21, "Savonnette Zyna", "Hygiène", 75.0));
        list.add(new Product(22, "Dentifrice Miswak", "Hygiène", 180.0));

        // 7. Fournitures
        list.add(new Product(23, "Cahier 96 pages", "Fournitures", 110.0));
        list.add(new Product(24, "Stylo Bic DZ", "Fournitures", 35.0));

        return list;
    }
}