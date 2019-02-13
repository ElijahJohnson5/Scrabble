package scrabble.trie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public String[] getPossibleWords(String tiles) {
        for (int i = tiles.length() - 1; i > 0; i--) {
            Set<String> set = permute(tiles.substring(0, i + 1), 0, i);
            System.out.println(set);
            for (String s : set) {
                if (search(s)) {
                    System.out.println(s);
                }
            }
        }

        return null;
    }

    private Set<String> permute(String str, int startIndex, int endIndex) {
        Set<String> set = new HashSet<>();
        if (startIndex == endIndex) {
            set.add(str);
            return set;
        } else {
            for (int i = startIndex; i <= endIndex; i++) {
                str = swap(str, startIndex, i);
                set.addAll(permute(str, startIndex + 1, endIndex));
                str = swap(str, startIndex, i);
            }

            return set;
        }
    }

    private String swap(String str, int i, int j) {
        char temp;
        char[] charArray = str.toCharArray();
        temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
