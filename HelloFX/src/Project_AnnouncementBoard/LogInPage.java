package Project_AnnouncementBoard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInPage extends Application {

    private Connection connection; // Database connection for authentication
    private static Stage newEventStageInstance; // Track NewEventPage window (singleton)

    @Override
    public void start(Stage primaryStage) {
        // Initialize database connection once
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }

        // Add cleanup handler when window closes
        primaryStage.setOnCloseRequest(e -> cleanup());

        // Main container
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #fce4ec;");

        // Center card container
        VBox loginCard = new VBox(20);
        loginCard.setMaxWidth(400);
        loginCard.setPadding(new Insets(40));
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 15, 0, 0, 5);");

        // Title
        Label titleLabel = new Label("Admin Login");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #e91e63; -fx-font-smoothing-type: lcd;");

        Label subtitleLabel = new Label("Sign in to upload events");
        subtitleLabel.setFont(Font.font("Segoe UI", 14));
        subtitleLabel.setStyle("-fx-text-fill: #65676b; -fx-font-smoothing-type: lcd;");

        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Matric Number field
        Label matricLabel = new Label("Matric Number");
        matricLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        matricLabel.setStyle("-fx-text-fill: #050505;");

        TextField matricField = new TextField();
        matricField.setPromptText("e.g., A123456");
        matricField.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 12; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-radius: 6; " +
                        "-fx-border-width: 1.5;");
        matricField.setFont(Font.font("Segoe UI", 14));

        VBox matricBox = new VBox(8);
        matricBox.getChildren().addAll(matricLabel, matricField);

        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        passwordLabel.setStyle("-fx-text-fill: #050505;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 12; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-radius: 6; " +
                        "-fx-border-width: 1.5;");
        passwordField.setFont(Font.font("Segoe UI", 14));

        VBox passwordBox = new VBox(8);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Error message label (initially hidden)
        Label errorLabel = new Label();
        errorLabel.setFont(Font.font("Segoe UI", 12));
        errorLabel.setStyle("-fx-text-fill: #e4002b; -fx-font-smoothing-type: lcd;");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(45);
        loginButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        loginButton.setStyle(
                "-fx-background-color: #e91e63; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand;");

        // Hover effect
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: #c2185b; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand;"));

        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: #e91e63; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand;"));

        // Login action
        loginButton.setOnAction(e -> {
            String matric = matricField.getText().trim();
            String password = passwordField.getText();

            if (matric.isEmpty() || password.isEmpty()) {
                errorLabel.setText("⚠️ Please fill in all fields");
                errorLabel.setVisible(true);
                return;
            }

            // === AUTHENTICATE USER ===
            if (authenticateUser(matric, password)) {
                errorLabel.setVisible(false);
                System.out.println("Login successful! Opening NewEventPage...");

                // === SINGLETON CHECK: Prevent duplicate NewEventPage windows ===
                // If NewEventPage already open, bring it to front instead of creating new one
                if (newEventStageInstance != null && newEventStageInstance.isShowing()) {
                    newEventStageInstance.requestFocus();
                    newEventStageInstance.toFront();
                    System.out.println("NewEventPage already open - bringing to front");
                    primaryStage.close(); // Close login window
                    return;
                }

                // === OPEN NEW EVENT PAGE ===
                primaryStage.close(); // Close login window first

                try {
                    NewEventPage newEventPage = new NewEventPage(matric); // Pass matric for user identification
                    newEventStageInstance = new Stage();
                    // Clear reference when NewEventPage closes
                    newEventStageInstance.setOnHidden(event -> newEventStageInstance = null);
                    newEventPage.start(newEventStageInstance);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                errorLabel.setText("⚠️ Invalid matric number or password");
                errorLabel.setVisible(true);
            }
        });

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(Double.MAX_VALUE);
        cancelButton.setPrefHeight(40);
        cancelButton.setFont(Font.font("Segoe UI", 14));
        cancelButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #65676b; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand;");

        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(
                "-fx-background-color: #f5f5f5; " +
                        "-fx-text-fill: #65676b; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand;"));

        cancelButton.setOnMouseExited(e -> cancelButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #65676b; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand;"));

        cancelButton.setOnAction(e -> {
            primaryStage.close();
        });

        // Add all components to login card
        loginCard.getChildren().addAll(
                titleBox,
                matricBox,
                passwordBox,
                errorLabel,
                loginButton,
                cancelButton);

        // Center the card
        StackPane centerContainer = new StackPane(loginCard);
        centerContainer.setPadding(new Insets(50));
        root.setCenter(centerContainer);

        // Create scene
        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Admin Login - UPM Announcement Board");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateUser(String matricNum, String password) {
        if (connection == null) {
            System.err.println("Database connection failed");
            return false;
        }

        // Use PreparedStatement to prevent SQL injection
        String query = "SELECT MATRIC_NUM, PASSWORD_HASH, ROLE FROM AB_USERS " +
                "WHERE MATRIC_NUM = ? AND PASSWORD_HASH = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, matricNum);
            pstmt.setString(2, password); // Note: In production, use proper password hashing!

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("User authenticated: " + resultSet.getString("MATRIC_NUM"));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Cleanup database resources
     */
    private void cleanup() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("LogInPage: Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Main entry point - Redirects to HomePage
     * 
     * All program files now redirect to HomePage as the single entry point.
     * This ensures consistent application start regardless of which file is run.
     * 
     * @param args Command line arguments (unused)
     */
    public static void main(String[] args) {
        // Launch HomePage as the single entry point
        HomePage.main(args);
    }
}
