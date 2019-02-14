package scrabble.player;

import scrabble.Board;
import scrabble.Node;
import scrabble.Tile;

import java.util.ArrayList;
import java.util.List;

public class CPUPlayer extends Player {
    private List<Tile> anchorSquares;

    public CPUPlayer() {
        anchorSquares = new ArrayList<>();
    }

    @Override
    public int takeTurn(Board board, Node rootNode) {
        //Generate cross checks
        //Get anchor square
        //Get all legal moves for that anchor square
        getAnchorSquares(board);
        for (int i = 0; i < anchorSquares.size(); i++) {
            //leftPart("", rootNode, )
        }
        return 0;
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
