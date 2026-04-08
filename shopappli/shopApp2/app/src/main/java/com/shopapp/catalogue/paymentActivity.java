package com.shopapp.catalogue;

import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.shopapp.R;

public class paymentActivity extends androidx.appcompat.app.AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);
        double total = getIntent().getDoubleExtra("total_amount", 0.0);
        TextView tvTotal = findViewById(R.id.tv_checkout_total);
        tvTotal.setText(total + " DA");
    }
}
