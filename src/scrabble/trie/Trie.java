package scrabble.trie;


import scrabble.DictionaryInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Trie implements DictionaryInterface {
    private TrieNode root;

    private int nodeId;

    public Trie() {
        nodeId = 0;
        root = new TrieNode(nodeId++);
    }

    @Override
    public void insert(File dict){
        try {
            BufferedReader br = new BufferedReader(new FileReader(dict));
            String word;
            while ((word = br.readLine()) != null) {
                insert(word);
            }
        } catch (IOException e) {
            System.out.println("Could not read dictionary file");
        }
    }

    private void insert(String word) {
        TrieNode temp = root;
        for (int i = 0; i < word.length(); i++) {
            if (!temp.getChildren().containsKey(word.charAt(i))) {
                temp.getChildren().put(word.charAt(i), new TrieNode(nodeId++));
            }

            temp = temp.getChildren().get(word.charAt(i));
        }

        temp.setEndOfWord(true);
    }

    @Override
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

    @Override
    public TrieNode getRootTrieNode() {
        return root;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
