package com.example.finalappli11.ui.cart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalappli11.data.CartManager;
import com.example.finalappli11.R;
import com.example.finalappli11.model.CustomerOrder;
import com.example.finalappli11.model.Product;
import com.example.finalappli11.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        EditText etName = findViewById(R.id.etName);
        Button btnConfirm = findViewById(R.id.btnConfirmOrder);

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(this, "Veuillez entrer votre nom", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                sendOrderToServer(name);
            });
        }
    }

    private void sendOrderToServer(String clientName) {
        List<Product> items = CartManager.getCartItems();
        StringBuilder itemsStr = new StringBuilder();
        for (Product p : items) {
            itemsStr.append(p.getName()).append(" (x1), ");
        }
        
        double total = CartManager.calculerTotal();
        CustomerOrder order = new CustomerOrder(clientName, itemsStr.toString(), total);

        RetrofitClient.getService().createOrder(order).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CheckoutActivity.this, "Commande envoyée avec succès !", Toast.LENGTH_LONG).show();
                    CartManager.viderPanier();
                    finish();
                } else {
                    Toast.makeText(CheckoutActivity.this, "Erreur lors de l'envoi : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Échec de connexion : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
