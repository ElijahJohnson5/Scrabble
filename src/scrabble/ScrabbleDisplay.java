/**
 * @author Elijah Johnson
 * @description handles things to do with the display
 */
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

    /**
     * Gets the hbox of the computer hand
     * @return the computer hand hbox
     */
    public HBox getComputerHand() {
        return computerHand;
    }

    /**
     * Gets the GridPane representing the board
     * @return the GridPane that is the board
     */
    public GridPane getBoard() {
        return board;
    }

    /**
     * Gets the hbox of the player hand
     * @return return the player hand hbox
     */
    public HBox getPlayerHand() {
        return playerHand;
    }

    /**
     * Initializes the display, gets called automatically by fxml loa\der
     */
    public void initialize() {
        //Set everything to default values
        board.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.WHITE,
                                CornerRadii.EMPTY,
                                Insets.EMPTY)));
        computerHand.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.LIGHTSLATEGRAY,
                                CornerRadii.EMPTY,
                                Insets.EMPTY)));
        playerHand.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.LIGHTSLATEGRAY,
                                CornerRadii.EMPTY,
                                Insets.EMPTY)));
        computerScore.setText("0");
        playerScore.setText("0");
        computerScore.setFont(new Font(40));
        playerScore.setFont(new Font(40));
    }

    /**
     * Sets up the drag and drop for the user
     * @param user the User player that is the user
     * @param dict the dictionary, used in an event handler
     */
    public void setupForUserPlayer(UserPlayer user, Dictionary dict) {
        playMove.setOnMouseClicked(
                mouseEvent -> user.attemptPlayMove(mouseEvent, dict));
        rightSide.setOnMouseDragReleased(user::exchangeTile);
        playerHand.setOnMouseDragReleased(user::resetTile);
    }

    /**
     * Updates the score display
     * @param score the score to add
     * @param player the player to add the score to
     */
    public void updateScore(int score, Player player) {
        if (player instanceof CPUPlayer) {
            computerScore.setText(Integer.toString(
                    Integer.parseInt(computerScore.getText()) + score));
        } else if (player instanceof  UserPlayer) {
            playerScore.setText(Integer.toString(
                    Integer.parseInt(playerScore.getText()) + score));
        }
    }

    /**
     * Gets the computer score
     * @return the value in the computer score
     */
    public int getTopScore() {
        return Integer.parseInt(computerScore.getText());
    }

    /**
     * Gets the player score
     * @return the value in the player score
     */
    public int getBottomScore() {
        return Integer.parseInt(playerScore.getText());
    }

}




