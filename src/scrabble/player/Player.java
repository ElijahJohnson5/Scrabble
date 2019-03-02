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
    protected Board board;

    //GUI
    protected HBox hand;

    public Player(TileManager manager, Board board, List<Tile> tray) {
        this(manager, board);
        this.tray.setTiles(tray);
    }


    /**
     * Set the manager of each player
     * @param manager the tile manager for the tiles
     */
    public Player(TileManager manager, Board board) {
        this.manager = manager;
        this.board = board;
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
    public Player(TileManager manager, Board board, HBox hand) {
        this(manager, board);
        this.hand = hand;
    }

    public int getLeftoverTileScore() {
        if (tray != null) {
            return tray.getTrayScore();
        }
        return 0;
    }

    /**
     * Takes a turn for the player, implemented
     * by child class
     * @param dict the dictionary to use for checking valid plays
     * @return 0 if the
     */
    abstract public int takeTurn(Dictionary dict);

    /**
     * Gets the last word that this player played
     * @return the string that was the last word this player played
     */
    abstract public String getLastWordPlayed();

    /**
     * Gets the last word score that this player played
     * @return the int that was how many points they got for the last word
     */
    abstract public int getLastWordPlayedScore();

}
