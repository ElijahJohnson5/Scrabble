package scrabble.trie;

import scrabble.DictionaryInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Trie extends DictionaryInterface {
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
                word = word.toUpperCase();
                insert(word);
            }
        } catch (IOException e) {
            System.out.println("Could not read dictionary file");
        }
    }

    private void insert(String word) {
        TrieNode temp = (TrieNode)root;
        for (int i = 0; i < word.length(); i++) {
            if (!temp.getCharacterNodeMap().containsKey(word.charAt(i))) {
                temp.getCharacterNodeMap().put(word.charAt(i), new TrieNode(nodeId++));
            }

            temp = (TrieNode)temp.getCharacterNodeMap().get(word.charAt(i));
        }

        temp.setWord(true);
    }

    @Override
    public boolean search(String word) {
        TrieNode temp = (TrieNode)root;
        for (int i = 0; i < word.length(); i++) {
            if (!temp.getCharacterNodeMap().containsKey(word.charAt(i))) {
                return false;
            }
            temp = (TrieNode)temp.getCharacterNodeMap().get(word.charAt(i));
        }

        return (temp != null && temp.isWord());
    }

    @Override
    public TrieNode getRootNode() {
        return (TrieNode)root;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
