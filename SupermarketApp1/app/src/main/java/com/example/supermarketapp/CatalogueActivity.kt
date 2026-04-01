package com.example.supermarketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CatalogueActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalogueScreen()
        }
    }
}

@Composable
fun CatalogueScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Catalogue Produits",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            println("Produit ajouté au catalogue ✅")
        }) {
            Text("Ajouter un produit")
        }
    }
}
