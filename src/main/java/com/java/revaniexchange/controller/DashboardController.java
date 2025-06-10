package com.java.revaniexchange.controller;

import com.java.revaniexchange.Main;
import com.java.revaniexchange.SessionManager;
import com.java.revaniexchange.model.Product;
import com.java.revaniexchange.repository.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, Double> priceCol;

    @FXML
    public void initialize() {
        // Set welcome message
        welcomeLabel.setText("Welcome, " + SessionManager.getCurrentUser().getUsername() + "!");

        // Setup table columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load data
        DatabaseManager dbManager = Main.getDbManager();
        ObservableList<Product> products = FXCollections.observableArrayList(dbManager.getAllProducts());
        productTable.setItems(products);

        // Add listener for row double-clicks
        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Product rowData = row.getItem();
                    try {
                        new Main().showProductDetail(rowData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    @FXML
    private void onShowOrdersClick() throws IOException {
        new Main().changeScene("orders-view.fxml", "My Orders");
    }

    @FXML
    private void onLogoutClick() throws IOException {
        SessionManager.clearSession();
        new Main().changeScene("login-view.fxml", "Login");
    }
}
