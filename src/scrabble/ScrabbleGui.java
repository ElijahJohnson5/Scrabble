/**
 * @author Elijah Johnson
 * @description Runs the game with a gui, with a user against
 * a computer
 */

package scrabble;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import scrabble.player.CPUPlayer;
import scrabble.player.Player;
import scrabble.player.UserPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScrabbleGui extends Application {
    private Player currentPlayer;
    private TileManager tileManager;
    private Board board;
    private ScrabbleDisplay controller;
    private Dictionary dict;
    public final static boolean DEBUG_PRINT = false;
    private final static String LETTER_DIS
            = "/default_letter_distributions.txt";
    private final static String DEFAULT_BOARD = "/default_board.txt";

    /**
     * Basic constructor, initializes all to null
     */
    public ScrabbleGui() {
        currentPlayer = null;
        tileManager = null;
        board = null;
        controller = null;
        dict = null;
    }

    /**
     * Setups the scrabble game and starts it
     */
    private void playGame() {
        //Initialize everything
        currentPlayer = null;
        controller.initialize();
        tileManager.initialize(new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream(LETTER_DIS))));
        board.initialize(new BufferedReader(
                        new InputStreamReader(
                                getClass().getResourceAsStream(DEFAULT_BOARD))),
                tileManager);
        UserPlayer userPlayer =
                new UserPlayer(tileManager, board, controller.getPlayerHand());
        controller.setupForUserPlayer(userPlayer, dict);
        CPUPlayer cpuPlayer1 =
                new CPUPlayer(tileManager, board, controller.getComputerHand());

        //Get the first player based off of who draws the letter closes to A
        while (currentPlayer == null) {
            Tile playerTile = tileManager.drawOne();
            Tile computerTile = tileManager.drawOne();

            if (playerTile.getCharacter() < computerTile.getCharacter()
                    || (playerTile.isBlank() && !computerTile.isBlank())) {
                currentPlayer = userPlayer;
            } else if (computerTile.getCharacter() < playerTile.getCharacter()
                    || (computerTile.isBlank() && !playerTile.isBlank())) {
                currentPlayer = cpuPlayer1;
            }

            tileManager.putBack(playerTile);
            tileManager.putBack(computerTile);
        }

        //Main loop
        AnimationTimer timer = new AnimationTimer() {
            private Long start = null;

            @Override
            public void handle(long now) {
                if (start == null) {
                    start = now;
                }
                //Have the current player take their turn
                if (currentPlayer.takeTurn(dict) == 0) {
                    //The game is over
                    if (currentPlayer.isHandEmpty()) {
                        controller.updateScore(currentPlayer.getLastWordPlayedScore(), currentPlayer);
                        //end game
                        this.stop();
                        int computerScore = controller.getTopScore();
                        int playerScore = controller.getBottomScore();
                        computerScore -= cpuPlayer1.getLeftoverTileScore();
                        playerScore -= userPlayer.getLeftoverTileScore();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Game Over");
                        if (computerScore > playerScore) {
                            alert.setHeaderText("You lost to the computer");
                        } else if (playerScore > computerScore) {
                            alert.setHeaderText("You beat the computer!");
                        } else {
                            alert.setHeaderText("You tied with the computer");
                        }
                        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,
                                Region.USE_PREF_SIZE);
                        alert.setContentText("Computer Score: " + computerScore +
                                        " Player Score: " + playerScore +
                                        " Would you like to play again?");
                        ButtonType yes = new ButtonType("Yes");
                        ButtonType no = new ButtonType("No");
                        alert.getButtonTypes().setAll(yes, no);
                        //Check if we should play again
                        alert.setOnHidden(dialogEvent -> {
                            ButtonType result = ((Alert)dialogEvent.getSource())
                                    .getResult();
                            if (result == yes) {
                                playGame();
                            } else {
                                Platform.exit();
                            }
                        });
                        //Show who won
                        Platform.runLater(alert::showAndWait);

                        if (DEBUG_PRINT) {
                            System.out.println("Game took " +
                                    (System.nanoTime() - start) / 1000000 +
                                    "ms");
                        }
                    } else {
                        //Update the current display of score
                        controller.updateScore(
                                currentPlayer.getLastWordPlayedScore(),
                                currentPlayer);
                        //Update current player
                        currentPlayer = (currentPlayer == userPlayer)
                                ? cpuPlayer1
                                : userPlayer;
                    }
                }
            }
        };
        //Start the main loop
        timer.start();
    }

    /**
     * Launch the application with args
     * @param args the command line args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Override start from javafx application
     * @param primaryStage the primary stage to show
     */
    @Override
    public void start(Stage primaryStage) {
        //Load the fxml
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("scrabble_display.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not read fxml file");
            return;
        }

        //Get the controller
        controller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Scrabble");
        primaryStage.setScene(scene);
        //Create dictionary
        dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.DAWG);
        dict.insert(new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/sowpods.txt"))));
        tileManager = new TileManager();
        board = new Board(controller.getBoard());
        //play the game
        playGame();
        primaryStage.show();
    }
}
