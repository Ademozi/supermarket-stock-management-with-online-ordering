package com.example.finalappli11;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rv;
    private List<Product> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rv = findViewById(R.id.rvProducts);
        allProducts = ProductData.getProducts();
        rv.setLayoutManager(new LinearLayoutManager(this));

        updateList(allProducts);

        // Configuration des boutons de catégories
        findViewById(R.id.btnAll).setOnClickListener(v -> updateList(allProducts));
        findViewById(R.id.btnCatBase).setOnClickListener(v -> filter("Produits de Base"));
        findViewById(R.id.btnCatPates).setOnClickListener(v -> filter("Pâtes et Féculents"));
        findViewById(R.id.btnCatLait).setOnClickListener(v -> filter("Produits Laitiers"));
        findViewById(R.id.btnCatEpicerie).setOnClickListener(v -> filter("Épicerie"));
        findViewById(R.id.btnCatHygiene).setOnClickListener(v -> filter("Hygiène"));
        findViewById(R.id.btnCatScolaire).setOnClickListener(v -> filter("Fournitures"));

        findViewById(R.id.btnNavCart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));
    }

    private void filter(String cat) {
        List<Product> f = new ArrayList<>();
        for(Product p : allProducts) {
            if(p.getCategory().equals(cat)) f.add(p);
        }
        updateList(f);
    }

    private void updateList(List<Product> l) {
        rv.setAdapter(new ProductAdapter(l, p -> {
            CartManager.ajouterProduit(p);
            Toast.makeText(this, p.getNom() + " ajouté !", Toast.LENGTH_SHORT).show();
        }));
    }
}