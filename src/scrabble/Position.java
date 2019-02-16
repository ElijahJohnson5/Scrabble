package scrabble;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(Position other) {
        this.row = other.row;
        this.col = other.col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Position transpose() {
        int temp = row;
        this.row = col;
        this.col = temp;
        return this;
    }

    @Override
    public String toString() {
        return "{" + row + ", " + col + "}";
    }

    @Override
    public int hashCode() {
        return (row * 15 + col) * 97;
    }
}
