package Chap2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class chap2_1 extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button okButton = new Button("OK");
        Button cancel = new Button("Cancel");
        HBox pane = new HBox(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.getChildren().addAll(okButton, cancel);

        Scene myScene = new Scene(pane, 200, 100);
        primaryStage.setTitle("Handle Event");
        primaryStage.setScene(myScene);
        primaryStage.show();

        OKEventHandler handler1 = new OKEventHandler();
        okButton.setOnAction(handler1);
        cancel.setOnAction(e -> System.out.println("Cancel Button Clicked!!"));

    }

    class OKEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            System.out.println("OK Button Clicked!!");
        }
    }

    class CancelHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            System.out.println("Cancel Btn pressed!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
