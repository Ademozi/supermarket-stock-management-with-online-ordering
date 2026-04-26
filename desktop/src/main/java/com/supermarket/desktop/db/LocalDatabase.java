package com.supermarket.desktop.db;

import com.supermarket.desktop.model.CustomerOrder;
import com.supermarket.desktop.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LocalDatabase connects the desktop app directly to a LOCAL MySQL database
 * running on this PC (localhost:3306 / supermarket_local).
 *
 * This is the PRIMARY data source for the desktop. All user actions (add,
 * update, delete products, change order status) go here first. The
 * SyncService then pushes those changes to the remote server database.
 */
public class LocalDatabase {

    // ── This is the LOCAL database on YOUR PC ─────────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/supermarket_local";
    private static final String USER     = "root";       // your local MySQL username
    private static final String PASSWORD = "Str0ng@Pass2026!"; // your local MySQL password

    // ── Get a connection to the local MySQL ───────────────────────────────────
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Call this once at app startup. Creates the tables in the local DB
     * if they do not exist yet. Safe to call every time — IF NOT EXISTS
     * means it won't destroy existing data.
     */
    public static void initialize() {
        String createProducts = """
            CREATE TABLE IF NOT EXISTS products (
                id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                name     VARCHAR(255),
                price    DOUBLE,
                quantity INT,
                barcode  VARCHAR(255),
                category VARCHAR(255)
            )
        """;

        String createOrders = """
            CREATE TABLE IF NOT EXISTS orders (
                id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                client_name VARCHAR(255),
                items       TEXT,
                total       DOUBLE,
                status      VARCHAR(50),
                created_at  VARCHAR(100)
            )
        """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createProducts);
            stmt.execute(createOrders);
            System.out.println("Local DB initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Local DB initialization failed: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PRODUCTS
    // ══════════════════════════════════════════════════════════════════════════

    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            while (rs.next()) list.add(mapProduct(rs));
        } catch (SQLException e) {
            System.err.println("getAllProducts error: " + e.getMessage());
        }
        return list;
    }

    /** Inserts a product and returns it with the generated ID filled in. */
    public static Product addProduct(Product p) {
        String sql = "INSERT INTO products (name, price, quantity, barcode, category) VALUES (?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt   (3, p.getQuantity());
            ps.setString(4, p.getBarcode());
            ps.setString(5, p.getCategory());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getLong(1));
        } catch (SQLException e) {
            System.err.println("addProduct error: " + e.getMessage());
        }
        return p;
    }

    public static void updateProduct(Product p) {
        String sql = "UPDATE products SET name=?, price=?, quantity=?, barcode=?, category=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt   (3, p.getQuantity());
            ps.setString(4, p.getBarcode());
            ps.setString(5, p.getCategory());
            ps.setLong  (6, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateProduct error: " + e.getMessage());
        }
    }

    public static void deleteProduct(Long id) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteProduct error: " + e.getMessage());
        }
    }

    public static Product findByBarcode(String barcode) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM products WHERE barcode=? LIMIT 1")) {
            ps.setString(1, barcode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapProduct(rs);
        } catch (SQLException e) {
            System.err.println("findByBarcode error: " + e.getMessage());
        }
        return null;
    }

    public static List<Product> getLowStockProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM products WHERE quantity < 5")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapProduct(rs));
        } catch (SQLException e) {
            System.err.println("getLowStock error: " + e.getMessage());
        }
        return list;
    }

    private static Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId      (rs.getLong  ("id"));
        p.setName    (rs.getString("name"));
        p.setPrice   (rs.getDouble("price"));
        p.setQuantity(rs.getInt   ("quantity"));
        p.setBarcode (rs.getString("barcode"));
        p.setCategory(rs.getString("category"));
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ORDERS
    // ══════════════════════════════════════════════════════════════════════════

    public static List<CustomerOrder> getAllOrders() {
        List<CustomerOrder> list = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {
            while (rs.next()) list.add(mapOrder(rs));
        } catch (SQLException e) {
            System.err.println("getAllOrders error: " + e.getMessage());
        }
        return list;
    }

    public static void updateOrderStatus(Long id, String status) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE orders SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setLong  (2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateOrderStatus error: " + e.getMessage());
        }
    }

    /**
     * Called during the sync pull: saves an order received from the server.
     * Uses INSERT ... ON DUPLICATE KEY UPDATE so it works for both new
     * orders (INSERT) and updated ones (UPDATE), without creating duplicates.
     */
    public static void saveOrderFromServer(CustomerOrder o) {
        String sql = """
            INSERT INTO orders (id, client_name, items, total, status, created_at)
            VALUES (?,?,?,?,?,?)
            ON DUPLICATE KEY UPDATE
                client_name = VALUES(client_name),
                items       = VALUES(items),
                total       = VALUES(total),
                status      = VALUES(status),
                created_at  = VALUES(created_at)
        """;
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong  (1, o.getId());
            ps.setString(2, o.getClientName());
            ps.setString(3, o.getItems());
            ps.setDouble(4, o.getTotal());
            ps.setString(5, o.getStatus());
            ps.setString(6, o.getCreatedAt());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("saveOrderFromServer error: " + e.getMessage());
        }
    }

    private static CustomerOrder mapOrder(ResultSet rs) throws SQLException {
        CustomerOrder o = new CustomerOrder();
        o.setId        (rs.getLong  ("id"));
        o.setClientName(rs.getString("client_name"));
        o.setItems     (rs.getString("items"));
        o.setTotal     (rs.getDouble("total"));
        o.setStatus    (rs.getString("status"));
        o.setCreatedAt (rs.getString("created_at"));
        return o;
    }
}
