
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalcClient extends Application {

    private CalcInterface calcInt;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a radius: "));

        TextField tfRadius = new TextField();
        tfRadius.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tfRadius);

        BorderPane mainPane = new BorderPane();
        // Text area to display contents
        TextArea taArea = new TextArea();
        mainPane.setCenter(new ScrollPane(taArea));
        mainPane.setTop(paneForTextField);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        initializeRMI();
        tfRadius.setOnAction(e -> {
            try {
                double radius = Double.parseDouble(tfRadius.getText().trim());
                double area = calcInt.findArea(radius);
                taArea.appendText("Radius: " + radius + "\n");
                taArea.appendText("Area: " + area + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /** Initialize RMI */
    protected void initializeRMI() {
        String host = "";

        try {
            // Get the registry
            Registry registry = LocateRegistry.getRegistry(host);

            // Get the calc interface from the registry
            calcInt = (CalcInterface) (registry.lookup("CalcServer"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
