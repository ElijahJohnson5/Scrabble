package scrabble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Board {
    private int size;
    private Tile[][] tiles;
    private Tile[][] transposedTiles;

    public boolean initialize(File boardFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(boardFile));
            String line;
            line = br.readLine();
            if (line != null) {
                size = Integer.parseInt(line);
                tiles = new Tile[size][size];
                transposedTiles = new Tile[size][size];
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
                            tiles[i][j] = new Tile(Integer.parseInt(s.substring(0, 1)), false);
                            transposedTiles[j][i] = new Tile(Integer.parseInt(s.substring(0, 1)), false);
                        } else if (s.charAt(1) != '.') {
                            tiles[i][j] = new Tile(Integer.parseInt(s.substring(1, 2)), true);
                            transposedTiles[j][i] = new Tile(Integer.parseInt(s.substring(1, 2)), true);
                        } else if (s.charAt(1) == '.' && s.charAt(0) == '.') {
                            tiles[i][j] = new Tile();
                            transposedTiles[j][i] = new Tile();
                        }
                        j++;
                    } else if (s.length() == 1) {
                        tiles[i][j] = new Tile(s.charAt(0), 0);
                        transposedTiles[j][i] = new Tile(s.charAt(0), 0);
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

    @Override
    public String toString() {
        return (Arrays.deepToString(tiles) + "\n" + Arrays.deepToString(transposedTiles));
    }
}
