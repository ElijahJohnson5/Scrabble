/**
 * @author Elijah Johnson
 * @description Player parent class, abstract
 * needs to be implemented by children
 */

package scrabble.player;

import javafx.scene.layout.HBox;
import scrabble.*;

import java.util.List;

public abstract class Player {
    protected TileManager manager;
    protected Tray tray;

    //GUI
    protected HBox hand;

    public Player(TileManager manager, List<Tile> tray) {
        this(manager);
        this.tray.setTiles(tray);
    }


    /**
     * Set the manager of each player
     * @param manager the tile manager for the tiles
     */
    public Player(TileManager manager) {
        this.manager = manager;
        tray = new Tray();
        hand = null;
    }

    public boolean isHandEmpty() {
        return tray.isEmpty();
    }

    /**
     * Constructor used for gui
     * Initialize the hand hbox to the hand
     * given
     * @param manager the tile manager for the tiles
     * @param hand the hbox representing the players hand in the gui
     */
    public Player(TileManager manager, HBox hand) {
        this(manager);
        this.hand = hand;
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
