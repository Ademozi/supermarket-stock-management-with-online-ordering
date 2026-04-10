package com.example.finalappli11.network;

import com.example.finalappli11.model.CustomerOrder;
import com.example.finalappli11.model.Product;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("products") // Update this to match your actual endpoint path
    Call<List<Product>> getProducts();

    @POST("orders")
    Call<Void> createOrder(@Body CustomerOrder order);
}
