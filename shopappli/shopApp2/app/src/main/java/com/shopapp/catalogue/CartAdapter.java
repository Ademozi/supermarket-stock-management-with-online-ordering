package com.shopapp.catalogue;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopapp.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public List<product> cartList;
    public OnCartChangedListener listener;

    public interface OnCartChangedListener{
        void onTotalChanged();
    }

    public CartAdapter(List<product> cartList, OnCartChangedListener listener){
        this.cartList = cartList;
        this.listener = listener;

    }

    @NonNull
    @Override

    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position){
        product currentItem;
        currentItem = cartList.get(position);
        holder.tvTitle.setText(currentItem.getName());
        holder.tvPrice.setText(currentItem.getPrice() + " DA");

        holder.imgProduct.setImageResource(currentItem.getImageResource());

        holder.btnDelete.setOnClickListener( v ->{
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());

            if(listener != null){
                listener.onTotalChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static  class CartViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvPrice;
        ImageView imgProduct;
        ImageButton btnDelete;
    public  CartViewHolder(@NonNull View itemView){
        super(itemView);
        tvTitle = itemView.findViewById(R.id.cart_product_title);
        tvPrice = itemView.findViewById(R.id.cart_product_price);
        imgProduct = itemView.findViewById(R.id.cart_product_image);
        btnDelete = itemView.findViewById(R.id.btn_delete);



    }

    }
    }