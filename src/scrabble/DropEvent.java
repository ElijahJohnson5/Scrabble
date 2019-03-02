/**
 * @name Elijah Johnson
 * @description Custom dropEvent
 * gets fired to the grid pane representing the board when a tile gets dropped
 **/

package scrabble;

import javafx.event.Event;
import javafx.event.EventType;

public class DropEvent extends Event {
    /**
     * Values needed by the board
     */
    private final Position pos;
    private final Tile tile;
    private final boolean reset;

    //Create new event type for this event
    public static final EventType<DropEvent> DROP_EVENT
            = new EventType<>(Event.ANY,  "DROP_EVENT");

    /**
     * Constructor taking necessary information
     * @param pos the position of the tile
     * @param tile the tile being dropped
     * @param reset whether to reset the tile or not
     */
    public DropEvent(Position pos, Tile tile, boolean reset) {
        super(DROP_EVENT);
        this.pos = pos;
        this.tile = tile;
        this.reset = reset;
    }

    /**
     * Gets the tile member variable
     * @return the tile member variable
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Gets the pos member variable
     * @return the pos member variable
     */
    public Position getPos() {
        return pos;
    }

    /**
     * Gets the reset member boolean
     * @return the boolean of whether to reset this tile or not
     */
    public boolean isReset() {
        return reset;
    }
}
