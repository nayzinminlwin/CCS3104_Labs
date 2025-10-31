package hellofx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class TenXGrid extends Application {
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Create a pane to hold the grids
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(15, 15, 15, 15));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                TextField textField = new TextField();
                textField.setPrefColumnCount(1);
                textField.setAlignment(Pos.CENTER);
                // int k = GenerateRandNumbers(0, 1);
                int k = Math.random() < 0.5 ? 0 : 1; // Alternative way to generate 0 or 1
                textField.setText(k + "");
                pane.add(textField, j, i);
            }
        }

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Show Ten x Ten Grid"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    public int GenerateRandNumbers(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static void main(String[] args) {
        launch(args);
    }
}