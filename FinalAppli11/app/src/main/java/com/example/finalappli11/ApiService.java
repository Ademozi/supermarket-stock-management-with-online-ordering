package com.example.finalappli11;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products") // Update this to match your actual endpoint path
    Call<List<Product>> getProducts();
}
