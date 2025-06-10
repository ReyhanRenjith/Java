package com.java.revaniexchange.controller;

import com.java.revaniexchange.Main;
import com.java.revaniexchange.SessionManager;
import com.java.revaniexchange.model.Product;
import com.java.revaniexchange.repository.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ProductDetailController {

    @FXML private Label nameLabel;
    @FXML private ImageView productImageView;
    @FXML private Label priceLabel;
    @FXML private Label descriptionLabel;
    @FXML private Button buyButton;

    private Product currentProduct;

    public void initData(Product product) {
        currentProduct = product;
        nameLabel.setText(product.getName());
        priceLabel.setText(String.format("$%.2f", product.getPrice()));
        descriptionLabel.setText(product.getDescription());

        try {
            productImageView.setImage(new Image(product.getImageUrl(), 300, 300, true, true, true));
        } catch (Exception e) {
            productImageView.setImage(new Image("https://placehold.co/300x300/e2e8f0/64748b?text=No+Image", 300, 300, true, true));
        }
    }

    @FXML
    private void onBuyProductClick() throws IOException {
        DatabaseManager dbManager = Main.getDbManager();
        dbManager.placeOrder(SessionManager.getCurrentUser().getId(), currentProduct.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully purchased " + currentProduct.getName() + "!");
        alert.showAndWait();

        onBackToDashboardClick();
    }

    @FXML
    private void onBackToDashboardClick() throws IOException {
        new Main().changeScene("dashboard-view.fxml", "Dashboard");
    }
}
