/**
 * @author Elijah Johnson
 * @description Trie implementation
 * just like dog, less complicated, takes up
 * a lot more memory
 */

package scrabble.trie;

import scrabble.Dictionary;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Trie extends Dictionary {
    private int nodeId;

    /**
     * Create root node with an id of 0
     */
    public Trie() {
        nodeId = 0;
        root = new TrieNode(nodeId++);
    }

    /**
     * Inserts a list of words in a file into the dictionary
     * @param br the file containing the dictionary to be stored
     *             int the trie
     */
    @Override
    public void insert(BufferedReader br){
        List<String> words = new ArrayList<>();
        try {
            String word;
            //Read each word and make it uppercase
            while ((word = br.readLine()) != null) {
                word = word.toUpperCase();
                words.add(word);
            }
            //Sort the words
            Collections.sort(words);
            br.close();
        } catch (IOException e) {
            System.out.println("Could not read dictionary file");
        }
        //Actually insert each word in the trie
        for (String word : words) {
            insert(word);
        }
        //Make sure the root is not a word
        root.setWord(false);
    }

    /**
     * Inserts a word into the trie
     * @param word the word to be inserted
     */
    private void insert(String word) {
        TrieNode temp = (TrieNode) root;
        for (int i = 0; i < word.length(); i++) {
            //If it doesnt contain the key, add it
            if (!temp.getCharacterNodeMap().containsKey(word.charAt(i))) {
                temp.getCharacterNodeMap().put(word.charAt(i), new TrieNode(nodeId++));
            }
            //Walk down the nodes
            temp = (TrieNode) temp.getCharacterNodeMap().get(word.charAt(i));
        }
        temp.setWord(true);
    }

    /**
     * Searches for a word in the dictionary
     * @param word the word to check is in the dictionary
     * @return true if it is a word, otherwise false
     */
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

    /**
     * To string used for debugging
     * @return the root node to string
     */
    @Override
    public String toString() {
        return root.toString();
    }
}
