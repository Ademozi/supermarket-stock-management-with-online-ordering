package com.example.finalappli11.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalappli11.data.CartManager;
import com.example.finalappli11.model.Product;
import com.example.finalappli11.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartList;
    private OnDeleteListener listener;

    // Interface pour mettre à jour le total dans CartActivity quand on supprime
    public interface OnDeleteListener {
        void onDelete();
    }

    public CartAdapter(List<Product> cartList, OnDeleteListener listener) {
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // On gonfle le layout item_cart
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product p = cartList.get(position);

        // Affichage du nom et du prix
        holder.name.setText(p.getName());
        holder.price.setText(p.getPrice() + " DA");

        // Gestion du bouton supprimer (croix rouge)
        holder.btnRemove.setOnClickListener(v -> {
            // 1. On retire du manager
            CartManager.retirerProduit(position);
            // 2. On prévient l'adapter pour rafraîchir la liste
            notifyDataSetChanged();
            // 3. On appelle le listener pour recalculer le prix total affiché
            if (listener != null) {
                listener.onDelete();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // La classe qui fait le lien avec les IDs du XML
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        Button btnRemove;

        public CartViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvCartName);
            price = v.findViewById(R.id.tvCartPrice);
            btnRemove = v.findViewById(R.id.btnRemoveItem);
        }
    }
}