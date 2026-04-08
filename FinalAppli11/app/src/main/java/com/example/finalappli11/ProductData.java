package com.example.finalappli11;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class ProductData {
    public interface ApiCallback {
        void onSuccess(List<Product> products);
        void onFailure(Throwable t);
    }

    public static void fetchProducts(ApiCallback callback) {
        RetrofitClient.getService().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Erreur de réponse : " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
