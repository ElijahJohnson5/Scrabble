package scrabble.player;

import scrabble.Board;
import scrabble.Dictionary;
import scrabble.TileManager;
import scrabble.Tray;

public abstract class Player {
    protected TileManager manager;
    protected Tray tray;

    public Player(TileManager manager) {
        this.manager = manager;
        tray = new Tray();
    }

    abstract public int takeTurn(Board board, Dictionary dcit);

}
