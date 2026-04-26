package com.supermarket.desktop.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.supermarket.desktop.model.Product;
import com.supermarket.desktop.model.CustomerOrder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

/**
 * ApiClient communicates with the REMOTE server (your productapi Spring Boot app).
 *
 * IMPORTANT: This class is now used ONLY by SyncService.
 * It is NOT used directly by the UI anymore.
 *
 * The flow is:
 *   UI button → LocalDatabase (write locally) → SyncService → ApiClient → Remote server
 *
 * Change SERVER_IP below to your actual server IP address when you deploy.
 */
public class ApiClient {

    // ✅ Change this to your server's static IP when you deploy to a real server
    // For local testing, keep it as localhost
    private static final String SERVER_IP   = "localhost";
    private static final String BASE_URL    = "http://" + SERVER_IP + ":8080/products";
    private static final String ORDERS_URL  = "http://" + SERVER_IP + ":8080/orders";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static List<Product> getAllProducts() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<Product>>() {});
    }

    public static void addProduct(Product product) throws Exception {
        String json = mapper.writeValueAsString(product);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void deleteProduct(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void updateProduct(Product product) throws Exception {
        String json = mapper.writeValueAsString(product);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + product.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static Product getProductByBarcode(String barcode) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/barcode/" + barcode))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), Product.class);
    }

    public static List<Product> getLowStockProducts() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/low-stock"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<Product>>() {});
    }

    public static List<CustomerOrder> getOrders() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ORDERS_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return Arrays.asList(mapper.readValue(response.body(), CustomerOrder[].class));
    }

    public static void updateOrderStatus(Long id, String status) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ORDERS_URL + "/" + id + "?status=" + status))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
