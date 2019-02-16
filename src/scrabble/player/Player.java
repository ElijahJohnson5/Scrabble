package scrabble.player;

import scrabble.Board;
import scrabble.Dictionary;
import scrabble.TileManager;
import scrabble.Tray;

public abstract class Player {
    protected TileManager manager;
    protected Tray tray;

    /**
     * Set the manager of each player
     * @param manager the tile manager for the tiles
     */
    public Player(TileManager manager) {
        this.manager = manager;
        tray = new Tray();
    }

    /**
     * Takes a turn for the player, implemented
     * by child class
     * @param board the current board state
     * @param dict the dictionary to use for checking valid plays
     * @return
     */
    abstract public int takeTurn(Board board, Dictionary dict);

}
