package Project_AnnouncementBoard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePage extends Application {

    private VBox postArea; // Container for all post cards
    private Connection connection; // Database connection for this session

    // Singleton tracking: Prevent duplicate windows
    private static Stage primaryStageInstance; // Track the main HomePage window
    private static Stage loginStageInstance; // Track login window (only one allowed)

    @Override
    public void start(Stage primaryStage) {
        // Store reference to primary stage for singleton management
        primaryStageInstance = primaryStage;

        // === DATABASE INITIALIZATION ===
        // Create fresh connection for this session
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }

        // === CLEANUP HANDLER ===
        // Ensure database connection is closed when window closes
        primaryStage.setOnCloseRequest(e -> {
            cleanup();
            primaryStageInstance = null; // Clear singleton reference
        });

        // === UI LAYOUT ===
        // Main container with pink background (#fce4ec)
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #fce4ec;");

        // === HEADER SECTION ===
        // Contains: UPM Logo, Title, Refresh button, Admin Login button
        HBox heading = createHeading(primaryStage);
        root.setTop(heading);

        // === POST FEED SECTION ===
        // Scrollable container for post cards
        postArea = new VBox(15); // 15px spacing between posts
        postArea.setPadding(new Insets(20));
        postArea.setAlignment(Pos.TOP_CENTER);
        postArea.setStyle("-fx-background-color: #fce4ec;");

        ScrollPane scrollPane = new ScrollPane(postArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #fce4ec; -fx-background-color: #fce4ec;");

        root.setCenter(scrollPane);

        // === LOAD POSTS FROM DATABASE ===
        loadPosts();

        // === CREATE AND SHOW WINDOW ===
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("UPM Announcement Board");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeading(Stage primaryStage) {
        HBox heading = new HBox();
        heading.setPadding(new Insets(15, 20, 15, 20));
        heading.setAlignment(Pos.CENTER_LEFT);
        heading.setStyle(
                "-fx-background-color: #e91e63; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        // UPM Logo with rounded corners
        StackPane logoContainer = null;
        try {
            String logoPath = System.getProperty("user.dir") +
                    "/HelloFX/src/Project_AnnouncementBoard/images/UPM_Logo.jpg";
            // Load image at higher resolution with quality settings enabled
            // requestedWidth, requestedHeight, preserveRatio, smooth
            Image logoImage = new Image(new FileInputStream(logoPath), 200, 0, true, true);
            ImageView upmLogo = new ImageView(logoImage);
            upmLogo.setFitHeight(55); // Slightly larger for better clarity
            upmLogo.setPreserveRatio(true);
            upmLogo.setSmooth(true); // Enable smooth/high-quality rendering
            upmLogo.setCache(true); // Cache for better performance

            // Wrap in StackPane for rounded corners
            logoContainer = new StackPane(upmLogo);
            logoContainer.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-background-radius: 8; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 0, 1);");
            logoContainer.setPadding(new Insets(3));

        } catch (FileNotFoundException e) {
            System.err.println("UPM Logo not found: " + e.getMessage());
        }

        // Spacer between logo and title
        Region logoSpacer = new Region();
        logoSpacer.setPrefWidth(15);

        // Title
        Label title = new Label("UPM Announcement Board");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white; -fx-font-smoothing-type: lcd;");

        // Spacer to push buttons to right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Refresh button with icon
        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 6 12 6 12; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;");

        // Refresh hover effect
        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle(
                "-fx-background-color: #fce4ec; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 6 12 6 12; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"));

        refreshButton.setOnMouseExited(e -> refreshButton.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 6 12 6 12; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"));

        // Refresh button action
        refreshButton.setOnAction(e -> refreshPosts());

        // Spacer between refresh and login buttons
        Region buttonSpacer = new Region();
        buttonSpacer.setPrefWidth(10);

        // Login button with icon
        Button loginButton = new Button("👤 Admin Login");
        loginButton.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;");

        // Hover effect
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: #fce4ec; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"));

        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #e91e63; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"));

        // === ADMIN LOGIN BUTTON ACTION ===
        // Singleton pattern: Only one login window allowed at a time
        loginButton.setOnAction(e -> {
            // Check if login window already exists and is open
            if (loginStageInstance != null && loginStageInstance.isShowing()) {
                // Bring existing window to front instead of creating duplicate
                loginStageInstance.requestFocus();
                loginStageInstance.toFront();
                System.out.println("Login window already open - bringing to front");
                return;
            }

            try {
                loginStageInstance = new Stage();
                // Clear reference when window closes
                loginStageInstance.setOnHidden(event -> loginStageInstance = null);
                new LogInPage().start(loginStageInstance);
            } catch (Exception ex) {
                System.err.println("Error opening login page: " + ex.getMessage());
            }
        });

        // Add components to heading
        if (logoContainer != null) {
            heading.getChildren().addAll(logoContainer, logoSpacer, title, spacer, refreshButton, buttonSpacer,
                    loginButton);
        } else {
            heading.getChildren().addAll(title, spacer, refreshButton, buttonSpacer, loginButton);
        }
        return heading;
    }

    private void refreshPosts() {
        System.out.println("Refreshing posts...");
        System.out.println("Current number of posts before clear: " + postArea.getChildren().size());

        // Close old connection and create a new one to see fresh data
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Old connection closed");
            }
            connection = DBConnection.getConnection();
            System.out.println("New connection created for refresh");
        } catch (SQLException e) {
            System.err.println("Error reconnecting to database: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Clear existing posts
        postArea.getChildren().clear();
        System.out.println("Posts cleared. Current size: " + postArea.getChildren().size());

        // Reload posts from database
        loadPosts();
        System.out.println("After loadPosts(). New number of posts: " + postArea.getChildren().size());

        // Force layout update
        postArea.layout();
        postArea.requestLayout();
    }

    private void loadPosts() {
        if (connection == null) {
            showErrorMessage("Database connection failed. Please check your connection.");
            return;
        }

        // Use try-with-resources to automatically close Statement and ResultSet
        String query = "SELECT p.POST_ID, p.CAPTION, p.UPLOAD_TIME, p.CATEGORY, p.SHARE_SLUG, " +
                "u.NAME, u.FACULTY, " +
                "i.FILE_PATH, i.FILE_NAME, i.WIDTH_PX, i.HEIGHT_PX " +
                "FROM AB_POSTS p " +
                "LEFT JOIN AB_USERS u ON p.MATRIC_NUM = u.MATRIC_NUM " +
                "LEFT JOIN AB_IMAGES i ON p.IMAGE_ID = i.IMAGE_ID " +
                "WHERE p.ACTIVE_STATUS = ? " +
                "ORDER BY p.UPLOAD_TIME DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 1); // ACTIVE_STATUS = 1

            try (ResultSet resultSet = pstmt.executeQuery()) {

                boolean hasResults = false;
                while (resultSet.next()) {
                    hasResults = true;

                    // Get data from result set
                    int postId = resultSet.getInt("POST_ID");
                    String caption = resultSet.getString("CAPTION");
                    String uploadTime = resultSet.getString("UPLOAD_TIME");
                    String category = resultSet.getString("CATEGORY");
                    String shareSlug = resultSet.getString("SHARE_SLUG");
                    String userName = resultSet.getString("NAME");
                    String faculty = resultSet.getString("FACULTY");
                    String filePath = resultSet.getString("FILE_PATH");
                    String fileName = resultSet.getString("FILE_NAME");

                    // alter file path to match local directory structure if needed
                    // get current working directory
                    String currentDir = System.getProperty("user.dir");
                    if (filePath != null && !filePath.isEmpty()) {
                        filePath = currentDir + "/HelloFX/src/Project_AnnouncementBoard/" + filePath;
                    }

                    // System.out.println("Loading image from: " + filePath);

                    // Create post card
                    VBox postCard = createPostCard(postId, caption, uploadTime, category,
                            shareSlug, userName, faculty, filePath, fileName);
                    postArea.getChildren().add(postCard);
                }

                if (!hasResults) {
                    showInfoMessage("No announcements available at the moment. Check back later!");
                }
            } // ResultSet auto-closed here
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Error loading posts: " + e.getMessage());
        }
    }

    private void cleanup() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("HomePage: Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    private VBox createPostCard(int postId, String caption, String uploadTime,
            String category, String shareSlug, String userName,
            String faculty, String filePath, String fileName) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setMaxWidth(600);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Post header with user info
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox userInfo = new VBox(2);
        Label userNameLabel = new Label(userName != null ? userName : "Anonymous");
        userNameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        userNameLabel.setStyle("-fx-font-smoothing-type: lcd;");

        Label facultyLabel = new Label(faculty != null ? faculty : "");
        facultyLabel.setFont(Font.font("Segoe UI", 12));
        facultyLabel.setStyle("-fx-text-fill: #65676b; -fx-font-smoothing-type: lcd;");

        Label timeLabel = new Label(uploadTime != null ? uploadTime.substring(0, 16) : "");
        timeLabel.setFont(Font.font("Segoe UI", 11));
        timeLabel.setStyle("-fx-text-fill: #65676b; -fx-font-smoothing-type: lcd;");

        userInfo.getChildren().addAll(userNameLabel, facultyLabel, timeLabel);

        // Category badge
        if (category != null && !category.isEmpty()) {
            Label categoryBadge = new Label(category);
            categoryBadge.setStyle(
                    "-fx-background-color: #fce4ec; " +
                            "-fx-text-fill: #e91e63; " +
                            "-fx-padding: 3 8 3 8; " +
                            "-fx-background-radius: 12; " +
                            "-fx-font-size: 11px; " +
                            "-fx-font-weight: bold;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            header.getChildren().addAll(userInfo, spacer, categoryBadge);
        } else {
            header.getChildren().add(userInfo);
        }

        card.getChildren().add(header);

        // Caption with "see more" feature
        if (caption != null && !caption.isEmpty()) {
            VBox captionBox = new VBox(5);

            Label captionLabel = new Label(caption);
            captionLabel.setWrapText(true);
            captionLabel.setFont(Font.font("Segoe UI", 15));
            captionLabel.setStyle("-fx-text-fill: #050505; -fx-font-smoothing-type: lcd; -fx-line-spacing: 2;");
            captionLabel.setMaxHeight(68); // Approximately 3 lines (15px font * 1.5 line height * 3)

            Label seeMoreLabel = new Label("See more");
            seeMoreLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            seeMoreLabel.setStyle("-fx-text-fill: #65676b; -fx-cursor: hand; -fx-font-smoothing-type: lcd;");

            // Check if caption needs "see more" by measuring text height
            javafx.scene.text.Text tempText = new javafx.scene.text.Text(caption);
            tempText.setFont(Font.font("Segoe UI", 15));
            tempText.setWrappingWidth(550); // Approximate width of caption area

            boolean needsSeeMore = tempText.getLayoutBounds().getHeight() > 68;

            if (needsSeeMore) {
                // Initially show collapsed version
                captionBox.getChildren().addAll(captionLabel, seeMoreLabel);

                // Toggle expand/collapse on click
                seeMoreLabel.setOnMouseClicked(e -> {
                    if (captionLabel.getMaxHeight() == 68) {
                        // Expand
                        captionLabel.setMaxHeight(Double.MAX_VALUE);
                        seeMoreLabel.setText("See less");
                    } else {
                        // Collapse
                        captionLabel.setMaxHeight(68);
                        seeMoreLabel.setText("See more");
                    }
                });

                // Hover effect
                seeMoreLabel.setOnMouseEntered(e -> seeMoreLabel.setStyle(
                        "-fx-text-fill: #e91e63; -fx-cursor: hand; -fx-font-smoothing-type: lcd; -fx-underline: true;"));

                seeMoreLabel.setOnMouseExited(
                        e -> seeMoreLabel
                                .setStyle("-fx-text-fill: #65676b; -fx-cursor: hand; -fx-font-smoothing-type: lcd;"));
            } else {
                // No need for "see more", show full caption
                captionLabel.setMaxHeight(Double.MAX_VALUE);
                captionBox.getChildren().add(captionLabel);
            }

            card.getChildren().add(captionBox);
        }

        // Image
        if (filePath != null && !filePath.isEmpty()) {
            try {
                ImageView imageView = new ImageView(new Image(new FileInputStream(filePath)));
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(570);
                imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 3, 0, 0, 1);");
                card.getChildren().add(imageView);
            } catch (FileNotFoundException e) {
                Label errorLabel = new Label("⚠️ Image not found: " + fileName);
                errorLabel.setStyle("-fx-text-fill: #e4002b; -fx-font-style: italic;");
                card.getChildren().add(errorLabel);
            }
        }

        // Share button
        Button shareButton = new Button("📤 Share");
        shareButton.setStyle(
                "-fx-background-color: #fce4ec; " +
                        "-fx-text-fill: #050505; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 16 8 16; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;");

        shareButton.setOnMouseEntered(e -> shareButton.setStyle(
                "-fx-background-color: #f8bbd0; " +
                        "-fx-text-fill: #050505; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 16 8 16; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"));

        shareButton.setOnMouseExited(e -> shareButton.setStyle(
                "-fx-background-color: #fce4ec; " +
                        "-fx-text-fill: #050505; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 16 8 16; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"));

        shareButton.setOnAction(e -> {
            String shareLink = shareSlug != null ? "https://upm-announcements.edu.my/post/" + shareSlug
                    : "https://upm-announcements.edu.my/post/" + postId;

            // Copy to clipboard functionality would go here
            System.out.println("Share link: " + shareLink);
            shareButton.setText("✓ Link Copied!");

            // Reset button text after 2 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> shareButton.setText("📤 Share"));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        HBox buttonBox = new HBox(shareButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        card.getChildren().add(buttonBox);

        return card;
    }

    private void showErrorMessage(String message) {
        Label errorLabel = new Label("⚠️ " + message);
        errorLabel.setFont(Font.font("Segoe UI", 15));
        errorLabel.setStyle("-fx-text-fill: #e4002b; -fx-padding: 20; -fx-font-smoothing-type: lcd;");
        errorLabel.setWrapText(true);
        postArea.getChildren().add(errorLabel);
    }

    private void showInfoMessage(String message) {
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(40));

        Label infoLabel = new Label("📢 " + message);
        infoLabel.setFont(Font.font("Segoe UI", 16));
        infoLabel.setStyle("-fx-text-fill: #65676b; -fx-font-smoothing-type: lcd;");
        infoLabel.setWrapText(true);

        infoBox.getChildren().add(infoLabel);
        postArea.getChildren().add(infoBox);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
