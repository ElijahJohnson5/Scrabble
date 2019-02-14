package scrabble;

import scrabble.dawg.Node;
import scrabble.trie.TrieNode;

import java.io.File;

public interface DictionaryInterface {

    boolean search(String word);

    void insert(File dict);

    default Node getRootDawgNode() {
        return null;
    }

    default TrieNode getRootTrieNode() {
        return null;
    }
}
