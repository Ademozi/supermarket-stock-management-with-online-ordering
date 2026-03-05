package com.supermarket.desktop;

import com.supermarket.desktop.api.ApiClient;
import com.supermarket.desktop.model.Product;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {

    private TableView<Product> table = new TableView<>();

    @Override
    public void start(Stage stage) throws Exception {

        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField quantityField = new TextField();
        TextField barcodeField = new TextField();
        TextField categoryField = new TextField();

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

                loadProducts(); // refresh table

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        TableColumn<Product, String> nameCol =
                new TableColumn<>("Name");
        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getName()));

        TableColumn<Product, String> priceCol =
                new TableColumn<>("Price");
        priceCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(data.getValue().getPrice())));

        table.getColumns().addAll(nameCol, priceCol);

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, selected) -> {

                    if (selected != null) {

                        nameField.setText(selected.getName());
                        priceField.setText(String.valueOf(selected.getPrice()));
                        quantityField.setText(String.valueOf(selected.getQuantity()));
                        barcodeField.setText(selected.getBarcode());
                        categoryField.setText(selected.getCategory());

                    }
                });

        // REFRESH
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadProducts());

        VBox form = new VBox(
                new Label("Name"), nameField,
                new Label("Price"), priceField,
                new Label("Quantity"), quantityField,
                new Label("Barcode"), barcodeField,
                new Label("Category"), categoryField,
                addButton
        );

        // DELETE
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(e -> {

            Product selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                System.out.println("Select a product first");
                return;
            }

            try {
                ApiClient.deleteProduct(selected.getId());
                loadProducts();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //UPDATE
        Button updateButton = new Button("Update");

        updateButton.setOnAction(e -> {

            Product selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                System.out.println("No product selected");
                return;
            }

            try {

                selected.setName(nameField.getText());
                selected.setPrice(Double.parseDouble(priceField.getText()));
                selected.setQuantity(Integer.parseInt(quantityField.getText()));
                selected.setBarcode(barcodeField.getText());
                selected.setCategory(categoryField.getText());

                ApiClient.updateProduct(selected);

                loadProducts();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox hbox = new HBox(10, refreshBtn, deleteButton, updateButton);

        VBox root = new VBox(10, table, form, hbox);

        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Supermarket Stock");
        stage.show();

        loadProducts();
    }

    private void loadProducts() {
        try {
            List<Product> products = ApiClient.getAllProducts();
            table.getItems().setAll(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch();
    }
}