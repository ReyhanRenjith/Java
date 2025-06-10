package com.java.revaniexchange.controller;

import com.java.revaniexchange.Main;
import com.java.revaniexchange.SessionManager;
import com.java.revaniexchange.model.User;
import com.java.revaniexchange.repository.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    void onLoginButtonClick(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            return;
        }

        DatabaseManager dbManager = Main.getDbManager();
        Optional<User> userOpt = dbManager.validateLogin(username, password);

        if (userOpt.isPresent()) {
            // Set user in session
            SessionManager.setCurrentUser(userOpt.get());
            // Switch to dashboard
            Main main = new Main();
            main.changeScene("dashboard-view.fxml", "Dashboard");
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    void onSignUpButtonClick(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("signup-view.fxml", "Sign Up");
    }
}
