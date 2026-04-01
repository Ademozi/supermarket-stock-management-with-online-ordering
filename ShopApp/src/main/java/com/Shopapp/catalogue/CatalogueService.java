package com.shopapp.catalogue;

import com.shopapp.model.Product;
import java.util.*;

public class CatalogueService implements Catalogue {
    private Map<String, Product> products = new HashMap<>();

    public CatalogueService() {
        products.put("P001", new Product("P001", "Fresh Apples", "Fruits", 3.49, "apples.jpg", "Crisp and juicy apples."));
        products.put("P002", new Product("P002", "Whole Milk", "Dairy", 1.99, "milk.jpg", "Fresh whole milk."));
        products.put("P003", new Product("P003", "Organic Bananas", "Fruits", 1.29, "bananas.jpg", "Sweet organic bananas."));
        products.put("P004", new Product("P004", "Fresh Bread", "Bakery", 2.49, "bread.jpg", "Assorted fresh bread."));
    }

    @Override
    public List<Product> browseCatalogue() {
        return new ArrayList<>(products.values());
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        List<Product> results = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    @Override
    public List<Product> filterByCategory(String category) {
        List<Product> results = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                results.add(product);
            }
        }
        return results;
    }

    @Override
    public Product viewProductDetails(String productId) {
        return products.getOrDefault(productId, null);
    }
}
