package com.shopapp.catalogue;

import java.util.List;
import com.shopapp.model.Product;

public interface Catalogue {
    List<Product> browseCatalogue();
    List<Product> searchProduct(String keyword);
    List<Product> filterByCategory(String category);
    Product viewProductDetails(String productId);
}
