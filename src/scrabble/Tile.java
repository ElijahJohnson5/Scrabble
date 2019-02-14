package scrabble;

public class Tile {
    private char character;
    private int score;
    private int multiplier;
    private boolean word;

    public Tile() {
        multiplier = 1;
        word = false;
        character = ' ';
        score = 0;
    }

    public Tile(int multiplier, boolean word) {
        this.word = word;
        this.multiplier = multiplier;
    }

    public Tile(char character, int score) {
        this.character = character;
        this.score = score;
        multiplier = 1;
    }

    public int getScore() {
        return score;
    }

    public boolean isBlank() {
        return (character == '*');
    }

    public boolean isEmpty() {
        return (character == ' ');
    }

    @Override
    public String toString() {
        return String.valueOf(character);
    }
}
