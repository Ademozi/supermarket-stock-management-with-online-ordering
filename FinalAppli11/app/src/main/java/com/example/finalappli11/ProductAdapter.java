package com.example.finalappli11;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> list;
    private OnAddListener listener;

    public interface OnAddListener { void onAdd(Product p); }

    public ProductAdapter(List<Product> list, OnAddListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = list.get(position);
        holder.name.setText(p.getNom());
        holder.price.setText(p.getPrice() + " DA");
        holder.btn.setOnClickListener(v -> listener.onAdd(p));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        Button btn;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvProductName);
            price = v.findViewById(R.id.tvProductPrice);
            btn = v.findViewById(R.id.btnAddToCart);
        }
    }
}