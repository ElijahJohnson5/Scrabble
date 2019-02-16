package scrabble;

import scrabble.dawg.Dawg;
import scrabble.trie.Trie;

public class DictionaryFactory {
    public enum DictionaryType {
        DAWG, TRIE
    }


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
