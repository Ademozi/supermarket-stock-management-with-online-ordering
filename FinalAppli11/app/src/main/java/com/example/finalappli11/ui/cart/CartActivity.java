package com.example.finalappli11.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalappli11.adapter.CartAdapter;
import com.example.finalappli11.data.CartManager;
import com.example.finalappli11.R;

public class CartActivity extends AppCompatActivity {
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvTotal = findViewById(R.id.tvCartTotalAmount);
        Button btnValidate = findViewById(R.id.btnValidateCart);
        RecyclerView rv = findViewById(R.id.rvCartItems);

        rv.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter adapter = new CartAdapter(CartManager.getCartItems(), () -> updateTotal());
        rv.setAdapter(adapter);

        updateTotal();

        btnValidate.setOnClickListener(v -> {
            startActivity(new Intent(this, CheckoutActivity.class));
        });
    }

    private void updateTotal() {
        tvTotal.setText(String.format("%.2f DA", CartManager.calculerTotal()));
    }
}