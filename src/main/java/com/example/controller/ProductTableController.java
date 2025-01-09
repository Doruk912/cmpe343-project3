package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import com.example.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class ProductTableController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        DatabaseConnection db = new DatabaseConnection();
        List<Product> products = db.getAllProducts();
        productList.setAll(products);
        productTable.setItems(productList);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) productTable.getScene().getWindow();
        stage.close();
    }

    public void handleAddProduct(ActionEvent actionEvent) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter product details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField quantityField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Quantity:"), 0, 2);
        grid.add(quantityField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String name = nameField.getText().trim();
                BigDecimal price;
                int quantity;

                try {
                    price = new BigDecimal(priceField.getText().trim());
                    quantity = Integer.parseInt(quantityField.getText().trim());
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Price must be a valid number and quantity must be an integer.");
                    return null;
                }

                if (!name.isEmpty() && price.compareTo(BigDecimal.ZERO) > 0 && quantity > 0) {
                    return new Product(name, price, quantity);
                } else {
                    showAlert("Invalid Input", "All fields must be filled with valid values.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(product -> {
            DatabaseConnection db = new DatabaseConnection();
            if (db.addProduct(product)) {
                productList.add(product);
                showInfo("Success", "Product added successfully!");
            } else {
                showAlert("Error", "Failed to add the product.");
            }
        });
    }

    public void handleEditProduct(ActionEvent actionEvent) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("No Selection", "Please select a product to edit.");
            return;
        }

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Edit product details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(selectedProduct.getName());
        TextField priceField = new TextField(selectedProduct.getPrice().toString());
        TextField quantityField = new TextField(String.valueOf(selectedProduct.getQuantity()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Quantity:"), 0, 2);
        grid.add(quantityField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String name = nameField.getText().trim();
                BigDecimal price;
                int quantity;

                try {
                    price = new BigDecimal(priceField.getText().trim());
                    quantity = Integer.parseInt(quantityField.getText().trim());
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Price must be a valid number and quantity must be an integer.");
                    return null;
                }

                if (!name.isEmpty() && price.compareTo(BigDecimal.ZERO) > 0 && quantity >= 0) {
                    selectedProduct.setName(name);
                    selectedProduct.setPrice(price);
                    selectedProduct.setQuantity(quantity);
                    return selectedProduct;
                } else {
                    showAlert("Invalid Input", "All fields must be filled with valid values.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(product -> {
            DatabaseConnection db = new DatabaseConnection();
            if (db.updateProduct(product)) { //False dönüyor
                productTable.refresh();
                showInfo("Success", "Product updated successfully!");
            } else {
                showAlert("Error", "Failed to update the product.");
            }
        });

    }

    public void handleDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("No Selection", "Please select a product to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        if(selectedProduct.getQuantity() > 0){
            alert.setHeaderText("Are you sure you want to delete this product? It has remaining stock.");
        }else{
            alert.setHeaderText("Are you sure you want to delete this product?");
        }

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                DatabaseConnection db = new DatabaseConnection();
                if (db.deleteProduct(selectedProduct)) {
                    productList.remove(selectedProduct);
                    showInfo("Success", "Product deleted successfully!");
                } else {
                    showAlert("Error", "Failed to delete the product.");
                }
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
