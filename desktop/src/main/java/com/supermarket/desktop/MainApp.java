package com.supermarket.desktop;

import com.supermarket.desktop.api.ApiClient;
import com.supermarket.desktop.model.CustomerOrder;
import com.supermarket.desktop.model.Product;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainApp extends Application {

    private TableView<Product> table = new TableView<>();
    private TableView<CustomerOrder> orderTable = new TableView<>();
    private ScheduledExecutorService scheduler;
    private Label statusLabel = new Label("Ready");

    // ── PALETTE ──────────────────────────────────────────────────────────────
    private static final String BG_DARK      = "#0f1117";
    private static final String BG_SURFACE   = "#1a1d27";
    private static final String BG_CARD      = "#21253a";
    private static final String BG_INPUT     = "#2a2f45";
    private static final String ACCENT       = "#4f8ef7";
    private static final String ACCENT_HOVER = "#3a7cf5";
    private static final String SUCCESS      = "#2ecc87";
    private static final String WARNING      = "#f5a623";
    private static final String DANGER       = "#e05252";
    private static final String TEXT_PRIMARY = "#e8eaf6";
    private static final String TEXT_MUTED   = "#8890a8";
    private static final String BORDER       = "#2e3350";

    // ── CSS ───────────────────────────────────────────────────────────────────
    private static final String CSS = """
        .root {
            -fx-font-family: 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
            -fx-font-size: 13px;
            -fx-background-color: #0f1117;
        }
        .tab-pane {
            -fx-background-color: #0f1117;
        }
        .tab-pane .tab-header-area {
            -fx-padding: 0 0 0 0;
        }
        .tab-pane .tab-header-background {
            -fx-background-color: #1a1d27;
            -fx-border-color: transparent transparent #2e3350 transparent;
            -fx-border-width: 0 0 1 0;
        }
        .tab {
            -fx-background-color: transparent;
            -fx-padding: 10 24 10 24;
            -fx-cursor: hand;
        }
        .tab:selected {
            -fx-background-color: #0f1117;
            -fx-border-color: transparent transparent #4f8ef7 transparent;
            -fx-border-width: 0 0 2 0;
        }
        .tab .tab-label {
            -fx-text-fill: #8890a8;
            -fx-font-size: 13px;
            -fx-font-weight: normal;
        }
        .tab:selected .tab-label {
            -fx-text-fill: #e8eaf6;
            -fx-font-weight: bold;
        }
        .tab-content-area {
            -fx-background-color: #0f1117;
            -fx-padding: 0;
        }
        .table-view {
            -fx-background-color: #1a1d27;
            -fx-border-color: #2e3350;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-table-cell-border-color: #2e3350;
        }
        .table-view .column-header-background {
            -fx-background-color: #21253a;
            -fx-background-radius: 8 8 0 0;
        }
        .table-view .column-header {
            -fx-background-color: transparent;
            -fx-border-color: transparent transparent #2e3350 transparent;
            -fx-border-width: 0 0 1 0;
            -fx-size: 38;
        }
        .table-view .column-header .label {
            -fx-text-fill: #8890a8;
            -fx-font-size: 11px;
            -fx-font-weight: bold;
            -fx-padding: 0 12 0 12;
        }
        .table-row-cell {
            -fx-background-color: #1a1d27;
            -fx-border-color: transparent transparent #2e3350 transparent;
            -fx-border-width: 0 0 1 0;
            -fx-cell-size: 40px;
        }
        .table-row-cell:odd {
            -fx-background-color: #1e2133;
        }
        .table-row-cell:selected {
            -fx-background-color: rgba(79,142,247,0.15);
            -fx-border-color: transparent transparent #4f8ef7 transparent;
        }
        .table-row-cell:hover {
            -fx-background-color: rgba(79,142,247,0.08);
        }
        .table-cell {
            -fx-text-fill: #e8eaf6;
            -fx-padding: 0 12 0 12;
            -fx-font-size: 13px;
        }
        .table-row-cell:selected .table-cell {
            -fx-text-fill: #e8eaf6;
        }
        .table-view .corner {
            -fx-background-color: #21253a;
        }
        .table-view:focused {
            -fx-border-color: #4f8ef7;
        }
        .scroll-bar:vertical,
        .scroll-bar:horizontal {
            -fx-background-color: #1a1d27;
            -fx-border-color: transparent;
        }
        .scroll-bar .thumb {
            -fx-background-color: #2e3350;
            -fx-background-radius: 4;
        }
        .scroll-bar .thumb:hover {
            -fx-background-color: #4f8ef7;
        }
        .scroll-bar .increment-button,
        .scroll-bar .decrement-button {
            -fx-background-color: transparent;
            -fx-padding: 0;
        }
        .text-field {
            -fx-background-color: #2a2f45;
            -fx-border-color: #2e3350;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            -fx-text-fill: #e8eaf6;
            -fx-prompt-text-fill: #8890a8;
            -fx-padding: 8 12 8 12;
            -fx-font-size: 13px;
        }
        .text-field:focused {
            -fx-border-color: #4f8ef7;
            -fx-border-width: 1.5;
        }
        .label {
            -fx-text-fill: #8890a8;
            -fx-font-size: 11px;
            -fx-font-weight: bold;
        }
        .primary-btn {
            -fx-background-color: #4f8ef7;
            -fx-text-fill: white;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-padding: 8 18 8 18;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            -fx-border-color: transparent;
        }
        .primary-btn:hover {
            -fx-background-color: #3a7cf5;
        }
        .primary-btn:pressed {
            -fx-background-color: #2a6cf0;
        }
        .danger-btn {
            -fx-background-color: rgba(224,82,82,0.15);
            -fx-text-fill: #e05252;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-border-color: #e05252;
            -fx-border-width: 1;
            -fx-padding: 8 18 8 18;
            -fx-font-size: 13px;
            -fx-cursor: hand;
        }
        .danger-btn:hover {
            -fx-background-color: rgba(224,82,82,0.28);
        }
        .ghost-btn {
            -fx-background-color: #2a2f45;
            -fx-text-fill: #e8eaf6;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-border-color: #2e3350;
            -fx-border-width: 1;
            -fx-padding: 8 18 8 18;
            -fx-font-size: 13px;
            -fx-cursor: hand;
        }
        .ghost-btn:hover {
            -fx-background-color: #353a55;
        }
        .success-btn {
            -fx-background-color: rgba(46,204,135,0.15);
            -fx-text-fill: #2ecc87;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-border-color: #2ecc87;
            -fx-border-width: 1;
            -fx-padding: 8 18 8 18;
            -fx-font-size: 13px;
            -fx-cursor: hand;
        }
        .success-btn:hover {
            -fx-background-color: rgba(46,204,135,0.28);
        }
        .warning-btn {
            -fx-background-color: rgba(245,166,35,0.15);
            -fx-text-fill: #f5a623;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-border-color: #f5a623;
            -fx-border-width: 1;
            -fx-padding: 8 18 8 18;
            -fx-font-size: 13px;
            -fx-cursor: hand;
        }
        .warning-btn:hover {
            -fx-background-color: rgba(245,166,35,0.28);
        }
        .section-title {
            -fx-font-size: 15px;
            -fx-font-weight: bold;
            -fx-text-fill: #e8eaf6;
        }
        .status-bar {
            -fx-background-color: #1a1d27;
            -fx-border-color: #2e3350 transparent transparent transparent;
            -fx-border-width: 1 0 0 0;
            -fx-padding: 6 16 6 16;
        }
        .status-label {
            -fx-text-fill: #8890a8;
            -fx-font-size: 12px;
        }
        .sidebar {
            -fx-background-color: #1a1d27;
            -fx-border-color: transparent #2e3350 transparent transparent;
            -fx-border-width: 0 1 0 0;
            -fx-padding: 16;
            -fx-pref-width: 220;
            -fx-min-width: 220;
        }
        .form-card {
            -fx-background-color: #21253a;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-border-color: #2e3350;
            -fx-border-width: 1;
            -fx-padding: 16;
        }
        .toolbar {
            -fx-background-color: #1a1d27;
            -fx-border-color: transparent transparent #2e3350 transparent;
            -fx-border-width: 0 0 1 0;
            -fx-padding: 12 16 12 16;
        }
    """;

    @Override
    public void start(Stage stage) throws Exception {
        TabPane tabPane = new TabPane(buildProductsTab(), buildOrdersTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Status bar
        HBox statusBar = new HBox(statusLabel);
        statusBar.getStyleClass().add("status-bar");
        statusLabel.getStyleClass().add("status-label");

        VBox root = new VBox(tabPane, statusBar);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 1100, 700);

        // Apply CSS via a temp file (JavaFX requires a URL, not inline strings)
        applyCSS(scene);

        stage.setScene(scene);
        stage.setTitle("Supermarket Admin");
        stage.show();

        loadProducts();
        loadOrders();

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
                () -> Platform.runLater(this::loadOrders),
                10, 10, TimeUnit.SECONDS
        );
    }

    private void applyCSS(Scene scene) {
        try {
            java.io.File tmp = java.io.File.createTempFile("supermarket", ".css");
            tmp.deleteOnExit();
            java.nio.file.Files.writeString(tmp.toPath(), CSS);
            scene.getStylesheets().add(tmp.toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── PRODUCTS TAB ─────────────────────────────────────────────────────────

    private Tab buildProductsTab() {
        // ── Form Fields ──
        TextField nameField     = styledField("Product name");
        TextField priceField    = styledField("0.00");
        TextField quantityField = styledField("0");
        TextField barcodeField  = styledField("Barcode");
        TextField categoryField = styledField("Category");

        // ── Table ──
        setupProductTable();
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                nameField.setText(sel.getName());
                priceField.setText(String.valueOf(sel.getPrice()));
                quantityField.setText(String.valueOf(sel.getQuantity()));
                barcodeField.setText(sel.getBarcode());
                categoryField.setText(sel.getCategory());
            }
        });

        // ── Form Card (sidebar) ──
        Label formTitle = new Label("PRODUCT DETAILS");
        formTitle.getStyleClass().add("section-title");

        VBox form = new VBox(10,
                formTitle,
                fieldGroup("NAME", nameField),
                fieldGroup("PRICE (DA)", priceField),
                fieldGroup("QUANTITY", quantityField),
                fieldGroup("BARCODE", barcodeField),
                fieldGroup("CATEGORY", categoryField)
        );
        form.getStyleClass().add("form-card");

        Button addBtn = new Button("＋  Add Product");
        addBtn.getStyleClass().add("primary-btn");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setOnAction(e -> {
            try {
                Product p = new Product();
                p.setName(nameField.getText());
                p.setPrice(Double.parseDouble(priceField.getText()));
                p.setQuantity(Integer.parseInt(quantityField.getText()));
                p.setBarcode(barcodeField.getText());
                p.setCategory(categoryField.getText());
                ApiClient.addProduct(p);
                loadProducts();
                setStatus("Product added successfully.");
            } catch (Exception ex) { setStatus("Error: " + ex.getMessage()); }
        });

        Button updateBtn = new Button("✎  Update");
        updateBtn.getStyleClass().add("ghost-btn");
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setOnAction(e -> {
            Product sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { setStatus("Select a product first."); return; }
            try {
                sel.setName(nameField.getText());
                sel.setPrice(Double.parseDouble(priceField.getText()));
                sel.setQuantity(Integer.parseInt(quantityField.getText()));
                sel.setBarcode(barcodeField.getText());
                sel.setCategory(categoryField.getText());
                ApiClient.updateProduct(sel);
                loadProducts();
                setStatus("Product updated.");
            } catch (Exception ex) { setStatus("Error: " + ex.getMessage()); }
        });

        Button deleteBtn = new Button("✕  Delete");
        deleteBtn.getStyleClass().add("danger-btn");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setOnAction(e -> {
            Product sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { setStatus("Select a product first."); return; }
            try {
                ApiClient.deleteProduct(sel.getId());
                loadProducts();
                setStatus("Product deleted.");
            } catch (Exception ex) { setStatus("Error: " + ex.getMessage()); }
        });

        VBox sidebar = new VBox(12, form, addBtn, updateBtn, deleteBtn);
        sidebar.getStyleClass().add("sidebar");

        // ── Toolbar (search + low stock + refresh) ──
        TextField barcodeSearch = styledField("Scan or type barcode...");
        barcodeSearch.setPrefWidth(240);
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("ghost-btn");
        searchBtn.setOnAction(e -> {
            try {
                Product p = ApiClient.getProductByBarcode(barcodeSearch.getText());
                if (p != null) { table.getItems().setAll(p); setStatus("Product found."); }
                else setStatus("No product found for that barcode.");
            } catch (Exception ex) { setStatus("Error: " + ex.getMessage()); }
        });

        Button lowStockBtn = new Button("⚠  Low Stock");
        lowStockBtn.getStyleClass().add("warning-btn");
        lowStockBtn.setOnAction(e -> {
            try {
                table.getItems().setAll(ApiClient.getLowStockProducts());
                setStatus("Showing low-stock products.");
            } catch (Exception ex) { setStatus("Error: " + ex.getMessage()); }
        });

        Button refreshBtn = new Button("↻  Refresh");
        refreshBtn.getStyleClass().add("ghost-btn");
        refreshBtn.setOnAction(e -> { loadProducts(); setStatus("Products refreshed."); });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(10, barcodeSearch, searchBtn, spacer, lowStockBtn, refreshBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("toolbar");
        toolbar.setPadding(new Insets(10, 16, 10, 16));

        VBox tableArea = new VBox(toolbar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        VBox.setVgrow(tableArea, Priority.ALWAYS);

        HBox content = new HBox(tableArea, sidebar);
        HBox.setHgrow(tableArea, Priority.ALWAYS);
        content.setStyle("-fx-background-color: #0f1117;");

        Tab tab = new Tab("  Products  ", content);
        tab.setClosable(false);
        return tab;
    }

    private void setupProductTable() {
        table.setStyle("-fx-background-color: #1a1d27;");
        table.setPlaceholder(new Label("No products loaded"));

        TableColumn<Product, String> nameCol = col("NAME", 180, p -> p.getName());
        TableColumn<Product, String> catCol  = col("CATEGORY", 140, p -> p.getCategory());
        TableColumn<Product, String> priceCol = col("PRICE (DA)", 110, p ->
                String.format("%.2f", p.getPrice()));
        TableColumn<Product, String> qtyCol  = col("QTY", 80, p -> {
            int q = p.getQuantity();
            return String.valueOf(q);
        });
        TableColumn<Product, String> barCol  = col("BARCODE", 140, p -> p.getBarcode());

        // Color-code quantity column
        qtyCol.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                try {
                    int q = Integer.parseInt(item);
                    if (q == 0) setStyle("-fx-text-fill: #e05252; -fx-font-weight: bold;");
                    else if (q < 5) setStyle("-fx-text-fill: #f5a623; -fx-font-weight: bold;");
                    else setStyle("-fx-text-fill: #2ecc87;");
                } catch (NumberFormatException ex) { setStyle(""); }
            }
        });

        table.getColumns().addAll(nameCol, catCol, priceCol, qtyCol, barCol);
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    // ── ORDERS TAB ────────────────────────────────────────────────────────────

    private Tab buildOrdersTab() {
        setupOrderTable();

        Button confirmBtn = new Button("✔  Mark Confirmed");
        confirmBtn.getStyleClass().add("success-btn");
        confirmBtn.setOnAction(e -> {
            CustomerOrder sel = orderTable.getSelectionModel().getSelectedItem();
            if (sel == null) { setStatus("Select an order first."); return; }
            try { ApiClient.updateOrderStatus(sel.getId(), "CONFIRMED"); loadOrders(); setStatus("Order confirmed."); }
            catch (Exception ex) { setStatus("Error: " + ex.getMessage()); }
        });

        Button doneBtn = new Button("✔✔  Mark Done");
        doneBtn.getStyleClass().add("primary-btn");
        doneBtn.setOnAction(e -> {
            CustomerOrder sel = orderTable.getSelectionModel().getSelectedItem();
            if (sel == null) { setStatus("Select an order first."); return; }
            try { ApiClient.updateOrderStatus(sel.getId(), "DONE"); loadOrders(); setStatus("Order marked as done."); }
            catch (Exception ex) { setStatus("Error: " + ex.getMessage()); }
        });

        Button refreshBtn = new Button("↻  Refresh Orders");
        refreshBtn.getStyleClass().add("ghost-btn");
        refreshBtn.setOnAction(e -> { loadOrders(); setStatus("Orders refreshed."); });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(10, spacer, refreshBtn, confirmBtn, doneBtn);
        toolbar.setAlignment(Pos.CENTER_RIGHT);
        toolbar.getStyleClass().add("toolbar");
        toolbar.setPadding(new Insets(10, 16, 10, 16));

        VBox layout = new VBox(toolbar, orderTable);
        VBox.setVgrow(orderTable, Priority.ALWAYS);
        layout.setStyle("-fx-background-color: #0f1117;");

        Tab tab = new Tab("  Orders  ", layout);
        tab.setClosable(false);
        tab.setOnSelectionChanged(e -> { if (tab.isSelected()) loadOrders(); });
        return tab;
    }

    private void setupOrderTable() {
        orderTable.setPlaceholder(new Label("No orders yet"));

        TableColumn<CustomerOrder, String> idCol     = col("ID", 60, o -> String.valueOf(o.getId()));
        TableColumn<CustomerOrder, String> clientCol = col("CLIENT", 140, o -> o.getClientName());
        TableColumn<CustomerOrder, String> itemsCol  = col("ITEMS", 300, o -> o.getItems());
        TableColumn<CustomerOrder, String> totalCol  = col("TOTAL (DA)", 110, o ->
                String.format("%.2f", o.getTotal()));
        TableColumn<CustomerOrder, String> dateCol   = col("DATE", 160, o -> o.getCreatedAt());
        TableColumn<CustomerOrder, String> statusCol = col("STATUS", 120, o -> o.getStatus());

        // Status badge coloring
        statusCol.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                switch (item.toUpperCase()) {
                    case "CONFIRMED" -> setStyle("-fx-text-fill: #2ecc87; -fx-font-weight: bold;");
                    case "DONE"      -> setStyle("-fx-text-fill: #4f8ef7; -fx-font-weight: bold;");
                    case "PENDING"   -> setStyle("-fx-text-fill: #f5a623; -fx-font-weight: bold;");
                    default          -> setStyle("-fx-text-fill: #8890a8;");
                }
            }
        });

        orderTable.getColumns().addAll(idCol, clientCol, itemsCol, totalCol, statusCol, dateCol);
        VBox.setVgrow(orderTable, Priority.ALWAYS);
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    private <S> TableColumn<S, String> col(String title, double width,
                                           java.util.function.Function<S, String> extractor) {
        TableColumn<S, String> c = new TableColumn<>(title);
        c.setPrefWidth(width);
        c.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(extractor.apply(data.getValue())));
        return c;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private VBox fieldGroup(String labelText, TextField field) {
        Label lbl = new Label(labelText);
        VBox group = new VBox(4, lbl, field);
        return group;
    }

    private void loadProducts() {
        try {
            table.getItems().setAll(ApiClient.getAllProducts());
            setStatus("Products loaded  ·  " + table.getItems().size() + " items");
        } catch (Exception e) { setStatus("Failed to load products: " + e.getMessage()); }
    }

    private void loadOrders() {
        try {
            List<CustomerOrder> orders = ApiClient.getOrders();
            orderTable.getItems().setAll(orders);
            setStatus("Orders loaded  ·  " + orders.size() + " orders");
        } catch (Exception e) { setStatus("Failed to load orders: " + e.getMessage()); }
    }

    private void setStatus(String msg) {
        Platform.runLater(() -> statusLabel.setText(msg));
    }

    @Override
    public void stop() {
        if (scheduler != null) scheduler.shutdownNow();
    }

    public static void main(String[] args) {
        launch();
    }
}