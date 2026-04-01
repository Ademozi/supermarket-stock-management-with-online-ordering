# 🧱 1️⃣ Product.java (Frontend Model)

```
public class Product {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String barcode;
    private String category;
}
```

Why do we need this?

Because backend sends:
```
{
  "id":1,
  "name":"Milk",
  "price":2.5
}
```
That is JSON.
But Java works with objects.
So we created a class that matches the JSON structure.
This allows Jackson to convert:
```
JSON  →  Product object
```

Without this class, Java wouldn’t know how to store the data.

## 🌐 2️⃣ ApiClient.java (Very Important)

This class is your bridge between desktop and backend.
It does:
```
Desktop → HTTP request → Backend → Response → Desktop
```
Let’s break it down.

##🔹 HttpClient
HttpClient client = HttpClient.newHttpClient();

This creates something that can send HTTP requests.

Think of it like:
A browser inside your Java app.

##🔹 GET Request
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL))
        .GET()
        .build();

This creates:
GET http://localhost:8080/products

Same as typing it in a browser.

##🔹 Sending Request
HttpResponse<String> response =
        client.send(request, HttpResponse.BodyHandlers.ofString());

This means:
Send request
Wait for response
Store response as text (JSON string)

Now we have:
response.body()
Which contains JSON text.

##🔹 Convert JSON → Java Objects
return mapper.readValue(response.body(),
        new TypeReference<List<Product>>() {});

This is Jackson.
It reads JSON and converts it into:
```
List<Product>
```
So now instead of text, we have real Java objects.

----------------------------

# 🧠 Why This Is Powerful

Because:
Your backend doesn’t care if the client is:

JavaFX
Android (Kotlin)
Web (React)
iOS

All of them just call REST API.

This is called:
```
Client-Server Architecture
```

------------------------------

# Add API Method in JavaFX

Update ApiClient.java

```
public static void addProduct(Product product) throws Exception {

    ObjectMapper mapper = new ObjectMapper();

    String json = mapper.writeValueAsString(product);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .header("Content-Type","application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

    HttpClient client = HttpClient.newHttpClient();

    client.send(request, HttpResponse.BodyHandlers.ofString());
}

```

What this does:

```
Product Object
      ↓
Convert to JSON
      ↓
Send POST request
      ↓
Backend saves product
```
