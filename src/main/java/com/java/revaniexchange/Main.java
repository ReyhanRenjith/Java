package com.java.revaniexchange;

import com.java.revaniexchange.controller.ProductDetailController;
import com.java.revaniexchange.model.Product;
import com.java.revaniexchange.repository.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stg;
    private static DatabaseManager dbManager;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stg = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("JavaFX Product Store");

        // Initialize Database
        dbManager = new DatabaseManager();
        dbManager.initDatabaseAndSeed();

        // Load initial FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Changes the current scene of the application.
     * @param fxml The FXML file to load.
     * @param title The new title for the window.
     */
    public void changeScene(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxml));
        Parent pane = loader.load();
        stg.getScene().setRoot(pane);
        stg.setTitle(title);
    }

    /**
     * Changes to the Product Detail scene, passing the selected product.
     * @param product The product to display.
     */
    public void showProductDetail(Product product) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/product-detail-view.fxml"));
        Parent pane = loader.load();

        // Get controller and pass data
        ProductDetailController controller = loader.getController();
        controller.initData(product);

        stg.getScene().setRoot(pane);
        stg.setTitle("Product Details: " + product.getName());
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        // Gracefully close the database connection
        if (dbManager != null) {
            dbManager.closeConnection();
        }
    }

    public static DatabaseManager getDbManager() {
        return dbManager;
    }
}
