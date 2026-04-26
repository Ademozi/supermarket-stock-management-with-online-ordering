package com.supermarket.desktop.sync;

import com.supermarket.desktop.api.ApiClient;
import com.supermarket.desktop.db.LocalDatabase;
import com.supermarket.desktop.model.CustomerOrder;
import com.supermarket.desktop.model.Product;

import java.util.List;

/**
 * SyncService is responsible for keeping the remote server database in sync
 * with the local desktop database.
 *
 * HOW IT WORKS:
 *   - Every time the desktop WRITES something locally (add/update/delete),
 *     one of the push methods below is called in a background thread so the
 *     UI is never blocked.
 *   - Every 10 seconds, pullOrdersFromServer() is called by the scheduler in
 *     MainApp to fetch new orders placed by mobile customers and save them
 *     into the local DB.
 *
 * IMPORTANT: If the server is offline, the local write still succeeds. The
 * sync just prints an error. In a real production app you would queue failed
 * syncs and retry them, but for this school project the simple approach is fine.
 */
public class SyncService {

    // ── Push product changes TO the server ────────────────────────────────────

    public static void pushProductAdd(Product p) {
        new Thread(() -> {
            try {
                ApiClient.addProduct(p);
                System.out.println("[Sync] Product added on server: " + p.getName());
            } catch (Exception e) {
                System.err.println("[Sync] Failed to push product add: " + e.getMessage());
            }
        }).start();
    }

    public static void pushProductUpdate(Product p) {
        new Thread(() -> {
            try {
                ApiClient.updateProduct(p);
                System.out.println("[Sync] Product updated on server: " + p.getName());
            } catch (Exception e) {
                System.err.println("[Sync] Failed to push product update: " + e.getMessage());
            }
        }).start();
    }

    public static void pushProductDelete(Long id) {
        new Thread(() -> {
            try {
                ApiClient.deleteProduct(id);
                System.out.println("[Sync] Product deleted on server: id=" + id);
            } catch (Exception e) {
                System.err.println("[Sync] Failed to push product delete: " + e.getMessage());
            }
        }).start();
    }

    // ── Push order status change TO the server ────────────────────────────────

    public static void pushOrderStatus(Long id, String status) {
        new Thread(() -> {
            try {
                ApiClient.updateOrderStatus(id, status);
                System.out.println("[Sync] Order " + id + " status → " + status + " pushed to server.");
            } catch (Exception e) {
                System.err.println("[Sync] Failed to push order status: " + e.getMessage());
            }
        }).start();
    }

    // ── Pull new orders FROM the server into the local DB ─────────────────────

    /**
     * Called every 10 seconds by the MainApp scheduler.
     * Fetches all orders from the server (orders placed by the mobile app)
     * and saves them into the local MySQL database using INSERT ON DUPLICATE KEY UPDATE.
     * This means: new orders get inserted, existing ones get updated if changed.
     */
    public static void pullOrdersFromServer() {
        new Thread(() -> {
            try {
                List<CustomerOrder> serverOrders = ApiClient.getOrders();
                for (CustomerOrder o : serverOrders) {
                    LocalDatabase.saveOrderFromServer(o);
                }
                System.out.println("[Sync] Pulled " + serverOrders.size() + " orders from server.");
            } catch (Exception e) {
                System.err.println("[Sync] Failed to pull orders: " + e.getMessage());
            }
        }).start();
    }
}
