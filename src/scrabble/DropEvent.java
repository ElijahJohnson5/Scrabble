/**
 * @name Elijah Johnson
 * @description
 **/

package scrabble;

import javafx.event.Event;
import javafx.event.EventType;

public class DropEvent extends Event {
    private final Position pos;
    private final Tile tile;

    public static final EventType<DropEvent> DROP_EVENT = new EventType<>(Event.ANY,  "DROP_EVENT");

    public DropEvent(Position pos, Tile tile) {
        super(DROP_EVENT);
        this.pos = pos;
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public Position getPos() {
        return pos;
    }
}
