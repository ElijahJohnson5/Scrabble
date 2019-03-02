/**
 * @author Elijah Johnson
 * @description The dictionary abstract parent class, represents
 * a dictionary that can be searched
 */

package scrabble;

import java.io.BufferedReader;

public abstract class Dictionary {
    protected DictNode root;

    /**
     * Searches for a word in the dictionary
     * @param word the word to search for
     * @return true if the word is in the dictionary
     * otherwise false
     */
    abstract public boolean search(String word);

    /**
     * Insert words into the dictionary from
     * a dictionary file
     * @param br the file that contains the dictionary new line seperated
     */
    abstract public void insert(BufferedReader br);

    /**
     * Get the root node of the dictionary
     * @return root
     */
    public DictNode getRootNode() {
        return root;
    }
}
