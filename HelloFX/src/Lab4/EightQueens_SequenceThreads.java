package Lab4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class EightQueens_SequenceThreads extends Application {
    public static final int BOARD_SIZE = 8;

    // queenPositions[row] = column
    // This array stores the column index of the queen for each specific row.
    // -1 indicates that no queen is currently placed in that row.
    private int[] queenPositions = { -1, -1, -1, -1, -1, -1, -1, -1 };
    Label[][] boardSquares = new Label[BOARD_SIZE][BOARD_SIZE];
    int threadIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        nextSolution();

        // Display chess board
        GridPane chessBoard = new GridPane();
        chessBoard.setAlignment(Pos.CENTER);

        // i -> row, j -> col
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                chessBoard.add(boardSquares[row][col] = new Label(), col, row);
                boardSquares[row][col].setStyle("-fx-border-color: black");
                boardSquares[row][col].setPrefSize(55, 55);
            }
        }

        // Display queens
        Image queenImage = new Image("Lab4/queen.jpg");
        for (int row = 0; row < BOARD_SIZE; row++) {
            // Place the image at the calculated column for this row
            int column = queenPositions[row];
            if (column != -1) {
                boardSquares[row][column].setGraphic(new ImageView(queenImage));
            }
        }

        // next solution button
        Button nextSolutionButton = new Button("Next Solution");
        nextSolutionButton.setPrefWidth(55 * BOARD_SIZE);
        nextSolutionButton.setAlignment(Pos.CENTER);
        nextSolutionButton.setOnAction(e -> {
            if (threadIndex < BOARD_SIZE) {
                nextSolution();
                // updateBoardUI();
            } else {
                threadIndex = 0;
            }
        });

        // add button to the chessBoard
        chessBoard.add(nextSolutionButton, 0, BOARD_SIZE, BOARD_SIZE, 1);
        Scene scene = new Scene(chessBoard, 57 * BOARD_SIZE, 57 * BOARD_SIZE);
        primaryStage.setTitle("EightQueens");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void nextSolution() {

        final int index = threadIndex;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread started for initial column " + index);
                queenPositions[0] = index;

                for (int i = 1; i < BOARD_SIZE; i++) {
                    queenPositions[i] = -1;
                }
                // Each thread would ideally have its own queenPositions array to avoid
                // conflicts, but for simplicity, we are using the same array here.
                boolean found = searchForSolution();

                Platform.runLater(() -> {
                    updateBoardUI();
                    if (found) {
                        System.out.println("Queens : " + java.util.Arrays.toString(queenPositions));
                    } else {
                        System.out.println("No solution found for start column " + index);
                    }
                });
            }
        }).start();
        threadIndex++;

    }

    private void updateBoardUI() {
        // Access the GridPane from your main class
        // (You might need to make 'boardSquares' a class-level variable)

        Image queenImage = new Image("Lab4/queen.jpg");

        // Clear old queens first (optional, but good practice)
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                boardSquares[r][c].setGraphic(null);
            }
        }

        // Place the new queens
        // Place the new queens
        for (int row = 0; row < BOARD_SIZE; row++) {
            int column = queenPositions[row];
            if (column != -1) {
                // Create the view
                ImageView queenView = new ImageView(queenImage);

                // --- FIX START: Force the image to be smaller than the cell ---
                queenView.setFitWidth(50); // slightly smaller than the 55px cell
                queenView.setFitHeight(50);
                queenView.setPreserveRatio(true); // keeps the image looking correct
                // --- FIX END ---

                boardSquares[row][column].setGraphic(queenView);
            }
        }
    }

    /** Search for a solution using backtracking */
    private boolean searchForSolution() {
        // k -> currentRow
        // We are looking for a valid column in the 'currentRow' to place a queen
        int currentRow = 1;

        while (currentRow >= 1 && currentRow < BOARD_SIZE) {
            // Find a valid column position in the current row
            int validColumn = findNextValidColumn(currentRow);

            // here is where threading might start
            // create 8 threads

            if (validColumn < 0) {
                // No valid position found in this row, need to backtrack
                queenPositions[currentRow] = -1;
                currentRow--; // Move back to the previous row
            } else {
                // Valid position found, place the queen and move to next row
                queenPositions[currentRow] = validColumn;
                currentRow++;
            }
        }

        if (currentRow == -1)
            return false; // No solution found (backtracked past the start)
        else
            return true; // A solution is found
    }

    // k -> row
    public int findNextValidColumn(int row) {
        // We start searching from the column immediately after the current queen's
        // position
        // This prevents re-checking positions we've already tried in this recursion
        // step.
        int startColumn = queenPositions[row] + 1;

        // j -> column
        for (int column = startColumn; column < BOARD_SIZE; column++) {
            if (isSafePlacement(row, column))
                return column; // Found a safe spot
        }

        return -1; // No valid column found in this row
    }

    /** Return true if a queen can be safely placed at (targetRow, targetColumn) */
    public boolean isSafePlacement(int targetRow, int targetColumn) {
        // Check against all queens placed in previous rows
        // i -> distance (how many rows back are we checking?)
        for (int distance = 1; distance <= targetRow; distance++) {

            int previousRow = targetRow - distance;
            int previousQueenColumn = queenPositions[previousRow];

            // 1. Check same column
            if (previousQueenColumn == targetColumn
                    // 2. Check up-left diagonal
                    || previousQueenColumn == targetColumn - distance
                    // 3. Check up-right diagonal
                    || previousQueenColumn == targetColumn + distance) {
                return false; // Conflict found
            }
        }
        return true; // No conflict
    }

    public static void main(String[] args) {
        launch(args);
    }
}