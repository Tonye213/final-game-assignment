import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

/************************************************************************************************************************************************************************
 * @Description:
 * The Probability Game is an engaging JavaFX-based game where players attempt to find hidden prizes within a 3x5 grid of buttons.
 * With only 6 tries, players must click on the buttons to uncover 3 hidden prizes. The game challenges players' luck and strategy,
 * providing instant feedback on their progress. If all prizes are found within the allotted attempts, the player wins; otherwise, they lose.
 
 * @author: Tonye Jackreece
 * @date: 22/06/2024
 * @studentNumber: 3765653
 ***********************************************************************************************************************************************************************/

public class ProbabilityGame extends Application
{

    // Constants for the game configuration
    private static final int ROWS = 3;
    private static final int COLUMNS = 5;
    private static final int TOTAL_CELLS = ROWS * COLUMNS;
    private static final int PRIZES = 3;
    private static final int MAX_TRIES = 6;

    // Array to hold the buttons
    private Button[][] buttons = new Button[ROWS][COLUMNS];
    private List<Integer> prizePositions;
    private int attempts = 0;
    private int foundPrizes = 0;
    
    /********************************************************************************************************************************************************************
     * start:
     * Sets the title of the primary stage and initializes the game setup.
     *******************************************************************************************************************************************************************/

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Probability Game");
        setupGame(primaryStage);
    }

    /*******************************************************************************************************************************************************************
     * setupGame:
     * Initializes the game layout with a grid of buttons and an instructions pane. Randomly selects prize positions,
      sets up the background image for the main layout, and displays the scene.
     *******************************************************************************************************************************************************************/
    
    private void setupGame(Stage stage)
    {
        GridPane gridPane = new GridPane();

        // Set gaps between the grid cells: Horizontal gap, Vertical gap
        gridPane.setHgap(10); 
        gridPane.setVgap(10); 

        // Initialize the grid with buttons labeled "T"
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLUMNS; col++)
            {
                Button button = new Button("T");
                button.setPrefSize(60, 60);
                int index = row * COLUMNS + col;
                button.setOnAction(event -> handleButtonClick(index, button));
                buttons[row][col] = button;
                gridPane.add(button, col, row);
            }
        }

        // Randomly select prize positions
        prizePositions = new ArrayList<>();
        for (int i = 0; i < TOTAL_CELLS; i++)
        {
            prizePositions.add(i);
        }
        Collections.shuffle(prizePositions);
        prizePositions = prizePositions.subList(0, PRIZES);

        // Reset attempts and found prizes
        attempts = 0;
        foundPrizes = 0;

        // Create instructions pane
        VBox instructionsPane = new VBox();
        // Align text to top left
        instructionsPane.setAlignment(Pos.TOP_LEFT); 
        instructionsPane.setPadding(new Insets(10));
        instructionsPane.setSpacing(10);
        
        
        
        // Instructions text with larger font size
        Text instructionsText = new Text(
            "Welcome to the Probability Game!\n\n" +
            "You have 6 tries to find the 3 prizes hidden in the grid.\n" +
            "Click on the boxes to reveal what's behind them.\n" +
            "If you find all 3 prizes within 6 tries, you win!\n" +
            "Good luck!"
        );
        
        // font control
        instructionsText.setFont(new Font(12)); 
        

        instructionsPane.getChildren().addAll( instructionsText);

        // Create main layout
        HBox mainLayout = new HBox();
        mainLayout.setSpacing(20);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(instructionsPane, gridPane);

        // Set the background image for the main layout
        BackgroundImage backgroundImage = new BackgroundImage(
            new Image("file:backgroung.png"), 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.DEFAULT, 
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
        );
        mainLayout.setBackground(new Background(backgroundImage));

        // Set the scene with the main layout
        Scene scene = new Scene(mainLayout, 600, 300);
        stage.setScene(scene);
        stage.show();
    }

    /********************************************************************************************************************************************************************
     * handleButtonClick:
     * Handles the logic for when a button in the grid is clicked. 
      Increments attempts, checks if the button clicked is a prize, updates the button text accordingly, and checks for win/lose conditions.
     *******************************************************************************************************************************************************************/
     
    private void handleButtonClick(int index, Button button)
    {
        // Ignore if button already clicked
        if (button.getText().isEmpty() || button.getText().equals("🏆"))
        {
            return;
        }

        attempts++;
        
        
        // Check if the clicked button is a prize
        if (prizePositions.contains(index))
        {
            button.setText("🏆");  // Represent the prize
            foundPrizes++;
            prizePositions.remove(Integer.valueOf(index));
        } else
        {
            button.setText("");
        }

        // Check win or lose conditions
        if (foundPrizes == PRIZES)
        {
            showAlert(AlertType.INFORMATION, "You Win!", "Congratulations, you found all the prizes!");
            resetGame();
        } else if (attempts == MAX_TRIES)
        {
            showAlert(AlertType.ERROR, "Game Over", "Sorry, you didn't find all the prizes in 6 tries.");
            resetGame();
        }
    }

    /*******************************************************************************************************************************************************************
     * showAlert:
      Displays an alert with a specified type, title, and message.
    *******************************************************************************************************************************************************************/
    
    private void showAlert(AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /********************************************************************************************************************************************************************
     * resetGame:
     * Resets the game by reinitializing the game setup with the current stage.
     *******************************************************************************************************************************************************************/
     
    private void resetGame()
    {
        setupGame((Stage) buttons[0][0].getScene().getWindow());
    }
}
