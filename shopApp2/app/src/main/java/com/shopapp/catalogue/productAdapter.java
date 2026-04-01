package com.shopapp.catalogue;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shopapp.R;
import java.util.List;

public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder> {

    private List<product> productList;

    public productAdapter(List<product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // !!! IMPORTANT : Assure-toi d'utiliser le bon layout XML ici !!!
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        product currentProduct = productList.get(position);

        // Remplissage des données
        holder.title.setText(currentProduct.getTitle());
        holder.price.setText(currentProduct.getPrice()); // Ton prix est déjà une String "3500 DA"

        // Pour l'image, on utilise l'image par défaut pour l'instant
        holder.image.setImageResource(R.drawable.ic_launcher_foreground);

        // Tu pourrais ajouter une description par défaut dans ta classe product plus tard
        holder.description.setText("Unité"); // Exemple

        // Gestion du clic sur le bouton "+"
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ici, tu coderas plus tard l'ajout au panier
                // Pour l'instant, on peut juste faire un Toast de test
                // Toast.makeText(v.getContext(), currentProduct.getTitle() + " ajouté !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des nouveaux éléments du XML
        ImageView image;
        TextView title, price, description;
        View addToCart; // Le FloatingActionButton

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Lien avec les IDs du nouveau XML item_product.xml
            image = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            price = itemView.findViewById(R.id.product_price);
            description = itemView.findViewById(R.id.product_description);
            addToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List <product> newList){
        this.productList = newList;
        notifyDataSetChanged();
    }

    public void setNewList(List<product> newList){
        this.productList = newList;
        notifyDataSetChanged();;
    }


}