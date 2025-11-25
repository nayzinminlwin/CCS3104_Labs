package Test1_Prac;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ButtonDemo_Prac extends Application {
    protected Text myText = new Text(50, 50, "Java FX Programming!!");

    protected BorderPane getPane() {

        BorderPane myBorderPane = new BorderPane();

        HBox hb = new HBox(20);
        Button leftBtn = new Button("<< Left");
        Button rightBtn = new Button("Right >>");
        hb.getChildren().addAll(leftBtn, rightBtn);
        hb.setAlignment(Pos.CENTER);
        hb.setStyle("-fx-border-color: green");

        myBorderPane.setBottom(hb);

        Pane paneForText = new Pane();
        paneForText.getChildren().add(myText);
        myBorderPane.setCenter(paneForText);

        leftBtn.setOnAction(e -> myText.setX(myText.getX() - 10));
        rightBtn.setOnAction(e -> myText.setX(myText.getX() + 10));

        return myBorderPane;

    }

    @Override
    public void start(Stage primaryStage) {
        Pane finalPane = getPane();
        Scene myScene = new Scene(finalPane, 450, 200);

        primaryStage.setTitle("Button Demo Prac");
        primaryStage.setScene(myScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
