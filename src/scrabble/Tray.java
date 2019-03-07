/**
 * @author Elijah Johnson
 * @description Handles everything to do with
 * tiles in a players hand
 */

package scrabble;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Tray {
    private List<Tile> tiles;
    private boolean dragAndDrop;

    /**
     * Initialize the tiles to a new array list
     */
    public Tray() {
        tiles = new ArrayList<>();
        dragAndDrop = false;
    }

    /**
     * Get all of the displayes of the tiles in the tray
     * @return the list of panes of the tiles that will be displayed
     */
    public List<Pane> getTileDisplay() {
        List<Pane> displays = new ArrayList<>();
        for (Tile tile : tiles) {
            displays.add(tile.getDisplay());
        }
        return displays;
    }


    /**
     * Set the tiles to a new list of tiles
     * @param tiles the new list of tiles
     */
    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * Gets the current tiles in the tray
     * @return the tiles
     */
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Check if a character is in the tray
     * @param c the character to check is in the tray
     * @return true if there is a tile in the tray that has the character c
     * otherwise false
     */
    public boolean isInTray(char c) {
        for (Tile t : tiles) {
            if (t.getCharacter() == c || t.isBlank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Hides the tiles in the tray from the user
     */
    public void hideTray() {
        for (Tile t : tiles) {
            t.hide();
        }
    }

    /**
     * Get the size of the tray
     * should never be greater than seven
     * @return the tiles.size()
     */
    public int size() {
        return tiles.size();
    }

    /**
     * Draw back up to seven after making a play
     * @param manager the tile manager used
     *                to draw new tiles
     */
    public void redrawToSeven(TileManager manager) {
        int size = tiles.size();
        for (int i = size; i < 7; i++) {
            Tile toAdd = manager.drawOne();
            if (toAdd == null) {
                break;
            }
            tiles.add(toAdd);
        }
    }

    /**
     * Removes all of the tiles of toRemove
     * from tiles
     * @param toRemove the tiles to remove from the list
     */
    public void removeAll(List<Tile> toRemove) {
        tiles.removeAll(toRemove);
    }

    /**
     * Removes a single tile with the value of c
     * @param c the character of the tile to remove
     * @return the removed tile
     */
    public Tile removeFromTray(char c) {
        Tile toRemove = null;
        for (Tile t : tiles) {
            if (t.getCharacter() == c || t.isBlank()) {
                toRemove = t;
                break;
            }
        }
        tiles.remove(toRemove);
        return toRemove;
    }

    /**
     * Replaces the tile t with another random tile from the bag
     * @param manager the tile manager to draw from
     * @param t the tile to replace
     */
    public void replace(TileManager manager, Tile t) {
        tiles.remove(t);
        manager.putBack(t);
        tiles.add(manager.drawOne());
    }

    /**
     * Add a tile to the tray
     * @param t the tile to be added
     */
    public void addToTray(Tile t) {
        tiles.add(t);
    }

    /**
     * Sets up drag and drop for all of the tiles currently on the tray
     * @param hand the HBox representing the playes hand
     * @param board the GridPane representing the board
     * @param droppedCallback A Callback for when a tile gets
     *                        dropped onto the board
     * @param returnedCallback A Callback for when a tile gets
     *                         returned from the board
     */
    public void setDragAndDrop(HBox hand,
                               GridPane board,
                               BiConsumer<Tile, Position> droppedCallback,
                               Consumer<Tile> returnedCallback) {
        dragAndDrop = true;
        //Loop through all and set
        for (Tile t : tiles) {
            t.setDragAndDrop(hand,
                    board,
                    droppedCallback,
                    returnedCallback);
        }
    }

    /**
     * Check if the tray contains the tile that has the pane
     * pane
     * @param pane the pane of a tile to check for
     * @return true if the tray has a tile which display matches pane
     * otherwise false
     */
    public boolean contains(Pane pane) {
        for (Tile t : tiles) {
            if (t.getDisplay() == pane) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the tile whose display matches the pane
     * @param pane the pane to match to a tile display
     * @return the tile that is found otherwise null
     */
    public Tile getTile(Pane pane) {
        for (Tile t : tiles) {
            if (t.getDisplay() == pane) {
                return t;
            }
        }
        return null;
    }

    /**
     * Gets the score of the remaining tiles on the tray
     * @return the sum of the tiles values on the tray
     */
    public int getTrayScore() {
        int sum = 0;
        for (Tile t : tiles) {
            sum += t.getScore();
        }
        return sum;
    }

    /**
     * Check if the tray is set to drag and drop
     * @return the member boolean drag and drop
     */
    public boolean isDragAndDrop() {
        return dragAndDrop;
    }

    /**
     * Check if the tray is empty
     * @return if tiles is empty
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    /**
     * The string representation of the tiles
     * @return The string representing the current tray
     */
    @Override
    public String toString() {
        return tiles.toString();
    }
}
