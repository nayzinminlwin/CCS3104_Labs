package Project_AnnouncementBoard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewEventPage extends Application {

    private Connection connection; // Database connection for this session
    private String matricNum; // Admin user's matric number (from login)
    private String userName; // Admin user's name (fetched from database)
    private File selectedImageFile; // Currently selected image file
    private ImageView imagePreview; // Preview widget for selected image
    private Label imageStatusLabel; // Label showing image selection status

    // Constructor to accept matric number
    public NewEventPage() {
        this.matricNum = "A231198"; // Default for testing
    }

    public NewEventPage(String matricNum) {
        this.matricNum = matricNum;
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize database connection
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }

        // Add cleanup handler when window closes
        primaryStage.setOnCloseRequest(e -> cleanup());

        // Fetch user info
        fetchUserInfo();

        // Main container
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #fce4ec;");

        // Top header with user info
        HBox header = createHeader();
        root.setTop(header);

        // Center content area
        VBox contentArea = createContentArea(primaryStage);
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #fce4ec; -fx-background-color: #fce4ec;");
        root.setCenter(scrollPane);

        // Create scene
        Scene scene = new Scene(root, 700, 700);
        primaryStage.setTitle("Create New Event - UPM Announcement Board");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the header with user info
     */
    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setPadding(new Insets(20, 25, 20, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: #e91e63; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        // User icon
        Label userIcon = new Label("👤");
        userIcon.setFont(Font.font(35));
        userIcon.setStyle("-fx-background-color: white; -fx-background-radius: 50%; -fx-padding: 8;");

        // User info
        VBox userInfo = new VBox(3);
        Label welcomeLabel = new Label("Welcome back,");
        welcomeLabel.setFont(Font.font("Segoe UI", 13));
        welcomeLabel.setStyle("-fx-text-fill: #fce4ec; -fx-font-smoothing-type: lcd;");

        Label nameLabel = new Label(userName != null ? userName : "Admin User");
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-smoothing-type: lcd;");

        userInfo.getChildren().addAll(welcomeLabel, nameLabel);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Title
        Label titleLabel = new Label("Create New Event");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-smoothing-type: lcd;");

        header.getChildren().addAll(userIcon, userInfo, spacer, titleLabel);
        return header;
    }

    /**
     * Creates the main content area with form
     */
    private VBox createContentArea(Stage primaryStage) {
        VBox contentArea = new VBox(25);
        contentArea.setPadding(new Insets(30, 50, 30, 50));
        contentArea.setAlignment(Pos.TOP_CENTER);

        // Form card
        VBox formCard = new VBox(20);
        formCard.setMaxWidth(600);
        formCard.setPadding(new Insets(30));
        formCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 5);");

        // Caption section
        Label captionLabel = new Label("Event Description");
        captionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        captionLabel.setStyle("-fx-text-fill: #050505;");

        TextArea captionArea = new TextArea();
        captionArea.setPromptText("Describe your event here... (e.g., event details, date, time, venue)");
        captionArea.setPrefRowCount(8);
        captionArea.setWrapText(true);
        captionArea.setFont(Font.font("Segoe UI", 14));
        captionArea.setStyle(
                "-fx-control-inner-background: white; " +
                        "-fx-background-color: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-padding: 12; " +
                        "-fx-background-insets: 0; " +
                        "-fx-border-insets: 0;");

        VBox captionBox = new VBox(10);
        captionBox.getChildren().addAll(captionLabel, captionArea);

        // Category section
        Label categoryLabel = new Label("Event Category (Optional)");
        categoryLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        categoryLabel.setStyle("-fx-text-fill: #050505;");

        TextField categoryField = new TextField();
        categoryField.setPromptText("e.g., Career, Academic, Sports, Cultural");
        categoryField.setFont(Font.font("Segoe UI", 14));
        categoryField.setStyle(
                "-fx-control-inner-background: white; " +
                        "-fx-background-color: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-padding: 12; " +
                        "-fx-background-insets: 0; " +
                        "-fx-border-insets: 0;");

        VBox categoryBox = new VBox(10);
        categoryBox.getChildren().addAll(categoryLabel, categoryField);

        // Share Link section
        Label shareLinkLabel = new Label("Share Link / Register Link (Optional)");
        shareLinkLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        shareLinkLabel.setStyle("-fx-text-fill: #050505;");

        TextField shareLinkField = new TextField();
        shareLinkField.setPromptText("e.g., https://forms.gle/abc123 or https://event-website.com");
        shareLinkField.setFont(Font.font("Segoe UI", 14));
        shareLinkField.setStyle(
                "-fx-control-inner-background: white; " +
                        "-fx-background-color: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-padding: 12; " +
                        "-fx-background-insets: 0; " +
                        "-fx-border-insets: 0;");

        VBox shareLinkBox = new VBox(10);
        shareLinkBox.getChildren().addAll(shareLinkLabel, shareLinkField);

        // Image upload section
        Label imageLabel = new Label("Event Image");
        imageLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        imageLabel.setStyle("-fx-text-fill: #050505;");

        Button uploadButton = new Button("📷 Choose Image");
        uploadButton.setPrefHeight(45);
        uploadButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        uploadButton.setStyle(
                "-fx-background-color: #fce4ec; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #e91e63; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-cursor: hand;");

        uploadButton.setOnMouseEntered(e -> uploadButton.setStyle(
                "-fx-background-color: #f8bbd0; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #e91e63; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-cursor: hand;"));

        uploadButton.setOnMouseExited(e -> uploadButton.setStyle(
                "-fx-background-color: #fce4ec; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #e91e63; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-cursor: hand;"));

        // Image status label
        imageStatusLabel = new Label("No image selected");
        imageStatusLabel.setFont(Font.font("Segoe UI", 12));
        imageStatusLabel.setStyle("-fx-text-fill: #65676b;");

        // Image preview
        imagePreview = new ImageView();
        imagePreview.setFitWidth(500);
        imagePreview.setPreserveRatio(true);
        imagePreview.setVisible(false);
        imagePreview.setManaged(false); // Don't take up space when invisible
        imagePreview.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Event Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                selectedImageFile = file;
                imageStatusLabel.setText("✓ " + file.getName());
                imageStatusLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-weight: bold;");

                // Show preview
                try {
                    Image image = new Image(new FileInputStream(file));
                    imagePreview.setImage(image);
                    imagePreview.setVisible(true);
                    imagePreview.setManaged(true); // Take up space when visible
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox imageBox = new VBox(10);
        imageBox.getChildren().addAll(imageLabel, uploadButton, imageStatusLabel, imagePreview);

        // Status message label
        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Segoe UI", 13));
        statusLabel.setWrapText(true);
        statusLabel.setVisible(false);

        // Publish button
        Button publishButton = new Button("📢 Publish Event");
        publishButton.setPrefHeight(50);
        publishButton.setPrefWidth(Double.MAX_VALUE);
        publishButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        publishButton.setStyle(
                "-fx-background-color: #e91e63; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;");

        publishButton.setOnMouseEntered(e -> publishButton.setStyle(
                "-fx-background-color: #c2185b; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"));

        publishButton.setOnMouseExited(e -> publishButton.setStyle(
                "-fx-background-color: #e91e63; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"));

        publishButton.setOnAction(e -> {
            String caption = captionArea.getText().trim();
            String category = categoryField.getText().trim();
            String shareLink = shareLinkField.getText().trim();

            if (caption.isEmpty()) {
                statusLabel.setText("⚠️ Please enter event description");
                statusLabel.setStyle("-fx-text-fill: #e4002b;");
                statusLabel.setVisible(true);
                return;
            }

            if (selectedImageFile == null) {
                statusLabel.setText("⚠️ Please select an event image");
                statusLabel.setStyle("-fx-text-fill: #e4002b;");
                statusLabel.setVisible(true);
                return;
            }

            // Publish event
            if (publishEvent(caption, category, shareLink, selectedImageFile)) {
                // Show success dialog
                showSuccessDialog();

                // Clear form completely
                captionArea.clear();
                categoryField.clear();
                shareLinkField.clear();
                selectedImageFile = null;
                imageStatusLabel.setText("No image selected");
                imageStatusLabel.setStyle("-fx-text-fill: #65676b;");
                imagePreview.setImage(null); // Clear the image
                imagePreview.setVisible(false);
                imagePreview.setManaged(false); // Remove space
                statusLabel.setVisible(false);
            } else {
                statusLabel.setText("⚠️ Failed to publish event. Please try again.");
                statusLabel.setStyle("-fx-text-fill: #e4002b;");
                statusLabel.setVisible(true);
            }
        });

        // Add all components to form card
        formCard.getChildren().addAll(
                captionBox,
                categoryBox,
                shareLinkBox,
                imageBox,
                statusLabel,
                publishButton);

        contentArea.getChildren().add(formCard);
        return contentArea;
    }

    /**
     * Fetches user info from database
     */
    private void fetchUserInfo() {
        if (connection == null)
            return;

        String query = "SELECT NAME FROM AB_USERS WHERE MATRIC_NUM = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, matricNum);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    userName = resultSet.getString("NAME");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user info: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Publishes event to database using PreparedStatement
     */
    private boolean publishEvent(String caption, String category, String shareLink, File imageFile) {
        if (connection == null) {
            System.err.println("Database connection failed");
            return false;
        }

        try {
            // First, save image to filesystem
            String imagePath = saveImage(imageFile);
            if (imagePath == null) {
                System.err.println("Failed to save image file");
                return false;
            }

            // Get next image ID
            int imageId = 1;
            String imageIdQuery = "SELECT NVL(MAX(IMAGE_ID), 0) + 1 AS NEXT_ID FROM AB_IMAGES";
            try (Statement stmt = connection.createStatement();
                    ResultSet imageIdResult = stmt.executeQuery(imageIdQuery)) {
                if (imageIdResult.next()) {
                    imageId = imageIdResult.getInt("NEXT_ID");
                }
            }

            // Detect MIME type from file extension
            String mimeType = "application/octet-stream"; // Default
            String fileName = imageFile.getName().toLowerCase();
            if (fileName.endsWith(".png")) {
                mimeType = "image/png";
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                mimeType = "image/jpeg";
            } else if (fileName.endsWith(".gif")) {
                mimeType = "image/gif";
            }

            // Insert image record using PreparedStatement
            String imageInsert = "INSERT INTO AB_IMAGES (IMAGE_ID, FILE_NAME, FILE_PATH, MIME_TYPE, UPLOADED_AT) " +
                    "VALUES (?, ?, ?, ?, SYSTIMESTAMP)";
            try (PreparedStatement pstmt = connection.prepareStatement(imageInsert)) {
                pstmt.setInt(1, imageId);
                pstmt.setString(2, imageFile.getName());
                pstmt.setString(3, imagePath);
                pstmt.setString(4, mimeType);
                pstmt.executeUpdate();
            }

            // Get next post ID
            int postId = 1;
            String postIdQuery = "SELECT NVL(MAX(POST_ID), 0) + 1 AS NEXT_ID FROM AB_POSTS";
            try (Statement stmt = connection.createStatement();
                    ResultSet postIdResult = stmt.executeQuery(postIdQuery)) {
                if (postIdResult.next()) {
                    postId = postIdResult.getInt("NEXT_ID");
                }
            }

            // Insert post record using PreparedStatement
            String postInsert = "INSERT INTO AB_POSTS (POST_ID, MATRIC_NUM, CAPTION, IMAGE_ID, CATEGORY, SHARE_SLUG, ACTIVE_STATUS, UPLOAD_TIME) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, 1, SYSTIMESTAMP)";
            try (PreparedStatement pstmt = connection.prepareStatement(postInsert)) {
                pstmt.setInt(1, postId);
                pstmt.setString(2, matricNum);
                pstmt.setString(3, caption);
                pstmt.setInt(4, imageId);
                if (category.isEmpty()) {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                } else {
                    pstmt.setString(5, category);
                }
                if (shareLink.isEmpty()) {
                    pstmt.setNull(6, java.sql.Types.VARCHAR);
                } else {
                    pstmt.setString(6, shareLink);
                }
                pstmt.executeUpdate();
            }

            System.out.println("Event published successfully!");
            return true;

        } catch (SQLException e) {
            System.err.println("Database error while publishing event: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves image file to local directory with proper error handling
     */
    private String saveImage(File sourceFile) {
        try {
            // Create images directory if it doesn't exist
            String imagesDir = System.getProperty("user.dir") + "/HelloFX/src/Project_AnnouncementBoard/images/";
            File dir = new File(imagesDir);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    System.err.println("Failed to create images directory: " + imagesDir);
                    return null;
                }
            }

            // Validate source file
            if (!sourceFile.exists() || !sourceFile.canRead()) {
                System.err.println("Source file does not exist or cannot be read: " + sourceFile.getAbsolutePath());
                return null;
            }

            // Generate unique filename
            String fileName = System.currentTimeMillis() + "_" + sourceFile.getName();
            File destFile = new File(imagesDir + fileName);

            // Copy file
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image saved successfully: " + destFile.getAbsolutePath());

            // Return relative path for database
            return "images/" + fileName;

        } catch (IOException e) {
            System.err.println("Error saving image file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cleanup database resources
     */
    private void cleanup() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("NewEventPage: Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Shows success dialog overlay
     */
    private void showSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Event Published Successfully! 🎉");
        alert.setContentText(
                "Your event has been published to the UPM Announcement Board.\nStudents can now view it on the home page.");

        // Style the dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: white; " +
                        "-fx-font-family: 'Segoe UI';");

        // Style header
        dialogPane.lookup(".header-panel").setStyle(
                "-fx-background-color: #e91e63; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold;");

        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        }

        // Style content
        dialogPane.lookup(".content").setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 20;");

        // Style button
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(
                "-fx-background-color: #e91e63; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 30 10 30; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;");

        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Launch HomePage as the single entry point
        HomePage.main(args);
    }
}
