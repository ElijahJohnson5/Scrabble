/**
 * @author Elijah Johnson
 * @description Handles keeping track of the tiles that are
 * left and drawing tiles from the bag randomly
 */

package scrabble;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class TileManager {
    private List<Tile> bag;
    private Map<Character, Integer> valueMap;
    private Map<Character, Integer> countMap;

    /**
     * Initialize all values to new instances
     */
    public TileManager() {
        bag = new ArrayList<>();
        valueMap = new HashMap<>();
        countMap = new HashMap<>();
    }

    /**
     * Initialize the tile manager based
     * on a file containing the letter distribution to be used
     * @param br the file that contains the letter
     *                   distribution to be used
     * @return true if it initializes correctly otherwise false
     */
    public boolean initialize(BufferedReader br) {
        countMap.clear();
        valueMap.clear();
        bag.clear();
        try {
            String line;
            //Read line by line
            //First char represents value
            while ((line = br.readLine()) != null) {
                String[] array = line.split(" ");
                int value = Integer.parseInt(line.substring(0,
                        line.indexOf(":")));
                //Inside { represent count and which letters have that count
                for (int i = 1; i < array.length; i++) {
                    int count = Integer.parseInt(
                            array[i].substring(1, array[i].indexOf(":")));
                    String[] chars = array[i].
                            substring(array[i].indexOf(":") + 1).split(",");
                    for (String s : chars) {
                        //Add to the count map and value map
                        countMap.put(s.charAt(0), count);
                        valueMap.put(s.charAt(0), value);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot read the letter dist file");
            return false;
        }
        //Create the bag
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                bag.add(new Tile(entry.getKey(), valueMap.get(entry.getKey())));
            }
        }
        return true;
    }

    /**
     * Gets the value of a string
     * @param string the string to get the value of
     * @return the value of string if all the letter values were added together
     */
    public int getValue(String string) {
        int sum = 0;
        for (int i = 0; i < string.length(); i++) {
            sum += getTileValue(string.charAt(i));
        }
        return sum;
    }

    /**
     * Gets a single character value
     * @param character the character to get the value of
     * @return 0 or the value in the value map
     */
    public int getTileValue(char character) {
        return valueMap.getOrDefault(character, 0);
    }

    /**
     * Draws a new hand of seven tiles
     * @return the newly drawn list of tiles
     */
    public List<Tile> drawTray(int count) {
        List<Tile> tray = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            //Draw each tile randomly
            Tile toAdd = drawOne();
            if (toAdd == null) {
                break;
            }
            tray.add(toAdd);
        }
        return tray;
    }

    /**
     * Check if the bag is empty
     * @return bag.isEmpty();
     */
    public boolean isEmpty() {
        return bag.isEmpty();
    }

    /**
     * Draw one random tile from the bag
     * @return the random tile from the bag
     */
    public Tile drawOne() {
        Random r = new Random();
        Tile toGet = null;
        if (bag.size() > 0) {
            int next = r.nextInt(bag.size());
            toGet = bag.get(next);
            bag.remove(next);
        }
        return toGet;
    }

    /**
     * Redraw a new tray of tiles, puts the tiles
     * from the tray back into the bag
     * @param tray the tray to put back
     * @return the newly drawn tray
     */
    public List<Tile> redrawTray(List<Tile> tray, int count) {
        List<Tile> newTray = drawTray(count);
        bag.addAll(tray);
        return newTray;
    }

    /**
     * Puts a tile back in the bag
     * @param t the tile to be put back in the bag
     */
    public void putBack(Tile t) {
        bag.add(t);
    }
}
