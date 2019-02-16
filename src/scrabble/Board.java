package scrabble;

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

    public Board() {
        size = 0;
        tiles = null;
        isEmpty = true;
        tileManager = null;
        crossSum = new HashMap<>();
        isTransposed = false;
    }

    public boolean initialize(File boardFile, TileManager tileManager) {
        this.tileManager = tileManager;
        try {
            BufferedReader br = new BufferedReader(new FileReader(boardFile));
            String line;
            line = br.readLine();
            if (line != null) {
                size = Integer.parseInt(line);
                tiles = new BoardSquare[size][size];
            } else {
                System.out.println("Need a size of the board");
                return false;
            }

            for (int i = 0; i < size; i++) {
                line = br.readLine();
                if (line == null) {
                    System.out.println("File format is incorrect");
                    return false;
                }
                String[] values = line.split(" ");
                int j = 0;
                for (String s : values) {
                    if (s.length() == 2) {
                        if (s.charAt(0) != '.') {
                            tiles[i][j] = new BoardSquare(Integer.parseInt(s.substring(0, 1)), false);
                        } else if (s.charAt(1) != '.') {
                            tiles[i][j] = new BoardSquare(Integer.parseInt(s.substring(1, 2)), true);
                        } else if (s.charAt(1) == '.' && s.charAt(0) == '.') {
                            tiles[i][j] = new BoardSquare();
                        }
                        j++;
                    } else if (s.length() == 1) {
                        isEmpty = false;
                        tiles[i][j] = new BoardSquare(new Tile(s.charAt(0), tileManager.getTileValue(s.charAt(0))));
                        j++;
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("Could not open the board file");
            return false;
        }

        return true;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isEmpty(int row, int col) {
        return tiles[row][col].isEmpty();
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col].getTile();
    }

    public Set<Position> getPotentialAnchorSquares() {
        Set<Position> potentialAnchorSquares = new HashSet<>();
        if (isEmpty) {
            potentialAnchorSquares.add(new Position(size / 2, size / 2));
            return potentialAnchorSquares;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (anyAdjacentFilled(i, j)) {
                    potentialAnchorSquares.add(new Position(i, j));
                }
            }
        }

        return potentialAnchorSquares;
    }

    private boolean anyAdjacentFilled(int row, int col) {
        if (!tiles[row][col].isEmpty()) {
            return false;
        }
        if (row > 0) {
            if (!tiles[row - 1][col].isEmpty()) {
                return true;
            }
        }
        if (row < size - 1) {
            if (!tiles[row + 1][col].isEmpty()) {
                return true;
            }
        }

        if (col > 0) {
            if (!tiles[row][col - 1].isEmpty()) {
                return true;
            }
        }
        if (col < size - 1) {
            if (!tiles[row][col + 1].isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private int addToCrossCheck(Map<Integer, Set<Character>> crossCheck, int row, int col, Dictionary dict, boolean prefix) {
        String string = (prefix ? buildPrefixString(row, col) : buildSuffixString(row, col));
        Set<Character> c = new HashSet<>();
        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
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
        crossCheck.put(row * size + col, c);

        return tileManager.getValue(string);
    }

    public Map<Integer, Set<Character>> generateCrossChecks(Dictionary dict) {
        this.crossSum.clear();
        Map<Integer, Set<Character>> crossCheckMap = new HashMap<>();
        if (isEmpty) {
            return null;
        }
        int crossSum;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                crossSum = checkTiles(dict, crossCheckMap, i, j);
                if (crossSum != -1) {
                    this.crossSum.put(new Position(i, j), crossSum);
                }
            }
        }
        return crossCheckMap;
    }

    private int checkTiles(Dictionary dict, Map<Integer, Set<Character>> crossCheckMap, int row, int col) {
        if (isBelow(row, col) && isAbove(row, col)) {
            String prefix = buildPrefixString(row, col);
            String suffix = buildSuffixString(row, col);
            Set<Character> c = new HashSet<>();
            for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                if (dict.search(prefix + alphabet + suffix)) {
                    c.add(alphabet);
                }
            }
            crossCheckMap.put(row * size + col, c);
            int prefixValue = tileManager.getValue(prefix);
            return prefixValue + tileManager.getValue(suffix);
        }
        else if (isBelow(row, col)) {
            return addToCrossCheck(crossCheckMap, row, col, dict, true);
        }
        else if (isAbove(row, col)) {
            return addToCrossCheck(crossCheckMap, row, col, dict, false);
        }

        return -1;
    }

    private String buildPrefixString(int startingRow, int col) {
        int row = startingRow - 1;
        StringBuilder sb = new StringBuilder();
        while (row >= 0 && !tiles[row][col].isEmpty()) {
            sb.insert(0, tiles[row][col].getTileCharacter());
            row--;
        }

        return sb.toString();
    }

    private String buildSuffixString(int startingRow, int col) {
        int row = startingRow + 1;
        StringBuilder sb = new StringBuilder();
        while (row <= size - 1 && !tiles[row][col].isEmpty()) {
            sb.append(tiles[row][col].getTileCharacter());
            row++;
        }

        return sb.toString();
    }

    private boolean isBelow(int row, int col) {
        if (row > 0 && tiles[row][col].isEmpty()) {
            return !tiles[row - 1][col].isEmpty();
        }

        return false;
    }

    private boolean isAbove(int row, int col) {
        if (row < size - 1 && tiles[row][col].isEmpty()) {
            return !tiles[row + 1][col].isEmpty();
        }

        return false;
    }

    public void transpose() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                BoardSquare temp = tiles[i][j];
                tiles[i][j] = tiles[j][i];
                tiles[j][i] = temp;
            }
        }

        isTransposed = !isTransposed;
    }


    public Map<Position, Integer> getCrossSum() {
        return crossSum;
    }

    public boolean isTransposed() {
        return isTransposed;
    }

    public int getScoreFromPos(Position pos) {
        return crossSum.getOrDefault(pos, 0);
    }


    @Override
    public String toString() {
        return (Arrays.deepToString(tiles));
    }
}
