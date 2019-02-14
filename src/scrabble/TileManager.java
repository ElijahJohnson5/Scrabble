package scrabble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TileManager {
    private List<Tile> bag;
    private Map<Character, Integer> valueMap;
    private Map<Character, Integer> countMap;

    public TileManager() {
        bag = new ArrayList<>();
        valueMap = new HashMap<>();
        countMap = new HashMap<>();
    }

    public boolean initialize(File letterDist) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(letterDist));
            String line;
            while ((line = br.readLine()) != null) {
                String[] array = line.split(" ");
                int value = Integer.parseInt(line.substring(0, line.indexOf(":")));
                for (int i = 1; i < array.length; i++) {
                    int count = Integer.parseInt(array[i].substring(1, array[i].indexOf(":")));
                    String[] chars = array[i].substring(array[i].indexOf(":") + 1).split(",");
                    for (String s : chars) {
                        countMap.put(s.charAt(0), count);
                        valueMap.put(s.charAt(0), value);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot read the letter dist file");
            return false;
        }

        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                bag.add(new Tile(entry.getKey(), valueMap.get(entry.getKey())));
            }
        }

        return true;
    }

    public int getTileValue(char character) {
        return valueMap.get(character);
    }
}
