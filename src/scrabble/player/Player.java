package scrabble.player;

import scrabble.Board;
import scrabble.Node;

public abstract class Player {
    abstract public int takeTurn(Board board, Node rootNode);

    protected void legalMove() {

    }
}
