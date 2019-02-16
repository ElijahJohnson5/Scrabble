package scrabble;

import java.util.ArrayList;
import java.util.List;

public class Tray {
    private List<Tile> tiles;

    public Tray() {
        tiles = new ArrayList<>();
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }


    public boolean isInTray(char c) {
        for (Tile t : tiles) {
            if (t.getCharacter() == c || t.isBlank()) {
                return true;
            }
        }
        return false;
    }

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

    public void addToTray(Tile t) {
        tiles.add(t);
    }
}
