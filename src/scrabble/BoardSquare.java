/**
 * @author Elijah Johnson
 * @description Square that is on the board, can be empty
 * with multipliers or filled with a tile
 */

package scrabble;

public class BoardSquare {
    private int wordMultiplier;
    private int letterMultiplier;
    private Tile tile;

    /**
     * Default square not tile
     * and multipliers set to one
     */
    public BoardSquare() {
        wordMultiplier = 1;
        letterMultiplier = 1;
        tile = null;
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
        this.tile = t;
        this.wordMultiplier = 1;
        this.letterMultiplier = 1;
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
        return (tile == null);
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
            return "{" + letterMultiplier + ", " + wordMultiplier + "}";
        }
    }
}
