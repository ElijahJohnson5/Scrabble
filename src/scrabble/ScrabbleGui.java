package scrabble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scrabble.player.CPUPlayer;

import java.io.File;
import java.io.IOException;

public class ScrabbleGui extends Application {
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

        primaryStage.show();
        //Play three moves used for testing
        long start = System.nanoTime();
        cpuPlayer.takeTurn(board, dict);
        cpuPlayer.takeTurn(board, dict);
        cpuPlayer.takeTurn(board, dict);
        cpuPlayer.takeTurn(board, dict);
        cpuPlayer.takeTurn(board, dict);
        cpuPlayer.takeTurn(board, dict);
        cpuPlayer.takeTurn(board, dict);
        long end = System.nanoTime();
        System.out.println("Time to take seven moves " + (end - start) / 1000000);
    }
}
