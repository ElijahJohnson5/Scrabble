package scrabble.trie;

import scrabble.Node;

public class TrieNode extends Node {
    private int nodeId;

    public TrieNode(int nodeId) {
        super();
        this.nodeId = nodeId;
    }

    @Override
    public int hashCode() {
        return Integer.toString(nodeId).hashCode();
    }

    @Override
    public String toString() {
        return ((isWord) ? "*" : "") + characterNodeMap.toString();
    }
}
