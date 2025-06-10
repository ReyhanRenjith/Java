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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class OrdersController {

    @FXML private TableView<Product> ordersTable;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, String> descCol;
    @FXML private TableColumn<Product, Double> priceCol;

    @FXML
    public void initialize() {
        // Setup table columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load data
        DatabaseManager dbManager = Main.getDbManager();
        int userId = SessionManager.getCurrentUser().getId();
        ObservableList<Product> orders = FXCollections.observableArrayList(dbManager.getOrdersForUser(userId));
        ordersTable.setItems(orders);

        if (orders.isEmpty()) {
            ordersTable.setPlaceholder(new Label("You have not purchased any items yet."));
        }
    }

    @FXML
    private void onBackToDashboardClick() throws IOException {
        new Main().changeScene("dashboard-view.fxml", "Dashboard");
    }
}
