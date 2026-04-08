package com.example.finalappli11;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<Product> cartItems = new ArrayList<>();

    public static void ajouterProduit(Product p) { cartItems.add(p); }

    public static void retirerProduit(int pos) {
        if (pos >= 0 && pos < cartItems.size()) cartItems.remove(pos);
    }

    public static void viderPanier() { cartItems.clear(); }

    public static List<Product> getCartItems() { return cartItems; }

    public static double calculerTotal() {
        double total = 0;
        for (Product p : cartItems) total += p.getPrice();
        return total;
    }
}