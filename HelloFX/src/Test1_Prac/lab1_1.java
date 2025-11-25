package Test1_Prac;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class lab1_1 extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button okButton = new Button("OK");
        Pane newPane = new Pane();
        newPane.getChildren().add(okButton);
        Scene firstScene = new Scene(newPane, 200, 200);

        primaryStage.setTitle("MyJavaFX");
        primaryStage.setScene(firstScene);
        primaryStage.show();

        Stage newStage = new Stage();
        newStage.setTitle("Second Stage");
        newStage.setScene(new Scene(new Button("New Stage"), 200, 250));
        newStage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}
