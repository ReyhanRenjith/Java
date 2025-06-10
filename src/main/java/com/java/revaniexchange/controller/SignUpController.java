package com.java.revaniexchange.controller;

import com.java.revaniexchange.repository.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    private final DatabaseManager dbManager = new DatabaseManager();

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password cannot be empty.");
            return;
        }
        boolean success = dbManager.registerUser(username, password);
        if (success) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Registration successful! You can now log in.");
            usernameField.clear();
            passwordField.clear();
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Username already exists or error occurred.");
        }
    }

    @FXML
    private void handleLoginRedirect() {
        try {
            com.java.revaniexchange.Main main = new com.java.revaniexchange.Main();
            main.changeScene("login-view.fxml", "Login");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading login page.");
        }
    }
}
