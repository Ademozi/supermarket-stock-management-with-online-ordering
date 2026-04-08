package com.shopapp.catalogue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopapp.R;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangedListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<product> cartList;
    private TextView tvTotal;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvTotal = findViewById(R.id.tv_total_price);
        cartList = CartManager.getCartList();


        recyclerView = findViewById(R.id.rv_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CartAdapter(cartList, this);
        recyclerView.setAdapter(adapter);

        updateTotal();

        Button btnCheckout = findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, paymentActivity.class);
            intent.putExtra("total_amount", totalAmount);
            startActivity(intent);
        });
    }

    @Override
    public void onTotalChanged() {
        updateTotal();
    }

    private void updateTotal() {
        totalAmount = 0;
        for (product p : cartList) {
            totalAmount += p.getPrice();
        }
        if (tvTotal != null) {
            tvTotal.setText("Total: " + totalAmount + " DA");
        }
    }
}
