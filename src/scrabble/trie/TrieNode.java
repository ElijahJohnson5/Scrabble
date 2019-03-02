/**
 * @author Elijah Johnson
 * @description TrieNode handles transitioning
 * and printing itself
 */

package scrabble.trie;

import scrabble.DictNode;

public class TrieNode extends DictNode {
    private int nodeId;

    /**
     * Basic constructor for trie node
     * takes a unique nodeId
     * @param nodeId the id to represent this node
     */
    public TrieNode(int nodeId) {
        super();
        this.nodeId = nodeId;
    }

    /**
     * Transition this node along an edge
     * represented by c
     * @param c the character representing the transition
     * @return the node that is reached by the transition
     */
    @Override
    public TrieNode transition(char c) {
        return (TrieNode)characterNodeMap.get(c);
    }

    /**
     * Transition this node along a list of edges
     * @param trans the string of chars representing the transition
     * @return the node reached by transitioning along trans
     */
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

    /**
     * The hash code for this is the unique id
     * @return the unique node id
     */
    @Override
    public int hashCode() {
        return nodeId;
    }

    /**
     * To String for printing out the trie
     * @return the string representation of this node
     */
    @Override
    public String toString() {
        return ((isWord) ? "*" : "") + characterNodeMap.toString();
    }
}
