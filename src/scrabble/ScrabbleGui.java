package scrabble;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scrabble.player.CPUPlayer;
import scrabble.player.Player;

import java.io.File;
import java.io.IOException;

public class ScrabbleGui extends Application {
    private Player currentPlayer;
    public final static boolean DEBUG_PRINT = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scrabble_display.fxml"));
        Parent root = null;
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
        dict.insert(new File("./resources/dict.txt"));
        TileManager tileManager = new TileManager();
        Board board = new Board(controller.getBoard());

        tileManager.initialize(new File("./resources/default_letter_distributions.txt"));
        board.initialize(new File("./resources/default_board.txt"), tileManager);
        CPUPlayer cpuPlayer = new CPUPlayer(tileManager, controller.getComputerHand());
        currentPlayer = cpuPlayer;
        CPUPlayer cpuPlayer2 = new CPUPlayer(tileManager, controller.getPlayerHand());
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            private Long start = null;

            @Override
            public void handle(long now) {
                if (start == null) {
                    start = now;
                }

                if (currentPlayer.takeTurn(board, dict) == 0) {
                    if (currentPlayer.isHandEmpty()) {
                        //end game
                        this.stop();
                        long end = System.nanoTime();
                        System.out.println("Game took " + (end - start) / 1000000 + "ms");
                    } else {
                        currentPlayer = (currentPlayer == cpuPlayer) ? cpuPlayer2 : cpuPlayer;
                    }
                }
            }
        };
        //Play three moves used for testing
        timer.start();
    }
}
