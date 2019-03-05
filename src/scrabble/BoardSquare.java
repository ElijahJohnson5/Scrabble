/**
 * @author Elijah Johnson
 * @description Square that is on the board, can be empty
 * with multipliers or filled with a tile
 */

package scrabble;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class BoardSquare {
    private int wordMultiplier;
    private int letterMultiplier;
    private Tile tile;
    private boolean placed;

    //GUI
    private Pane pane;
    private Pane oldPane;
    /**
     * Default square not tile
     * and multipliers set to one
     */
    public BoardSquare() {
        wordMultiplier = 1;
        letterMultiplier = 1;
        tile = null;
        pane = null;
        placed = false;
    }

    /**
     * Creates a new display for a board square if it doesnt
     * exist
     */
    private void createDisplay() {
        if (tile == null) {
            pane = new Pane();
            //Size of square
            Rectangle rect = new Rectangle(50, 50);
            rect.setStrokeType(StrokeType.INSIDE);
            rect.setStroke(Color.BLACK);
            //Color based on multiplier
            if (wordMultiplier == 1 && letterMultiplier == 1) {
                rect.setFill(Color.WHITE);
            } else if (wordMultiplier > 1) {
                rect.setFill((wordMultiplier == 2) ? Color.PINK : Color.RED);
            } else if (letterMultiplier > 1) {
                rect.setFill((letterMultiplier == 2) ? Color.LIGHTBLUE : Color.DARKBLUE);
            }
            //Add to pane
            pane.getChildren().add(rect);
        }
        else {
            pane = tile.getDisplay();
        }
    }

    /**
     * Gets a display if it exists
     * otherwise creates a new display
     * @return the pane representing the display
     */
    public Pane getDisplay() {
        if (pane == null) {
            createDisplay();
        }
        return pane;
    }

    /**
     * Filled square with a tile
     * @param tile the tile that is occupying
     *             the square
     */
    public BoardSquare(Tile tile) {
        wordMultiplier = 1;
        letterMultiplier = 1;
        this.tile = tile;
        placed = true;
    }

    /**
     * Board square with a multiplier
     * either word or letter
     * @param multiplier the multiplier to set
     * @param word whether is is a word multiplier or a letter
     */
    public BoardSquare(int multiplier, boolean word) {
        if (word) {
            wordMultiplier = multiplier;
            letterMultiplier = 1;
        } else {
            letterMultiplier = multiplier;
            wordMultiplier = 1;
        }
        tile = null;
    }

    /**
     * If the board square is filled return the character
     * that is on the tile
     * @return the character that is on the tile, or
     * '.' if the square isnt filled
     */
    public char getTileCharacter() {
        if (tile != null) {
            return tile.getCharacter();
        }
        return '.';
    }

    /**
     * Plays a tile on this square
     * resets the multipliers to one
     * @param t the tile to play
     */
    public void playTile(Tile t) {
        if (tile == null) {
            this.tile = t;
        } else {
            this.tile.getDisplay(true);
        }
        this.wordMultiplier = 1;
        this.letterMultiplier = 1;
        placed = true;
        t.unHide();
        if (pane != null) {
            this.pane = t.getDisplay();
        }
    }

    /**
     * Places a tile temporarily
     * @param t the tile to place
     */
    public void placeTile(Tile t) {
        this.tile = t;
        if (pane != null) {
            this.oldPane = pane;
            this.pane = t.getDisplay();
        }
    }

    /**
     * Undo a place tile call
     */
    public void unPlaceTile() {
        this.tile = null;
        pane = oldPane;
        oldPane = null;
    }


    /**
     * Gets the word multiplier of
     * this square
     * @return the member variable
     * word multiplier
     */
    public int getWordMultiplier() {
        return wordMultiplier;
    }

    /**
     * Gets the letter multiplier of this square
     * @return the letter multiplier member variable
     */
    public int getLetterMultiplier() {
        return letterMultiplier;
    }

    /**
     * Gets the tile on this square
     * can be null
     * @return either null if it isnt
     * filled or the tile
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Check if the square is empty
     * @return true if the tile is null
     * otherwise false
     */
    public boolean isEmpty() {
        return (tile == null || !placed);
    }

    /**
     * Converts the board square to a string
     * representation
     * @return the string representing the
     * current square
     */
    @Override
    public String toString() {
        if (tile != null) {
            return tile.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            if (wordMultiplier > 1) {
                sb.append(wordMultiplier);
            } else {
                sb.append('.');
            }

            if (letterMultiplier > 1) {
                sb.append(letterMultiplier);
            } else {
                sb.append('.');
            }

            sb.append(" ");
            return sb.toString();
        }
    }
}
