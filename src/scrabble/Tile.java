package scrabble;

public class Tile {
    private char character;
    private int score;

    public Tile(char character, int score) {
        this.character = character;
        this.score = score;
    }

    public char getCharacter() {
        return character;
    }

    public int getScore() {
        return score;
    }

    public boolean isBlank() {
        return (character == '*');
    }

    @Override
    public String toString() {
        return "{" + character + ", " + score + "}";
    }
}
