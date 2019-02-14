package scrabble;

public class BoardSquare {
    private int wordMultiplier;
    private int letterMultiplier;
    private Tile tile;

    public BoardSquare() {
        wordMultiplier = 1;
        letterMultiplier = 1;
        tile = null;
    }

    public BoardSquare(Tile tile) {
        wordMultiplier = 1;
        letterMultiplier = 1;
        this.tile = tile;
    }

    public BoardSquare(int multiplier, boolean word) {
        if (word) {
            wordMultiplier = multiplier;
            letterMultiplier = 1;
        } else {
            letterMultiplier = multiplier;
            wordMultiplier = 1;
        }
    }

    @Override
    public String toString() {
        if (tile != null) {
            return tile.toString();
        } else {
            return "{" + letterMultiplier + ", " + wordMultiplier + "}";
        }
    }
}