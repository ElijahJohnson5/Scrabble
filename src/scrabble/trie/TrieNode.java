package scrabble.trie;

import scrabble.Node;

public class TrieNode extends Node {
    private int nodeId;

    public TrieNode(int nodeId) {
        super();
        this.nodeId = nodeId;
    }

    @Override
    public TrieNode transition(char c) {
        return (TrieNode)characterNodeMap.get(c);
    }

    @Override
    public TrieNode transition(String trans) {
        TrieNode temp = this;
        int length = trans.length();
        for (int i = 0; i < length; i++) {
            temp = temp.transition(trans.charAt(i));
            if (temp == null) break;
        }

        return temp;
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
