/**
 * @author Elijah Johnson
 * @description contains logic for the board,
 * including reading in from a file playing words
 * generating crossChecks and anchor squares
 */

package scrabble;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Board {
    private int size;
    private BoardSquare[][] tiles;
    private boolean isEmpty;
    private boolean isTransposed;
    private TileManager tileManager;
    private Map<Position, Integer> crossSum;
    private Set<Position> potentialAnchors;
    private Map<Tile, Position> overriden;

    //GUI
    private boolean initGui;
    private VBox board;

    /**
     * Default values
     */
    public Board() {
        size = 0;
        tiles = null;
        isEmpty = true;
        tileManager = null;
        crossSum = new HashMap<>();
        isTransposed = false;
        initGui = false;
    }

    /**
     * Create a board with a vbox
     * @param board the vbox representing the board
     *              on the display
     */
    public Board(VBox board) {
        this();
        this.board = board;
        isTransposed = false;
        //Tell it to do the rest of the gui stuff
        initGui = true;
        overriden = new HashMap<>();
    }

    /**
     * Initialize from a file representing a board
     * along with a tile manager
     * @param br the file representing the board
     * @param tileManager the tile manager used by the players, used to calculate scores
     * @return true if initialized correctly otherwise false
     */
    public boolean initialize(BufferedReader br, TileManager tileManager) {
        this.tileManager = tileManager;
        try {
            String line;
            line = br.readLine();
            //First line is size of board
            if (line != null) {
                size = Integer.parseInt(line);
                tiles = new BoardSquare[size][size];
            } else {
                System.out.println("Need a size of the board");
                return false;
            }
            //Next size lines are the board
            for (int i = 0; i < size; i++) {
                line = br.readLine();
                parseLine(line, i);
            }
        } catch (IOException e) {
            System.out.println("Could not open the board file");
            return false;
        }
        //Initialize the gui if it is needed
        if (initGui) {
            initializeDisplay();
        }

        return true;
    }

    private void parseLine(String line, int i) {
        if (line == null) {
            System.out.println("File format is incorrect");
            return;
        }
        //Split by spaces for each board square
        String[] values = line.split(" ");
        int j = 0;
        for (String s : values) {
            if (s.length() == 2) {
                if (s.charAt(0) != '.' && s.charAt(0) != '1') {
                    //Letter multiplier
                    tiles[i][j] = new BoardSquare(
                            Integer.parseInt(s.substring(0, 1)), true);
                } else if (s.charAt(1) != '.' && s.charAt(1) != '1') {
                    //Word multiplier
                    tiles[i][j] = new BoardSquare(
                            Integer.parseInt(s.substring(1, 2)), false);
                } else if ((s.charAt(1) == '.' && s.charAt(0) == '.') || (s.charAt(0) == '1' && s.charAt(1) == '1')) {
                    //Default multipliers
                    tiles[i][j] = new BoardSquare();
                }
                j++;
            } else if (s.length() == 1) {
                int score;
                if (Character.isUpperCase(s.charAt(0))) {
                    score = 0;
                } else {
                    score = tileManager.getTileValue(Character.toUpperCase(s.charAt(0)));
                }
                s = s.toUpperCase();
                //Occupied by tile
                isEmpty = false;
                tiles[i][j] = new BoardSquare(new Tile(s.charAt(0),
                        score));
                j++;
            }
        }
    }

    public void initialize(Scanner in, TileManager manager) {
        this.tileManager = manager;
        size = in.nextInt();
        in.nextLine();
        tiles = new BoardSquare[size][size];
        String line;
        for (int i = 0; i < size; i++) {
            line = in.nextLine();
            parseLine(line, i);
        }
    }

    /**
     * Initialize the display of the board for the gui
     */
    private void initializeDisplay() {
        List<HBox> rows = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            HBox row = new HBox();
            for (int j = 0; j < size; j++) {
                //Get displays for each BoardSquare
                row.getChildren().add(tiles[i][j].getDisplay());
            }
            rows.add(row);
        }
        //Add them all to the board
        board.getChildren().addAll(rows);
        board.addEventHandler(DropEvent.DROP_EVENT, dropEvent -> {
            Position pos = dropEvent.getPos();
            System.out.println(pos);

            if (overriden.containsKey(dropEvent.getTile())) {
                Position oldPos = overriden.get(dropEvent.getTile());
                tiles[oldPos.getRow()][oldPos.getCol()].unPlaceTile();
                HBox row = (HBox) board.getChildren().get(oldPos.getRow());
                row.getChildren().set(oldPos.getCol(), tiles[oldPos.getRow()][oldPos.getCol()].getDisplay());
            }

            if (isEmpty(pos.getRow(), pos.getCol())) {
                overriden.put(dropEvent.getTile(), pos);
                tiles[pos.getRow()][pos.getCol()].placeTile(dropEvent.getTile());
                HBox row = (HBox) board.getChildren().get(pos.getRow());
                row.getChildren().set(pos.getCol(), tiles[pos.getRow()][pos.getCol()].getDisplay());
            }
        });
    }

    /**
     * Get the size of the board
     * @return the size of the board
     */
    public int getSize() {
        return size;
    }

    /**
     * Check if the board is empty
     * @return if isEmpty is true
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Check if a board square at the row
     * row and col col is empty
     * @param row the row of the board
     * @param col the col of the board
     * @return if the board square at row col is empty
     */
    public boolean isEmpty(int row, int col) {
        return tiles[row][col].isEmpty();
    }

    /**
     * Get the tile at the row and col
     * @param row the row of the board
     * @param col the col of the board
     * @return null if it is empty or the tile if it isnt
     */
    public Tile getTile(int row, int col) {
        return tiles[row][col].getTile();
    }

    public Tile getTile(Position pos) {
        return tiles[pos.getRow()][pos.getCol()].getTile();
    }

    /**
     * Play a word on the board
     * @param word the string representing the word to be played
     * @param tiles the tiles to play the word from
     * @param start the start position of the word
     * @param end the wnd position of the word
     * @return true if the word was played otherwise false
     */
    public boolean playWord(String word, List<Tile> tiles, Position start,
                            Position end) {
        //Check if the word will be played across or down
        boolean across = (start.getRow() == end.getRow());
        int j;
        Position current = new Position(start);
        //Walk through the word one char at a time
        for (int i = 0; i < word.length(); i++) {
            Tile toPlay = null;
            //If it is empty we need to play a tile
            if (this.tiles[current.getRow()][current.getCol()].isEmpty()) {
                //Get the tile we are going to play
                for (j = 0; j < tiles.size(); j++) {
                    if(tiles.get(j).getCharacter() == word.charAt(i)) {
                        toPlay = tiles.get(j);
                        break;
                    }
                }
                if (j < tiles.size() && toPlay != null) {
                    tiles.remove(j);
                }
                if (toPlay == null) {
                    for (j = 0; j < tiles.size(); j++) {
                        if(tiles.get(j).getCharacter() == word.charAt(i)
                                || tiles.get(j).isBlank()) {
                            toPlay = tiles.get(j);
                            if (initGui) {
                                toPlay.changeBlankText(word.charAt(i));
                            }
                            toPlay.setCharacter(word.charAt(i));
                            break;
                        }
                    }
                    if (j < tiles.size()) {
                        tiles.remove(j);
                    }
                }
                //Play tile
                this.tiles[current.getRow()][current.getCol()].playTile(toPlay);
                if (initGui) {
                    HBox row = (HBox)board.getChildren().get(current.getRow());
                    row.getChildren().set(current.getCol(), this.tiles[current.getRow()][current.getCol()].getDisplay());
                }

                //If it is across play
                if (across) {
                    //Advance current
                    current.incrementCol();
                } else {
                    //Advance current
                    current.incrementRow();
                }
            } else {
                if (across) {
                    //Advance current
                    current.incrementCol();
                }
                else {
                    //Advance current
                    current.incrementRow();
                }
            }
        }
        isEmpty = false;
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(this);
        }
        //Played successfully
        return true;
    }

    /**
     * Get the squares that can be played off of,
     * the anchor squares
     * @return the set of positions that a play can be started from
     */
    public Set<Position> getPotentialAnchorSquares() {
        potentialAnchors = new HashSet<>();
        //If it is empty the only anchor is the middle square
        if (isEmpty) {
            potentialAnchors.add(new Position(size / 2, size / 2));
            return potentialAnchors;
        }
        //Loop through all of the tiles and check
        //if any adjacent squares are filled
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (anyAdjacentFilled(i, j)) {
                    //Add to set if a play can be started here
                    potentialAnchors.add(new Position(i, j));
                }
            }
        }
        //Return the set
        return potentialAnchors;
    }

    /**
     * Check if any adjacent squares are filled
     * @param row the row of the square we are checking
     * @param col the col of the square we are checking
     * @return true if at least one square adjacent to row col is filled
     */
    private boolean anyAdjacentFilled(int row, int col) {
        //If the tile we are on isnt empty return false
        //Cannot play there
        if (!tiles[row][col].isEmpty()) {
            return false;
        }
        //If we can look above the current square
        if (row > 0) {
            if (!tiles[row - 1][col].isEmpty()) {
                return true;
            }
        }
        //Look below the current square
        if (row < size - 1) {
            if (!tiles[row + 1][col].isEmpty()) {
                return true;
            }
        }
        //Look to the left of the current square
        if (col > 0) {
            if (!tiles[row][col - 1].isEmpty()) {
                return true;
            }
        }
        //Look to the right of the current square
        if (col < size - 1) {
            if (!tiles[row][col + 1].isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a set of valid characters to the cross check set
     * that can be played at row and col on the board
     * @param crossCheck the Map of squares on the board to sets of valid
     *                   characters
     * @param row the current row we are cross checking
     * @param col the current col we are cross checking
     * @param dict the dictionary of valid words
     * @param prefix whether we are looking at prefix or suffix
     * @return the value of the cross check letters
     */
    private int addToCrossCheck(Map<Integer, Set<Character>> crossCheck,
                                int row,
                                int col,
                                Dictionary dict,
                                boolean prefix) {
        //Build prefix or suffix based on if prefix is true
        String string = (prefix ? buildPrefixString(row, col)
                : buildSuffixString(row, col));
        Set<Character> c = new HashSet<>();
        //Check all the letters in the alphabet
        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
            //if the string + alphabet or alphabet + string is
            //in the dict add it to the set of valid letters
            if (prefix) {
                if (dict.search(string + alphabet)) {
                    c.add(alphabet);
                }
            } else {
                if (dict.search(alphabet + string)) {
                    c.add(alphabet);
                }
            }
        }
        //Add it to the map, calculate the square number from col
        //row and size
        crossCheck.put(row * size + col, c);
        //Return the value of the letters that are prefixed or suffixed
        return tileManager.getValue(string);
    }

    /**
     * Generates a map of square numbers to set of characters
     * that represent valid characters for that square when
     * making a move across
     * @param dict the dictionary of valid words
     * @return the set of map of squares to sets of characters that
     * represent valid plays for that square
     */
    public Map<Integer, Set<Character>> generateCrossChecks(Dictionary dict) {
        //Reset cross sums to be recalculated
        this.crossSum.clear();
        Map<Integer, Set<Character>> crossCheckMap = new HashMap<>();
        if (isEmpty) {
            //No cross checks can be generated
            return null;
        }
        int crossSum;
        //Loop through the whole board
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //Check the tile to see if a cross check needs to be
                //generated. Calculates crossSum at the same time
                crossSum = checkTiles(dict, crossCheckMap, i, j);
                if (crossSum != -1) {
                    //Add the cross sum for this position to the cross sum map
                    this.crossSum.put(new Position(i, j), crossSum);
                }
                /*else if (!tiles[i][j].isEmpty()) {
                    //Calculate cross sums for non empty tiles
                    String prefix = buildPrefixString(i, j);
                    String suffix = buildSuffixString(i, j);
                    crossSum = tileManager.getValue(prefix + tiles[i][j].getTile().getCharacter() + suffix);
                    this.crossSum.put(new Position(i, j), crossSum);
                }*/
            }
        }

        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(this.crossSum);
        }
        //Return the map
        return crossCheckMap;
    }

    /**
     * Check if we should add to the cross check
     * for this position
     * @param dict the dictionary of valid words
     * @param crossCheckMap the map of cross checks to add to if needed
     * @param row the current row we are on
     * @param col the current col we are on
     * @return the cross sum value if the cross check was added
     * otherwise -1
     */
    private int checkTiles(Dictionary dict, Map<Integer, Set<Character>> crossCheckMap, int row, int col) {
        //If there is a tile above and below
        if (isBelow(row, col) && isAbove(row, col)) {
            //Get prefix and suffix
            String prefix = buildPrefixString(row, col);
            String suffix = buildSuffixString(row, col);
            Set<Character> c = new HashSet<>();
            //Check all the letters of the alphabet
            for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                if (dict.search(prefix + alphabet + suffix)) {
                    c.add(alphabet);
                }
            }
            //Add to the cross check map
            crossCheckMap.put(row * size + col, c);
            //Return the value of prefix and suffix
            int prefixValue = tileManager.getValue(prefix);
            return prefixValue + tileManager.getValue(suffix);
        }
        else if (isBelow(row, col)) {
            //Add to the cross check for the prefix
            return addToCrossCheck(crossCheckMap, row, col, dict, true);
        }
        else if (isAbove(row, col)) {
            //Add to the cross check for the suffix
            return addToCrossCheck(crossCheckMap, row, col, dict, false);
        }
        return -1;
    }

    /**
     * Gets the value of a move based on a position
     * @param word the word that is being evaluated
     * @param move the list of tiles that represents the move
     * @param start the start position for the move
     * @return the value of the move
     */
    public int getValue(String word, List<Tile> move, Position start) {
        List<Tile> moveCopy = new ArrayList<>(move);
        int sum = 0;
        int crossSum = 0;
        int currentCrossSum = 0;
        int j;
        int wordMultiplier = 1;
        Position current = new Position(start);
        //Loop through word
        for (int i = 0; i < word.length(); i++) {
            Tile currentTile = null;
            //Get the tile that is the letter of the current word
            //Remove it from the copy of move
            if (this.tiles[current.getRow()][current.getCol()].isEmpty()) {
                for (j = 0; j < moveCopy.size(); j++) {
                    if (moveCopy.get(j).getCharacter() == word.charAt(i)) {
                        currentTile = moveCopy.get(j);
                        break;
                    }
                }
                if (j < moveCopy.size()) {
                    moveCopy.remove(j);
                }
                //If we didnt find one look for blanks
                if (currentTile == null) {
                    for (j = 0; j < moveCopy.size(); j++) {
                        if (moveCopy.get(j).isBlank()) {
                            currentTile = moveCopy.get(j);
                            break;
                        }
                    }
                    if (j < moveCopy.size()) {
                        moveCopy.remove(j);
                    }
                }
                //Add the score from pos
                currentCrossSum = getScoreFromPos(current);
                currentCrossSum -= prefixOrSuffixContainsBlank(current);
                //Get the word multiplier
                wordMultiplier *= this.tiles[current.getRow()][current.getCol()].getWordMultiplier();
                //Get the letter score multiplied by letter multiplier
                sum += currentTile.getScore() * this.tiles[current.getRow()][current.getCol()].getLetterMultiplier();
                if (potentialAnchors.contains(current) && this.tiles[current.getRow()][current.getCol()].getWordMultiplier() > 1) {
                    sum += currentCrossSum;
                } else {
                    crossSum += currentCrossSum;
                }
                //Increment current
                current = new Position(current.getRow(), current.getCol() + 1);
            } else {
                sum += this.tiles[current.getRow()][current.getCol()].getTile().getScore();
                current = new Position(current.getRow(), current.getCol() + 1);
            }
        }
        //Return the sum * the word multiplier
        //Bonus 50 points for playing all 7
        sum *= wordMultiplier;
        if (move.size() == 7) {
            sum += 50;
        }
        sum += crossSum;
        return sum;
    }

    private int prefixOrSuffixContainsBlank(Position currentPos) {
        int toSubtract = 0;
        if (currentPos.getRow() > 0 && !tiles[currentPos.getRow() - 1][currentPos.getCol()].isEmpty()) {
            if (tiles[currentPos.getRow() - 1][currentPos.getCol()].getTile().isBlank()) {
                toSubtract += tileManager.getTileValue(tiles[currentPos.getRow() - 1][currentPos.getCol()].getTileCharacter());
            }
        }

        if (currentPos.getRow() < size - 1 && !tiles[currentPos.getRow() + 1][currentPos.getCol()].isEmpty()) {
            if (tiles[currentPos.getRow() + 1][currentPos.getCol()].getTile().isBlank()) {
                toSubtract += tileManager.getTileValue(tiles[currentPos.getRow() + 1][currentPos.getCol()].getTileCharacter());
            }
        }

        return toSubtract;
    }

    /**
     * Builds a string representing the prefix of a word
     * @param startingRow the row to start looking for the prefix
     * @param col the col to look for the prefix
     * @return the string representing all of the tiles above the starting row
     */
    private String buildPrefixString(int startingRow, int col) {
        int row = startingRow - 1;
        StringBuilder sb = new StringBuilder();
        while (row >= 0 && !tiles[row][col].isEmpty()) {
            sb.insert(0, tiles[row][col].getTileCharacter());
            row--;
        }
        return sb.toString();
    }

    /**
     * Builds a string representing the suffix of a word
     * @param startingRow the row to start looking for the suffix
     * @param col the col to look for the suffix
     * @return the string representing all of the tiles below the starting row
     */
    private String buildSuffixString(int startingRow, int col) {
        int row = startingRow + 1;
        StringBuilder sb = new StringBuilder();
        while (row <= size - 1 && !tiles[row][col].isEmpty()) {
            sb.append(tiles[row][col].getTileCharacter());
            row++;
        }

        return sb.toString();
    }

    /**
     * Check if the row and col is below a tile
     * @param row the row we are in
     * @param col the col we are int
     * @return true if the tile above row col is not empty
     * otherwise false
     */
    private boolean isBelow(int row, int col) {
        if (row > 0 && tiles[row][col].isEmpty()) {
            return !tiles[row - 1][col].isEmpty();
        }

        return false;
    }

    /**
     * Check if the row and col is above a tile/filled square
     * @param row the row we are in
     * @param col the col we are in
     * @return true if the tile below row col is not empty
     * otherwise false
     */
    private boolean isAbove(int row, int col) {
        if (row < size - 1 && tiles[row][col].isEmpty()) {
            return !tiles[row + 1][col].isEmpty();
        }

        return false;
    }

    /**
     * Transpose the board, used for generating moves for the cpu
     */
    public void transpose() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i; j++) {
                BoardSquare temp = tiles[i][j];
                tiles[i][j] = tiles[j][i];
                tiles[j][i] = temp;
            }
        }

        isTransposed = !isTransposed;
    }

    /**
     * Check if the board is transposed
     * @return whether or not the board is transposed
     */
    public boolean isTransposed() {
        return isTransposed;
    }

    /**
     * Gets a cross sum value from a position
     * @param pos the position to get the cross sum of
     * @return 0 or the value in the cross sum map mapped to pos
     */
    private int getScoreFromPos(Position pos) {
        return crossSum.getOrDefault(pos, 0);
    }

    /**
     * Display the board as a string
     * @return the tiles to string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(tiles[i][j].toString());
            }
            if (i != size - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
