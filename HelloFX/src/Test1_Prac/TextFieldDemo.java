package Test1_Prac;

import hellofx.ButtonDemo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TextFieldDemo extends ButtonDemo {
    @Override // Override the getPane() method in the super class
    protected BorderPane getPane() {
        BorderPane pane = super.getPane();

        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a new message: "));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);
        pane.setTop(paneForTextField);

        tf.setOnAction(e -> text.setText(tf.getText()));
        super.text.setStyle(
                "-fx-fill: blue; -fx-font-size: 20px; -fx-font-weight: bold; -fx-font-style: italic; -fx-font-family: 'Times New Roman'");

        VBox leftVBox = new VBox(5);

        leftVBox.setPadding(new Insets(5, 5, 5, 5));
        leftVBox.setStyle("-fx-border-color: green");

        // radio buttons for selecting text color
        // grouped together using a ToggleGroup
        ToggleGroup group = new ToggleGroup();
        RadioButton rbRed = new RadioButton("Red");
        RadioButton rbGreen = new RadioButton("Green");
        RadioButton rbBlue = new RadioButton("Blue");
        leftVBox.getChildren().addAll(rbRed, rbGreen, rbBlue);
        pane.setLeft(leftVBox);
        rbRed.setToggleGroup(group);
        rbGreen.setToggleGroup(group);
        rbBlue.setToggleGroup(group);

        VBox rightVB = new VBox(5);
        rightVB.setPadding(new Insets(5, 5, 5, 5));
        rightVB.setStyle("-fx-border-color:green;");

        // create check boxes for selecting text styles
        CheckBox cbBold = new CheckBox("Bold");
        CheckBox cbItalic = new CheckBox("Italic");
        CheckBox cbUnderline = new CheckBox("Underline");
        rightVB.getChildren().addAll(cbBold, cbItalic, cbUnderline);

        pane.setRight(rightVB);

        // Event handling for radio buttons
        rbRed.setOnAction(e -> text.setStyle(text.getStyle().replaceAll("-fx-fill: [a-zA-Z]+;", "-fx-fill: red;")));
        rbGreen.setOnAction(
                e -> text.setStyle(text.getStyle().replaceAll("-fx-fill: [a-zA-Z]+;", "-fx-fill: green;")));
        rbBlue.setOnAction(
                e -> text.setStyle(text.getStyle().replaceAll("-fx-fill: [a-zA-Z]+;", "-fx-fill: blue;")));

        // Event handling for check boxes
        cbBold.setOnAction(e -> {
            if (cbBold.isSelected()) {
                text.setStyle(text.getStyle().replaceAll("-fx-font-weight: normal;", "-fx-font-weight: bold;"));
            } else {
                text.setStyle(text.getStyle().replaceAll("-fx-font-weight: bold;", "-fx-font-weight: normal;"));
            }
        });

        return pane;
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
