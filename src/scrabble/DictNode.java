/**
 * @author Elijah Johnson
 * @description The abstract dict node,
 * every node has a boolean for isWord
 * and a node map, Child classes can use this to make
 * compatible with dictionary
 */

package scrabble;

import java.util.HashMap;
import java.util.Map;

abstract public class DictNode {
    protected Map<Character, DictNode> characterNodeMap;
    protected boolean isWord;

    /**
     * Creates a new dict node with default values
     */
    public DictNode() {
        characterNodeMap = new HashMap<>();
        isWord = false;
    }

    /**
     * Get the node reached by transitioning from this node
     * to some other node by c
     * @param c the character representing the transition
     * @return the node reached by transitioning on c
     */
    abstract public DictNode transition(char c);

    /**
     * Get the node reached by transitiong from this node
     * to some other node by each other character in trans
     * @param trans the string of chars representing the transition
     * @return the new node reached by transitioning on the characters of trans
     */
    abstract public DictNode transition(String trans);

    /**
     * Get the Character node map representing the transitions
     * @return the character node map
     */
    public Map<Character, DictNode> getCharacterNodeMap() {
        return characterNodeMap;
    }

    /**
     * Set if this node represents a word
     * @param word true if this node represents
     *             a word if you stopped here
     */
    public void setWord(boolean word) {
        isWord = word;
    }

    /**
     * Check if you stopped at this node if it would
     * be a valid word
     * @return isWord
     */
    public boolean isWord() {
        return isWord;
    }
}
