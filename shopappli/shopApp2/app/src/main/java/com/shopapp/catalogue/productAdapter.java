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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        product currentProduct = productList.get(position);


        holder.title.setText(currentProduct.getName());
        holder.price.setText(currentProduct.getPrice() + "DA");


        holder.image.setImageResource(R.drawable.ic_launcher_foreground);




        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartManager.addItem(currentProduct);
                android.widget.Toast.makeText(v.getContext(), currentProduct.getName() + " ajouté au panier", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, price, description;
        View addToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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