package com.shopapp.catalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.util.Log;
import android.widget.Toast;


import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shopapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class catalogueActivity extends AppCompatActivity {

    productAdapter adapter;
    List <product> allProducts = new ArrayList<>();
    RecyclerView recyclerView;
    ApiService apiService;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Initialize Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.100.3:8080/products/") // Use localhost with 'adb reverse tcp:8080 tcp:8080'
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);

            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnItemSelectedListener(
                    item -> {
                        int id = item.getItemId();
                        if (id == R.id.nav_search) {
                            Intent intent = new Intent(catalogueActivity.this, com.shopapp.catalogue.CartActivity.class);
                            startActivity(intent);
                            return true;
                        }


                        else {
                            return false;
                        }


                    });

            RecyclerView rv = findViewById(R.id.recycler_view);
            rv.setLayoutManager(new LinearLayoutManager(this));

            adapter = new productAdapter(allProducts);
            rv.setAdapter(adapter);

            fetchProducts();


            Button btnOpenFilters = findViewById(R.id.btn_open_filters);
            btnOpenFilters.setOnClickListener(v -> {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
                View sheetView = getLayoutInflater().inflate(R.layout.layout_filter_menu, null);
                bottomSheetDialog.setContentView(sheetView);

                sheetView.findViewById(R.id.filter_fruits).setOnClickListener(view -> {
                    filterByCategory("Fruits");
                    bottomSheetDialog.dismiss();
                });

                sheetView.findViewById(R.id.filter_legumes).setOnClickListener(view -> {
                    filterByCategory("Legumes");
                    bottomSheetDialog.dismiss();
                });

                sheetView.findViewById(R.id.filter_boissons).setOnClickListener(view -> {
                    filterByCategory("Boissons");
                    bottomSheetDialog.dismiss();
                });

                sheetView.findViewById(R.id.filter_all).setOnClickListener(view -> {
                    adapter.updateList(allProducts);
                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.show();

            });

            CardView cardSearch = findViewById(R.id.card_search);


            androidx.appcompat.widget.SearchView searchView = findViewById(R.id.search_view);

            if (searchView != null) {
                searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        filterBySearch(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterBySearch(newText);
                        return false;
                    }
                });
            }



            btnOpenFilters = findViewById(R.id.btn_open_filters);
            btnOpenFilters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    afficherLeMenu();
                }
            });
        }





    private void filterByCategory(String category) {
        List<product> filteredList = new ArrayList<>();


        String cleanCategory = category.trim().toLowerCase();

        for (product p : allProducts) {
            if (p.getCategory() != null) {

                String productCat = p.getCategory().trim().toLowerCase();

                if (productCat.equals(cleanCategory)) {
                    filteredList.add(p);
                }
            }
        }

        adapter.updateList(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Rien trouvé pour : " + category, Toast.LENGTH_SHORT).show();
        }
    }

     private void filterBySearch(String query){
            List<product> filtredList = new ArrayList<>();

            for (product p : allProducts){
                if ( p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase().trim())){
                    filtredList.add(p);
                }
            }
            if(adapter !=null ){
                adapter.updateList(filtredList);
            }
     }


     private void afficherLeMenu(){

            com.google.android.material.bottomsheet.BottomSheetDialog menu =
                    new com.google.android.material.bottomsheet.BottomSheetDialog(this);

            View vueDeMenu = getLayoutInflater().inflate(R.layout.layout_filter_menu,null);
            menu.setContentView(vueDeMenu);

            vueDeMenu.findViewById(R.id.filter_all).setOnClickListener(v->{
                adapter.updateList(allProducts);
                menu.dismiss();
            });

            vueDeMenu.findViewById(R.id.filter_fruits).setOnClickListener(v->{
                filterByCategory("Fruits");
                menu.dismiss();
            });


         vueDeMenu.findViewById(R.id.filter_legumes).setOnClickListener(v->{
             filterByCategory("Legumes");
             menu.dismiss();
         });


         vueDeMenu.findViewById(R.id.filter_boissons).setOnClickListener(v->{
             filterByCategory("Boissons");
             menu.dismiss();
         });




         menu.show();


     }

    private void fetchProducts() {

        Log.d("TEST", "Starting API call...");

        apiService.getProducts().enqueue(new Callback<List<product>>() {

            @Override
            public void onResponse(Call<List<product>> call, Response<List<product>> response) {
                Log.d("TEST", "Response received");

                if (response.isSuccessful()) {
                    Log.d("TEST", "SUCCESS: " + response.body());
                } else {
                    Log.e("TEST", "ERROR CODE: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<product>> call, Throwable t) {
                Log.e("TEST", "FAILURE: " + t.toString());
            }
        });
    }
}




