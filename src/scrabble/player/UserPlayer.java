package scrabble.player;

import scrabble.Board;
import scrabble.Dictionary;
import scrabble.TileManager;

public class UserPlayer extends Player {

    public UserPlayer(TileManager manager) {
        super(manager);
    }

    @Override
    public int takeTurn(Board board, Dictionary dict) {
        return 0;
    }
}
