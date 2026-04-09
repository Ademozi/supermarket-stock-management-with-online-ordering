package com.supermarket.desktop;

import com.supermarket.desktop.api.ApiClient;
import com.supermarket.desktop.model.CustomerOrder;
import com.supermarket.desktop.model.Product;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainApp extends Application {

    private TableView<Product> table = new TableView<>();
    private TableView<CustomerOrder> orderTable = new TableView<>();
    private ScheduledExecutorService scheduler;

    @Override
    public void start(Stage stage) throws Exception {

        // ── PRODUCT FORM FIELDS ──
        TextField nameField     = new TextField();
        TextField priceField    = new TextField();
        TextField quantityField = new TextField();
        TextField barcodeField  = new TextField();
        TextField categoryField = new TextField();

        // ── PRODUCT TABLE COLUMNS ──
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getPrice())));

        TableColumn<Product, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        TableColumn<Product, String> barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getBarcode()));

        table.getColumns().addAll(nameCol, priceCol, qtyCol, categoryCol, barcodeCol);

        // Fill form when a product row is selected
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                nameField.setText(selected.getName());
                priceField.setText(String.valueOf(selected.getPrice()));
                quantityField.setText(String.valueOf(selected.getQuantity()));
                barcodeField.setText(selected.getBarcode());
                categoryField.setText(selected.getCategory());
            }
        });

        // ── ADD ──
        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> {
            try {
                Product p = new Product();
                p.setName(nameField.getText());
                p.setPrice(Double.parseDouble(priceField.getText()));
                p.setQuantity(Integer.parseInt(quantityField.getText()));
                p.setBarcode(barcodeField.getText());
                p.setCategory(categoryField.getText());
                ApiClient.addProduct(p);
                loadProducts();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // ── UPDATE ──
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            try {
                selected.setName(nameField.getText());
                selected.setPrice(Double.parseDouble(priceField.getText()));
                selected.setQuantity(Integer.parseInt(quantityField.getText()));
                selected.setBarcode(barcodeField.getText());
                selected.setCategory(categoryField.getText());
                ApiClient.updateProduct(selected);
                loadProducts();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // ── DELETE ──
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            try {
                ApiClient.deleteProduct(selected.getId());
                loadProducts();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // ── BARCODE SEARCH ──
        TextField barcodeSearchField = new TextField();
        barcodeSearchField.setPromptText("Scan barcode...");
        Button barcodeSearchButton = new Button("Search");
        barcodeSearchButton.setOnAction(e -> {
            try {
                Product product = ApiClient.getProductByBarcode(barcodeSearchField.getText());
                if (product != null) table.getItems().setAll(product);
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // ── LOW STOCK ──
        Button lowStockButton = new Button("Low Stock Alerts");
        lowStockButton.setOnAction(e -> {
            try {
                table.getItems().setAll(ApiClient.getLowStockProducts());
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // ── REFRESH PRODUCTS ──
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadProducts());

        // ── PRODUCT LAYOUT ──
        VBox form = new VBox(4,
                new Label("Name"),     nameField,
                new Label("Price"),    priceField,
                new Label("Quantity"), quantityField,
                new Label("Barcode"),  barcodeField,
                new Label("Category"), categoryField,
                addButton
        );
        HBox productButtons = new HBox(10, refreshBtn, deleteButton, updateButton, lowStockButton);
        HBox searchBox      = new HBox(10, new Label("Barcode:"), barcodeSearchField, barcodeSearchButton);
        VBox productsLayout = new VBox(10, searchBox, table, form, productButtons);

        Tab productsTab = new Tab("Products", productsLayout);
        productsTab.setClosable(false);

        // ── ORDER TABLE COLUMNS ──
        TableColumn<CustomerOrder, String> orderIdCol = new TableColumn<>("ID");
        orderIdCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<CustomerOrder, String> clientCol = new TableColumn<>("Client");
        clientCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getClientName()));

        TableColumn<CustomerOrder, String> itemsCol = new TableColumn<>("Items");
        itemsCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getItems()));
        itemsCol.setPrefWidth(250);

        TableColumn<CustomerOrder, String> totalCol = new TableColumn<>("Total (DA)");
        totalCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format("%.2f", data.getValue().getTotal())));

        TableColumn<CustomerOrder, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        TableColumn<CustomerOrder, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCreatedAt()));

        orderTable.getColumns().addAll(orderIdCol, clientCol, itemsCol, totalCol, statusCol, dateCol);

        // ── ORDER BUTTONS ──
        Button confirmBtn     = new Button("Mark Confirmed");
        Button doneBtn        = new Button("Mark Done");
        Button refreshOrders  = new Button("Refresh Orders");

        confirmBtn.setOnAction(e -> {
            CustomerOrder selected = orderTable.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            try {
                ApiClient.updateOrderStatus(selected.getId(), "CONFIRMED");
                loadOrders();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        doneBtn.setOnAction(e -> {
            CustomerOrder selected = orderTable.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            try {
                ApiClient.updateOrderStatus(selected.getId(), "DONE");
                loadOrders();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        refreshOrders.setOnAction(e -> loadOrders());

        HBox orderButtons = new HBox(10, refreshOrders, confirmBtn, doneBtn);
        VBox ordersLayout = new VBox(10, orderTable, orderButtons);

        Tab ordersTab = new Tab("Orders", ordersLayout);
        ordersTab.setClosable(false);

        // Reload orders when the tab is opened
        ordersTab.setOnSelectionChanged(e -> {
            if (ordersTab.isSelected()) loadOrders();
        });

        // ── TAB PANE ──
        TabPane tabPane = new TabPane(productsTab, ordersTab);

        stage.setScene(new Scene(tabPane, 900, 600));
        stage.setTitle("Supermarket Admin");
        stage.show();

        loadProducts();
        loadOrders();

        // Auto-refresh orders every 10 seconds
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
                () -> Platform.runLater(this::loadOrders),
                10, 10, TimeUnit.SECONDS
        );
    }

    private void loadProducts() {
        try {
            table.getItems().setAll(ApiClient.getAllProducts());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadOrders() {
        try {
            List<CustomerOrder> orders = ApiClient.getOrders();
            orderTable.getItems().setAll(orders);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void stop() {
        if (scheduler != null) scheduler.shutdownNow();
    }

    public static void main(String[] args) {
        launch();
    }
}