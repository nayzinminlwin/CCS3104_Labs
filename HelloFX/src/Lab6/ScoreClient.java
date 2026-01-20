package Lab6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ScoreClient extends Application {

    ScoreService_Interface scoreSvc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // connect to RMI server
        initializeRMI();

        HBox NameBox = new HBox(10);
        NameBox.setPadding(new Insets(10, 10, 10, 10));
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Name");
        NameBox.getChildren().addAll(nameLabel, nameField);

        HBox ScoreBox = new HBox(10);
        ScoreBox.setPadding(new Insets(10, 10, 10, 10));
        Label scoreLabel = new Label("Score:");
        Label scoreField = new Label("");
        ScoreBox.getChildren().addAll(scoreLabel, scoreField);

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        Button submitBtn = new Button("Search");
        buttonBox.getChildren().add(submitBtn);

        submitBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();

                // Invoke searchQuery function
                String scores = scoreSvc.getScores(name);
                System.out.println("Scores for " + name + ": " + scores);

                // set scoreField to scores
                scoreField.setText(scores);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(NameBox);
        mainPane.setCenter(ScoreBox);
        mainPane.setBottom(buttonBox);
        Scene scene = new Scene(mainPane, 250, 150);
        primaryStage.setTitle("Score Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initializeRMI() {
        String host = "";

        try {
            // Get the registry
            Registry reg = LocateRegistry.getRegistry(host);

            // Get the score service interface from the registry
            scoreSvc = (ScoreService_Interface) (reg.lookup("ScoreService"));

        } catch (Exception ex) {
            System.out.println("Client exception: " + ex.getMessage());
            // ex.printStackTrace();
        }
    }
}
