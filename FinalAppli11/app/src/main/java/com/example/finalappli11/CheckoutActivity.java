package com.example.finalappli11;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Button btnConfirm = findViewById(R.id.btnConfirmOrder);

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                Toast.makeText(this, "Paiement effectué avec succès !", Toast.LENGTH_LONG).show();
                CartManager.viderPanier();
                finish(); // Retourne à l'écran précédent
            });
        }
    }
}