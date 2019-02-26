/**
 * @author Elijah Johnson
 * @description The tile class, handles displaying itself
 * and getting score if the tile is played
 */

package scrabble;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Tile {
    private char character;
    private int score;

    //GUI
    private Pane tile;

    /**
     * Create a tile with the given character and score
     * @param character the character for this tile to have * for blank
     * @param score the score this tile gives when played
     */
    public Tile(char character, int score) {
        this.character = character;
        this.score = score;
        tile = null;
    }

    /**
     * Create a new display element if it is null
     */
    private void createDisplay() {
        tile = new Pane();
        //Tile size is 50x50
        Rectangle rect = new Rectangle(50, 50);
        rect.setStroke(Color.BLACK);
        rect.setStrokeType(StrokeType.INSIDE);
        //Good looking tile color
        rect.setFill(Color.BURLYWOOD);
        tile.getChildren().add(rect);
        //Arbitrary sized text that looks good
        Text text = new Text(15, 32, character + "");
        text.setFont(new Font(25));
        text.setTextAlignment(TextAlignment.CENTER);
        tile.getChildren().add(text);
        //Display the score to the user
        text = new Text(35, 40, Integer.toString(score));
        text.setFont(new Font(10));
        text.setTextAlignment(TextAlignment.CENTER);
        tile.getChildren().add(text);
    }

    /**
     * Update the character of this tile, only
     * used when blanks are played
     * @param c the new character of the tile
     */
    public void setCharacter(char c) {
        this.character = c;
    }

    /**
     * Make a blank tile display the correct character
     * used on blank tiles score stays the same
     * @param c the character to display
     */
    public void changeBlankText(char c) {
        //Make a blank tile display the correct character
        Text text = new Text(15, 32, c + "");
        text.setFont(new Font(25));
        text.setTextAlignment(TextAlignment.CENTER);
        tile.getChildren().set(1, text);
    }

    /**
     * Get the pane representing the display of the tile
     * or create one if needed
     * @return the tile member variable
     */
    public Pane getDisplay() {
        if (tile == null) {
            createDisplay();
        }
        return tile;
    }

    /**
     * Get the character represented by this tile
     * @return the character on this tile
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Get the score of this tile
     * @return the score of this tile
     */
    public int getScore() {
        return score;
    }

    /**
     * Checks if a tile is a blank tile
     * @return true if the character is '*' otherwise false
     */
    public boolean isBlank() {
        return (character == '*' || score == 0);
    }

    /**
     * Display the tile as a string
     * @return the string representing the tile
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        if (score == 0) {
            sb.append(character);
        } else {
            sb.append(Character.toLowerCase(character));
        }
        sb.append(" ");
        return sb.toString();
    }
}
