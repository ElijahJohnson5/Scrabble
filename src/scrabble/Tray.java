/**
 * @author Elijah Johnson
 * @description Handles everything to do with
 * tiles in a players hand
 */

package scrabble;

import java.util.ArrayList;
import java.util.List;

public class Tray {
    private List<Tile> tiles;

    /**
     * Initialize the tiles to a new array list
     */
    public Tray() {
        tiles = new ArrayList<>();
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
            tiles.add(manager.drawOne());
        }
    }

    /**
     * Removes all of the tiles of toRemove
     * from tiles
     * @param toRemove the tiles to remove from the list
     */
    public void removeAll(List<Tile> toRemove) {
        int j;
        //Make sure to not remove duplicates
        for (int i = 0; i < size(); i++) {
            for (j = 0; j < toRemove.size(); j++) {
                if (toRemove.get(j).getCharacter() == tiles.get(i).getCharacter()) {
                    tiles.remove(i);
                    break;
                }
            }
            toRemove.remove(j);
        }
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
     * Add a tile to the tray
     * @param t the tile to be added
     */
    public void addToTray(Tile t) {
        tiles.add(t);
    }

    @Override
    public String toString() {
        return tiles.toString();
    }
}
