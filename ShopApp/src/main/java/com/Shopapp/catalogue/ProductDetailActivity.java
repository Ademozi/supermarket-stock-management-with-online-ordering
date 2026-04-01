package com.shopapp.catalogue;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.shopapp.model.Product;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView detailImage;
    private TextView detailName, detailPrice, detailCategory, detailDescription;
    private Button addToCartBtn, buyNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailPrice = findViewById(R.id.detailPrice);
        detailCategory = findViewById(R.id.detailCategory);
        detailDescription = findViewById(R.id.detailDescription);
        addToCartBtn = findViewById(R.id.addToCartDetailBtn);
        buyNowBtn = findViewById(R.id.buyNowBtn);

        // Get product from intent
        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            detailName.setText(product.getName());
            detailPrice.setText("$" + product.getPrice());
            detailCategory.setText("Category: " + product.getCategory());
            detailDescription.setText(product.getDescription());
            detailImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }
}
