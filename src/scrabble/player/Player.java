/**
 * @author Elijah Johnson
 * @description Player parent class, abstract
 * needs to be implemented by children
 */

package scrabble.player;

import javafx.scene.layout.HBox;
import scrabble.Board;
import scrabble.Dictionary;
import scrabble.TileManager;
import scrabble.Tray;

public abstract class Player {
    protected TileManager manager;
    protected Tray tray;

    //GUI
    protected HBox hand;
    /**
     * Set the manager of each player
     * @param manager the tile manager for the tiles
     */
    public Player(TileManager manager) {
        this.manager = manager;
        tray = new Tray();
        hand = null;
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
