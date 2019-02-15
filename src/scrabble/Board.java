package scrabble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Board {
    private int size;
    private BoardSquare[][] tiles;
    private BoardSquare[][] transposedTiles;
    private boolean isEmpty;

    public Board() {
        size = 0;
        tiles = null;
        transposedTiles = null;
        isEmpty = true;
    }

    public boolean initialize(File boardFile, TileManager tileManager) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(boardFile));
            String line;
            line = br.readLine();
            if (line != null) {
                size = Integer.parseInt(line);
                tiles = new BoardSquare[size][size];
                transposedTiles = new BoardSquare[size][size];
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
                            transposedTiles[j][i] = new BoardSquare(Integer.parseInt(s.substring(0, 1)), false);
                        } else if (s.charAt(1) != '.') {
                            tiles[i][j] = new BoardSquare(Integer.parseInt(s.substring(1, 2)), true);
                            transposedTiles[j][i] = new BoardSquare(Integer.parseInt(s.substring(1, 2)), true);
                        } else if (s.charAt(1) == '.' && s.charAt(0) == '.') {
                            tiles[i][j] = new BoardSquare();
                            transposedTiles[j][i] = new BoardSquare();
                        }
                        j++;
                    } else if (s.length() == 1) {
                        isEmpty = false;
                        tiles[i][j] = new BoardSquare(new Tile(s.charAt(0), tileManager.getTileValue(s.charAt(0))));
                        transposedTiles[j][i] = new BoardSquare(new Tile(s.charAt(0), tileManager.getTileValue(s.charAt(0))));
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

    public Map<Integer, Integer> getPotentialAnchorSquares() {
        Map<Integer, Integer> potentialAnchorSquares = new HashMap<>();
        if (isEmpty) {
            potentialAnchorSquares.put(size / 2, size / 2);
            return potentialAnchorSquares;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (anyAdjacentFilled(i, j)) {
                    potentialAnchorSquares.put(i, j);
                }
            }
        }

        return potentialAnchorSquares;
    }

    private boolean anyAdjacentFilled(int row, int col) {
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

    public Map<Map<Integer, Integer>, Set<Character>> generateCrossChecks() {
        Map<Map<Integer, Integer>, Set<Character>> crossCheckMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0) {
                    if (tiles[i][j].isEmpty() && !tiles[i + 1][j].isEmpty()) {
                        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {

                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return (Arrays.deepToString(tiles) + "\n" + Arrays.deepToString(transposedTiles));
    }
}
