/**
 * @author Elijah Johnson
 * @description Handles creations of dictionaries
 */

package scrabble;

import scrabble.dawg.Dawg;
import scrabble.trie.Trie;

public class DictionaryFactory {
    /**
     * Types of possible dictionaries
     */
    public enum DictionaryType {
        DAWG, TRIE
    }

    /**
     * Creates a new dictionary of type type
     * @param type the type of dictionary to make
     * @return the newly created dictionary
     */
    public static Dictionary createDict(DictionaryType type) {
        switch (type) {
            case DAWG:
                return new Dawg();
            case TRIE:
                return new Trie();
        }

        return new Dawg();
    }
}
