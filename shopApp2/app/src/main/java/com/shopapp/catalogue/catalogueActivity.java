package com.shopapp.catalogue;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;


import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shopapp.R;

import java.util.ArrayList;
import java.util.List;

public class catalogueActivity extends AppCompatActivity {

    productAdapter adapter;
    List<product> allProducts = new ArrayList<>();
    RecyclerView recyclerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            allProducts.add(new product("pomme", "200DA", "Fruits"));
            allProducts.add(new product("carrote", "350DA", "Legumes"));
            allProducts.add(new product("coca", "120DA", "Boissons"));
            allProducts.add(new product("eau", "30DA", "Boissons"));


            RecyclerView rv = findViewById(R.id.recycler_view);
            rv.setLayoutManager(new LinearLayoutManager(this));

            adapter = new productAdapter(allProducts);
            rv.setAdapter(adapter);


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


            //buttonsr

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
                if ( p.getTitle() != null && p.getTitle().toLowerCase().contains(query.toLowerCase().trim())){
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


    }

