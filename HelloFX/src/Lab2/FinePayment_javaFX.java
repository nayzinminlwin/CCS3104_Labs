package Lab2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FinePayment_javaFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // First create a Grid for the form
        GridPane gPane = new GridPane();
        gPane.setAlignment(Pos.CENTER);
        gPane.setPadding(new Insets(10, 10, 10, 10));
        gPane.setHgap(5.5);
        gPane.setVgap(5.5);

        // Bold the Title and Centre it
        Label titleLabel = new Label("Vehicle Fine Payment System");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 0 0 10 0;");
        gPane.add(titleLabel, 0, 0);

        // Merging the two columns for the title
        GridPane.setColumnSpan(gPane.getChildren().get(0), 2);

        // initializing the labels and input fields
        Label vehicleTypeLabel = new Label("Vehicle Type:");
        RadioButton carButton = new RadioButton("Car");
        RadioButton bikeButton = new RadioButton("Bike");
        ToggleGroup vehicleTypeGroup = new ToggleGroup();
        carButton.setToggleGroup(vehicleTypeGroup);
        bikeButton.setToggleGroup(vehicleTypeGroup);

        Label speedLabel = new Label("Speed (km/h):");
        TextField speedTextField = new TextField();

        Button calc = new Button("Calculate Fine");

        // When button Clicked,
        calc.setOnAction(e -> {
            String vehicleType = "";
            double speed = 0;

            try {
                // Radio button validation
                if (carButton.isSelected()) {
                    vehicleType = "Car";
                } else if (bikeButton.isSelected()) {
                    vehicleType = "Bike";
                } else {
                    throw new Exception("No vehicle type selected");
                }

                // speed validation
                speed = Double.parseDouble(speedTextField.getText().trim());
                if (speed < 0) {
                    throw new NumberFormatException("Speed cannot be negative!!!");
                }

            } catch (Exception ex) {
                ShowError("Input Error", "ERROR!!", ex.getMessage());
                return;
            }

            // Call the VehicleFine class from Fine.java to calculate the fine
            double fine = Fine.VehicleFine(vehicleType, speed);

            // Print message to console and Call ShowInfo function for Alert Box
            String msg = String.format("Fine for %s going at %.2f km/h is: RM %.2f", vehicleType, speed, fine);
            System.out.println(msg);
            ShowInfo("Fine Calculation", null, msg);

            speedTextField.clear();
            vehicleTypeGroup.selectToggle(null);
        });

        // add labels and inputFields to the grid
        gPane.add(vehicleTypeLabel, 0, 1);
        gPane.add(carButton, 1, 1);
        gPane.add(bikeButton, 1, 2);
        gPane.add(speedLabel, 0, 3);
        gPane.add(speedTextField, 1, 3);
        gPane.add(calc, 1, 4);

        primaryStage.setTitle("Vehicle Fine System");
        Scene myScene = new Scene(gPane, 250, 180);

        primaryStage.setScene(myScene);

        primaryStage.show();
    }

    private void ShowError(String title, String header, String ContentText) {
        Alert myAlert = new Alert(AlertType.ERROR);
        myAlert.setTitle(title);
        myAlert.setHeaderText(header);
        myAlert.setContentText(ContentText);
        myAlert.showAndWait();
    }

    private void ShowInfo(String title, String header, String message) {
        Alert myInfoAlert = new Alert(AlertType.INFORMATION);
        myInfoAlert.setTitle(title);
        myInfoAlert.setHeaderText(header);
        myInfoAlert.setContentText(message);
        myInfoAlert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}