package scrabble;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private final static String LETTER_DIS = "/default_letter_distributions.txt";
    private final static String DEFAULT_BOARD = "/default_board.txt";

    public ScrabbleGui() {
        currentPlayer = null;
        tileManager = null;
        board = null;
        controller = null;
        dict = null;
    }

    private void playGame() {
        tileManager.initialize(new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream(LETTER_DIS))));
        board.initialize(new BufferedReader(
                        new InputStreamReader(
                                getClass().getResourceAsStream(DEFAULT_BOARD))),
                tileManager);
        UserPlayer userPlayer = new UserPlayer(tileManager, board, controller.getPlayerHand());
        controller.setupForUserPlayer(userPlayer, dict);
        CPUPlayer cpuPlayer1 = new CPUPlayer(tileManager, board, controller.getComputerHand());

        while (currentPlayer == null) {
            Tile playerTile = tileManager.drawOne();
            Tile computerTile = tileManager.drawOne();

            if (playerTile.getCharacter() < computerTile.getCharacter() || (playerTile.isBlank() && !computerTile.isBlank())) {
                currentPlayer = userPlayer;
            } else if (computerTile.getCharacter() < playerTile.getCharacter() || (computerTile.isBlank() && !playerTile.isBlank())) {
                currentPlayer = cpuPlayer1;
            }

            tileManager.putBack(playerTile);
            tileManager.putBack(computerTile);
        }

        AnimationTimer timer = new AnimationTimer() {
            private Long start = null;

            @Override
            public void handle(long now) {
                if (start == null) {
                    start = now;
                }
                if (currentPlayer.takeTurn(dict) == 0) {
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
                        alert.setContentText("Would you like to play again?");

                        ButtonType yes = new ButtonType("Awesome");
                        alert.getButtonTypes().setAll(yes);
                        Platform.runLater(alert::showAndWait);

                        if (DEBUG_PRINT) {
                            System.out.println("Game took " + (System.nanoTime() - start) / 1000000 + "ms");
                        }
                    } else {
                        controller.updateScore(currentPlayer.getLastWordPlayedScore(), currentPlayer);
                        currentPlayer = (currentPlayer == userPlayer) ? cpuPlayer1 : userPlayer;
                    }
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
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

        controller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Scrabble");
        primaryStage.setScene(scene);

        dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.DAWG);
        dict.insert(new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/sowpods.txt"))));
        tileManager = new TileManager();
        board = new Board(controller.getBoard());

        playGame();
        primaryStage.show();
    }
}
