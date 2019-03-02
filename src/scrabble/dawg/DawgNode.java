/**
 * @author Elijah Johnson
 * @description Node for a dawg,
 * handles transitioning, cloning itself
 * adding transitions
 */

package scrabble.dawg;

import scrabble.DictNode;

import java.util.HashMap;
import java.util.Map;


public class DawgNode extends DictNode {
    private int incomingTransition;
    private Integer hashCode;

    /**
     * Basic constructor creates a new node
     * with isWord set to isWord
     * @param isWord whether if stopping at this node
     *               creates a word
     */
    public DawgNode(boolean isWord) {
        super();
        hashCode = null;
        this.isWord = isWord;
    }

    /**
     * Copy constructor does not copy incoming transition
     * @param node The node to be copied
     */
    public DawgNode(DawgNode node) {
        super();
        this.isWord = node.isWord;
        characterNodeMap = new HashMap<>(node.characterNodeMap);
        hashCode = null;
        for (DictNode value : characterNodeMap.values()) {
            DawgNode v = (DawgNode) value;
            v.incomingTransition++;
        }
    }

    /**
     * Gets the incoming transition count
     * @return the incoming transition count
     */
    public int getIncomingTransition() {
        return incomingTransition;
    }

    /**
     * Check if this node has a transition along
     * the letter letter
     * @param letter the letter to check for the
     *               transition
     * @return true if it has a trans otherwise false
     */
    public boolean hasTransition(char letter) {
        return characterNodeMap.containsKey(letter);
    }

    /**
     * Override the transition from Node class
     * Returns the node from transitioning along letter
     * @param letter the letter to transition along
     * @return the new dawg node reached by this transition
     */
    @Override
    public DawgNode transition(char letter) {
        return (DawgNode)characterNodeMap.get(letter);
    }

    /**
     * Get the node reached by transitioning from this node
     * to some other node by each other character in trans
     * @param transStr the string of chars representing the transition
     * @return the new node reached by transitioning on the characters of trans
     */
    @Override
    public DawgNode transition(String transStr) {
        DawgNode temp = this;
        //Use the character transition
        for (int i = 0; i < transStr.length(); i++) {
            temp = temp.transition(transStr.charAt(i));
            if (temp == null) break;
        }

        return temp;
    }

    /**
     * Clones this node into a new node
     * @return a new node using the copy constructor
     */
    public DawgNode clone() {
        return new DawgNode(this);
    }

    /**
     * Cones this node and changes the transition of the
     * parent node
     * @param parent the parent of this node
     * @param transLabel the label from parent to this node to change
     * @return the newly cloned node
     */
    public DawgNode clone(DawgNode parent, char transLabel) {
        DawgNode clone = new DawgNode(this);
        parent.changeTransition(transLabel, this, clone);

        return clone;
    }

    /**
     * Change a transition from this node to newNode
     * @param letter the letter representing the transition to change
     * @param oldNode the node this transition used to go to
     * @param newNode the node to change the transition to
     */
    public void changeTransition(char letter, DawgNode oldNode, DawgNode newNode) {
        oldNode.incomingTransition--;
        newNode.incomingTransition++;

        characterNodeMap.put(letter, newNode);
    }

    /**
     * Adds a transition to this node with a new node
     * @param letter the letter representing the transition
     * @param isWord whether or not following this transition leads
     *               to a new word
     * @return the newly created node that the transition leads to
     */
    public DawgNode addTransition(char letter, boolean isWord) {
        DawgNode newNode = new DawgNode(isWord);
        newNode.incomingTransition++;

        characterNodeMap.put(letter, newNode);

        return newNode;
    }

    /**
     * Clear the hash code variable
     */
    public void clearHash() {
        hashCode = null;
    }

    /**
     * Check if this node has any transitions
     * @return if the character node map is empty
     */
    public boolean hasTransitions() {
        return !characterNodeMap.isEmpty();
    }

    /**
     * Get the amount of transitions out of this node
     * @return the size of the character node map
     */
    public int getTransitionCount() {
        return characterNodeMap.size();
    }

    /**
     * Reduce the transition count by toReduce
     * @param toReduce the amount to reduce the incoming transition count by
     */
    public void reduceTransitionCount(int toReduce) {
        incomingTransition -= toReduce;
    }

    /**
     * Check if this node has 2 or more transitions coming into it
     * @return incomingTransition >= 2
     */
    public boolean hasMultipleIncomingTrans() {
        return incomingTransition >= 2;
    }

    /**
     * Compares the transitions of two nodes
     * @param node1 the first node to compare
     * @param node2 the second node to compare
     * @return true if the character node maps are equivalent
     */
    public static boolean compareTransitions(DawgNode node1, DawgNode node2) {
        Map<Character, DictNode> map1 = node1.characterNodeMap;
        Map<Character, DictNode> map2 = node2.characterNodeMap;
        //If size is not the same not equal
        if (map1.size() == map2.size()) {
            //Loop through and check every node for transitions
            for (Map.Entry<Character, DictNode> keyValue : map1.entrySet()) {
                char current = keyValue.getKey();
                DawgNode currentNode = (DawgNode)keyValue.getValue();

                if (!map2.containsKey(current) || !map2.get(current).equals(currentNode)) {
                    return false;
                }
            }
        }
        else {
            return false;
        }

        return true;
    }

    /**
     * Equals override for use in Collections
     * Checks if this object equals another
     * @param obj the obj to check equals this one
     * @return true if the objects are equal otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof DawgNode)) {
            return false;
        }

        DawgNode that = (DawgNode)obj;
        //Compare isWord and compareTransitions
        return (isWord == that.isWord && compareTransitions(this, that));
    }

    /**
     * Custom hashCode for use in collections
     * Calculates the hash code
     * @return the hashCode for this item
     */
    @Override
    public int hashCode() {
        if (hashCode == null) {
            //Start with prime number
            int hash = 11;
            //Calculate hash code recursively
            hash = 37 * hash + (this.isWord ? 1 : 0);
            hash = 37 * hash + (this.characterNodeMap != null ? characterNodeMap.hashCode() : 0);
            //Store it so it doesnt have to be calculated again
            hashCode = hash;
            return hash;
        }
        else {
            return hashCode;
        }
    }
}
