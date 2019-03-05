//TODO Comment
package scrabble;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import scrabble.player.CPUPlayer;
import scrabble.player.Player;
import scrabble.player.UserPlayer;

public class ScrabbleDisplay {

    private @FXML HBox computerHand;
    private @FXML GridPane board;
    private @FXML HBox playerHand;
    private @FXML VBox rightSide;
    private @FXML Button playMove;
    private @FXML Label computerScore;
    private @FXML Label playerScore;

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
        computerScore.setText("0");
        playerScore.setText("0");
        computerScore.setFont(new Font(40));
        playerScore.setFont(new Font(40));
    }

    public void setupForUserPlayer(UserPlayer user, Dictionary dict) {
        playMove.setOnMouseClicked(mouseEvent -> user.attemptPlayMove(mouseEvent, dict));
        rightSide.setOnMouseDragReleased(user::exchangeTile);
        playerHand.setOnMouseDragReleased(user::resetTile);
    }

    public void updateScore(int score, Player player) {
        if (player instanceof CPUPlayer) {
            computerScore.setText(Integer.toString(Integer.parseInt(computerScore.getText()) + score));
        } else if (player instanceof  UserPlayer) {
            playerScore.setText(Integer.toString(Integer.parseInt(playerScore.getText()) + score));
        }
    }

    public int getTopScore() {
        return Integer.parseInt(computerScore.getText());
    }

    public int getBottomScore() {
        return Integer.parseInt(playerScore.getText());
    }

}




