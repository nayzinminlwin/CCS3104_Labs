package Lab3;

import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RandomCircle extends Application {
    private int clickCount = 0;
    private long startTime = 0;
    private long endTime = 0;

    @Override
    public void start(Stage primaryStage) {
        // Your code to create random circles goes here
        Circle myCircle = new Circle(250, 50, 10);
        myCircle.setStyle("-fx-fill: pink; -fx-stroke: grey; -fx-stroke-width: 1;");

        Pane pane = new Pane();
        pane.getChildren().add(myCircle);

        Scene scene = new Scene(pane, 400, 300);

        primaryStage.setTitle("Random Circle");
        System.out.println("Click the circle 20 times as fast as you can!");
        primaryStage.setScene(scene);
        primaryStage.show();

        myCircle.setOnMouseClicked(e -> {
            clickCount++;
            double x = Math.random() * (scene.getWidth() - myCircle.getRadius() * 2) + myCircle.getRadius();
            double y = Math.random() * (scene.getHeight() - myCircle.getRadius() * 2) + myCircle.getRadius();
            // change circle to random color
            myCircle.setStyle("-fx-fill: rgb(" + (int) (Math.random() * 256) + "," + (int) (Math.random() * 256) + ","
                    + (int) (Math.random() * 256) + "); -fx-stroke: grey; -fx-stroke-width: 1;");
            myCircle.setCenterX(x);
            myCircle.setCenterY(y);

            if (clickCount == 1) {
                // record current time
                startTime = System.currentTimeMillis();
            }

            if (clickCount >= 5) {
                // record end time
                endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;

                // deactivate circle
                myCircle.setOnMouseClicked(null);
                myCircle.setVisible(false);

                // display results
                String labelText = String.format("You clicked %d times in %.2f seconds.", clickCount,
                        totalTime / 1000.0);
                Label endLabel = new Label(labelText);

                // set centre end label
                endLabel.layoutXProperty().bind(scene.widthProperty().subtract(endLabel.widthProperty()).divide(2));
                endLabel.layoutYProperty().bind(scene.heightProperty().subtract(endLabel.heightProperty()).divide(2));
                pane.getChildren().add(endLabel);
                System.out.println(labelText);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
