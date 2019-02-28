package scrabble;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scrabble.player.CPUPlayer;
import scrabble.player.Player;
import scrabble.player.UserPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScrabbleGui extends Application {
    private Player currentPlayer;
    public final static boolean DEBUG_PRINT = true;
    private final static String LETTER_DIS = "/default_letter_distributions.txt";
    private final static String DEFAULT_BOARD = "/default_board.txt";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scrabble_display.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not read fxml file");
            return;
        }

        ScrabbleDisplay controller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Scrabble");
        primaryStage.setScene(scene);

        Dictionary dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.DAWG);
        dict.insert(new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/sowpods.txt"))));
        TileManager tileManager = new TileManager();
        Board board = new Board(controller.getBoard());

        tileManager.initialize(new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream(LETTER_DIS))));
        board.initialize(new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream(DEFAULT_BOARD))),
                tileManager);
        UserPlayer userPlayer = new UserPlayer(tileManager, board, controller.getPlayerHand());
        controller.setupForUserPlayer(userPlayer, dict);
        currentPlayer = userPlayer;
        CPUPlayer cpuPlayer2 = new CPUPlayer(tileManager, board, controller.getComputerHand());
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            private Long start = null;

            @Override
            public void handle(long now) {
                if (start == null) {
                    start = now;
                }
                if (currentPlayer.takeTurn(dict) == 0) {
                    if (currentPlayer.isHandEmpty()) {
                        //end game
                        this.stop();
                        long end = System.nanoTime();
                        System.out.println("Game took " + (end - start) / 1000000 + "ms");
                    } else {
                        currentPlayer = (currentPlayer == userPlayer) ? cpuPlayer2 : userPlayer;
                    }
                }
            }
        };
        //Play three moves used for testing
        timer.start();
    }
}
