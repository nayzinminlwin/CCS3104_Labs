package Chap6;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class FindGrade extends Application {
    // Statement for executing queries
    private Statement stmt;
    private TextField tfModelID = new TextField();
    private Label lblStatus = new Label();

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Initialize database connection and create a Statement object
        stmt = FirstDB.InitializeDB();

        Button btShowGrade = new Button("Find Car");
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(new Label("ModelID"), tfModelID,
                (btShowGrade));
        // set centered alignment
        hBox.setStyle("-fx-alignment: center");

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(hBox, lblStatus);
        vBox.setStyle("-fx-alignment: center");

        tfModelID.setPrefColumnCount(5);
        btShowGrade.setOnAction(e -> showGrade());

        // Create a scene and place it in the stage
        Scene scene = new Scene(vBox, 280, 80);
        primaryStage.setTitle("FindCar"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    private void showGrade() {
        String c_modelID = tfModelID.getText();
        try {
            String queryString = "Select ModelID, Model, Manufacturer, Year FROM CarModel "
                    + "WHERE ModelID = '" + c_modelID + "'";

            ResultSet rset = stmt.executeQuery(queryString);

            if (rset.next()) {
                String modelID = rset.getString(1);
                String model = rset.getString(2);
                String manufacturer = rset.getString(3);
                String year = rset.getString(4);

                // Display result in a label
                lblStatus.setText(modelID + " " + model +
                        " is manufactured by " + manufacturer + " in Year " + year);
            } else {
                lblStatus.setText("Not found");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}