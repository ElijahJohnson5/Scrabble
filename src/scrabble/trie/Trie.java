package scrabble.trie;

import java.util.*;

public class Trie {
    private TrieNode root;

    private int nodeId;

    public Trie() {
        nodeId = 0;
        root = new TrieNode(nodeId++);
    }

    public void insert(String word) {
        TrieNode temp = root;
        for (int i = 0; i < word.length(); i++) {
            if (!temp.getChildren().containsKey(word.charAt(i))) {
                temp.getChildren().put(word.charAt(i), new TrieNode(nodeId++));
            }

            temp = temp.getChildren().get(word.charAt(i));
        }

        temp.setEndOfWord(true);
    }

    public boolean search(String word) {
        TrieNode temp = root;
        for (int i = 0; i < word.length(); i++) {
            if (!temp.getChildren().containsKey(word.charAt(i))) {
                return false;
            }

            temp = temp.getChildren().get(word.charAt(i));
        }

        return (temp != null && temp.isEndOfWord());
    }

    private Set<TrieNode> getNonFinal(Set<TrieNode> allStates) {
        Set<TrieNode> nonFinal = new HashSet<>();
        for (TrieNode t : allStates) {
            if (!t.isEndOfWord()) {
                nonFinal.add(t);
            }
        }
        return nonFinal;
    }

    private Set<TrieNode> getFinal(Set<TrieNode> allStates) {
        Set<TrieNode> finalState = new HashSet<>();
        for (TrieNode t : allStates) {
            if (t.isEndOfWord()) {
                finalState.add(t);
            }
        }
        return finalState;
    }


    @Override
    public String toString() {
        return root.toString();
    }
}
