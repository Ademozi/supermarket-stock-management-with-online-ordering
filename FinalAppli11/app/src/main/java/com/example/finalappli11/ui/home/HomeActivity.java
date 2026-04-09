package com.example.finalappli11.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalappli11.ui.cart.CartActivity;
import com.example.finalappli11.data.CartManager;
import com.example.finalappli11.model.Product;
import com.example.finalappli11.adapter.ProductAdapter;
import com.example.finalappli11.R;
import com.example.finalappli11.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rv;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rv = findViewById(R.id.rvProducts);
        rv.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();

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

    private void loadProducts() {
        RetrofitClient.getService().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allProducts = response.body();
                    updateList(allProducts);
                } else {
                    Toast.makeText(HomeActivity.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Erreur de connexion : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
            Toast.makeText(this, p.getName() + " ajouté !", Toast.LENGTH_SHORT).show();
        }));
    }
}