package com.shopapp.catalogue;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("products")
    Call<List<product>> getProducts();

    @POST("orders")
    Call<Void> placeOrder(@Body List<product> cartItems);
}
