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

        Label vehicleTypeLabel = new Label("Vehicle Type:");
        RadioButton carButton = new RadioButton("Car");
        RadioButton bikeButton = new RadioButton("Bike");
        ToggleGroup vehicleTypeGroup = new ToggleGroup();
        carButton.setToggleGroup(vehicleTypeGroup);
        bikeButton.setToggleGroup(vehicleTypeGroup);

        Label speedLabel = new Label("Speed (km/h):");
        TextField speedTextField = new TextField();

        Button calc = new Button("Calculate Fine");

        calc.setOnAction(e -> {
            System.out.println("Calculate Fine button clicked");
            String vehicleType = "";
            if (carButton.isSelected()) {
                System.out.println("Car selected");
                vehicleType = "Car";
            } else if (bikeButton.isSelected()) {
                System.out.println("Bike selected");
                vehicleType = "Bike";
            } else {
                System.out.println("Please select a vehicle type.");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("ERROR!!");
                alert.setContentText("Please select a vehicle type.");
                alert.showAndWait();
                return;
            }
            int speed = 0;
            try {
                speed = Integer.parseInt(speedTextField.getText());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid speed.");
                return;
            }
            double fine = 0;
            if (vehicleType.equals("Car")) {
                fine = Fine.CarFine(speed, 110);
            } else if (vehicleType.equals("Bike")) {
                fine = Fine.BikeFine(speed, 70);
            }
            String msg = "Fine for " + vehicleType + " going at " + speed + " km/h is: RM " + fine;
            System.out.println(msg);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Fine Amount");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });

        // add controls to the grid
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

    public static void main(String[] args) {
        launch(args);
    }
}