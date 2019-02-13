package scrabble.trie;

import java.util.HashMap;
import java.util.Map;


public class TrieNode {
    private int nodeId;
    private Map<Character, TrieNode> children;
    private boolean isEndOfWord;

    public TrieNode(int nodeId) {
        children = new HashMap<>();
        isEndOfWord = false;
        this.nodeId = nodeId;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    @Override
    public int hashCode() {
        return Integer.toString(nodeId).hashCode();
    }

    @Override
    public String toString() {
        return ((isEndOfWord) ? "*" : "") + children.toString();
    }
}
