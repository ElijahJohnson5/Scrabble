package scrabble.player;

import scrabble.Board;
import scrabble.Node;
import scrabble.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CPUPlayer extends Player {
    private Map<Map<Integer, Integer>, Set<Character>> crossChecks;

    public CPUPlayer() {

    }

    @Override
    public int takeTurn(Board board, Node rootNode) {

        //Generate cross checks
        //Get anchor square
        //Get all legal moves for that anchor square
        return 0;
    }

    private void generateCrossChecks() {

    }

    private void getAnchorSquares(Board board) {

    }

    private int leftPart(String partialWord, Node n, int limit) {
        return 0;
    }

    private int extendRight(String partialWord, Node n, Tile square) {

        return 0;
    }
}
