//TODO Comment
package scrabble;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import scrabble.player.UserPlayer;

public class ScrabbleDisplay {

    private @FXML HBox computerHand;
    private @FXML GridPane board;
    private @FXML HBox playerHand;
    private @FXML Button playMove;

    public HBox getComputerHand() {
        return computerHand;
    }

    public GridPane getBoard() {
        return board;
    }

    public HBox getPlayerHand() {
        return playerHand;
    }

    public void initialize() {
        board.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        computerHand.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        playerHand.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setupForUserPlayer(UserPlayer user, Dictionary dict) {
        playMove.setOnMouseClicked(mouseEvent -> user.attemptPlayMove(dict));
    }
}




