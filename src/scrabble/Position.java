/**
 * @author Elijah Johnson
 * @description Basic position class, holds a row
 * and column and can be transposed
 */

package scrabble;

public class Position {
    private int row;
    private int col;

    /**
     * Create position with row and col
     * @param row the row to set row to
     * @param col the col to set col to
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Copy constructor
     * @param other the position to copy
     */
    public Position(Position other) {
        this.row = other.row;
        this.col = other.col;
    }

    /**
     * Gets the column of this position
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the row of this position
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns a new position that represents the
     * transposed position of this position
     * @return new position with row and col switched
     */
    public Position transposed() {
        return new Position(this.col, this.row);
    }

    /**
     * String representation of the position
     * @return the string containing the row and col
     */
    @Override
    public String toString() {
        return "{" + row + ", " + col + "}";
    }

    /**
     * HashCode override
     * Do math on the rows and columns
     * @return the hashcode that is calculated
     */
    @Override
    public int hashCode() {
        return (row * 50 + col) * 97;
    }
}
