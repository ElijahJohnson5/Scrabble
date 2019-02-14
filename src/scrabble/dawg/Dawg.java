package scrabble.dawg;

import scrabble.DictionaryInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Dawg implements DictionaryInterface<DawgNode> {
    private DawgNode startNode;
    private Map<DawgNode, DawgNode> equivalenceClass;
    private Set<Character> charSet;
    private int transitionCount;

    /**
     * Creates a new Directed Acyclic Word Graph
     */
    public Dawg() {
        //Initialize start node and it is not a word
        startNode = new DawgNode(false);
        equivalenceClass = new HashMap<>();
        charSet = new TreeSet<>();
    }

    /**
     * Get the longest prefix of the string str already
     * represented in the graph
     * @param str the str to get the longest prefix of
     * @return the string that is the longest prefix
     */
    private String determineLongestPrefix(String str) {
        DawgNode node = startNode;
        int i;
        for (i = 0; i < str.length(); i++) {
            //If we can transition do it otherwise leave the loop
            if (node.hasTransition(str.charAt(i))) {
                node = node.transition(str.charAt(i));
            }
            else {
                break;
            }
        }

        return str.substring(0, i);
    }

    /**
     * Gets the first node that has multiple outgoing transitions
     * @param startNode the node to start at
     * @param transStr the transition str to follow
     * @return a map containing strings as keys, and objects. Will always
     * be a size of two with the first object being an i and the
     * second being a node
     */
    private Map<String, Object> getFirstMultipleTransition(DawgNode startNode, String transStr) {
        DawgNode temp = startNode;
        int i = 0;
        for (; i < transStr.length(); i++) {
            char curr = transStr.charAt(i);
            //Transition if possible
            if (temp.hasTransition(curr)) {
                temp = temp.transition(curr);
            } else {
                temp = null;
            }

            //Break out if we found the first one or if the transition failed
            if (temp == null || temp.hasMultipleIncomingTrans()) {
                break;
            }
        }

        //Create return value
        Map<String, Object> map = new HashMap<>(2);
        map.put("charIndex", (temp == startNode || i == transStr.length() ? null : i));
        map.put("multipleNode", (temp == startNode || i == transStr.length() ? null : temp));

        return map;
    }

    /**
     * Remove the transition path represented by transStr
     * @param transStr the transition string to remove
     */
    private void removeTransitionPath(String transStr) {
        DawgNode temp = startNode;

        for (int i = 0; i < transStr.length(); i++) {
            //Transition through the string one at a time
            temp = temp.transition(transStr.charAt(i));
            if (equivalenceClass.get(temp) == temp) {
                //Remove from equivalence class
                equivalenceClass.remove(temp);
            }
            //Clear the hash
            temp.clearHash();
        }
    }

    /**
     * Clones the transition path from the pivot node to the trans
     * string node
     * @param pivotNode the node to clone up to
     * @param transToNode the transition string to the pivot node
     * @param trans the transition string to clone up to
     */
    private void cloneTransitionPath(DawgNode pivotNode, String transToNode, String trans) {
        //Get the last node to be cloned
        DawgNode lastNode = pivotNode.transition(trans);
        DawgNode lastCloned = null;
        char lastLabelChar = '\0';

        for (int i = trans.length(); i >= 0; i--) {
            String currentString = (i > 0 ? trans.substring(0, i) : null);
            DawgNode currentTarget = (i > 0 ? pivotNode.transition(currentString) : pivotNode);
            DawgNode cloned;

            //We are at the last node
            if (i == 0) {
                String transToNodeParent = transToNode.substring(0, transToNode.length() - 1);
                //Clone from the parent to the pivot node
                cloned = pivotNode.clone(startNode.transition(transToNodeParent), transToNode.charAt(transToNode.length() - 1));
            }
            else {
                //Clone the node
                cloned = currentTarget.clone();
            }

            //Add to our transition count
            transitionCount += cloned.getTransitionCount();

            //Update the cloned transition correctly
            if (lastCloned != null) {
                cloned.changeTransition(lastLabelChar, lastNode, lastCloned);
            }
            //Get the last label and the last cloned
            lastCloned = cloned;
            lastLabelChar = (i > 0 ? trans.charAt(i - 1) : '\0');
        }
    }

    /**
     * Adds the string to the graph
     * @param str the string to add the graph
     */
    private void addString(String str) {
        //Get the longest prefix that is already in the graph
        String prefix = determineLongestPrefix(str);
        //Get the suffix to be added
        String suffix = str.substring(prefix.length());

        //Get the first node that has multiple out going transitions
        Map<String, Object> map = getFirstMultipleTransition(startNode, prefix);
        //Get the two things returned can be null
        DawgNode multipleNode = (DawgNode) map.get("multipleNode");
        Integer index = (Integer) map.get("charIndex");

        //Remove the transition path from startnode to the transStr
        removeTransitionPath((index == null ? prefix : prefix.substring(0, index)));

        if (multipleNode != null && index != null) {
            String transitionPath = prefix.substring(0, index + 1);
            String transitionToClone = prefix.substring(index + 1);
            //Clone the transition path
            cloneTransitionPath(multipleNode, transitionPath, transitionToClone);
        }

        //Create new transition path from the startNode transitioned to the prefix
        //to the suffix
        addTransitionPath(startNode.transition(prefix), suffix);
    }

    private void addTransitionPath(DawgNode startNode, String str) {
        if (!str.isEmpty()) {
            DawgNode temp = startNode;
            for (int i = 0; i < str.length(); i++, transitionCount++) {
                temp = temp.addTransition(str.charAt(i), (i == str.length() - 1));

                charSet.add(str.charAt(i));
            }

        } else {
            startNode.setWord(true);
        }
    }

    /**
     * Minimize the DFA as we go
     * Removes and re makes transitions for duplicate nodes
     * @param startNode the node to start the minimization at
     * @param transStr the string of transitions to minimize
     */
    private void minimize(DawgNode startNode, String transStr) {
        char labelChar = transStr.charAt(0);
        //Get the first target node
        DawgNode targetNode = startNode.transition(labelChar);
         //If the target has children and the transStr is not empty
        //Recursively call minimize on the target node and the substring of transstr
        if (targetNode.hasTransitions() && !transStr.substring(1).isEmpty()) {
            minimize(targetNode, transStr.substring(1));
        }

        //Check if we have a node equivalent to the target node
        DawgNode equivNode = equivalenceClass.get(targetNode);

        //If we dont have equivalence add to the equivalence class and be done
        if (equivNode == null) {
            equivalenceClass.put(targetNode, targetNode);
            //If the two nodes are not exactly the same node
        } else if (equivNode != targetNode){
            //Reduce it by one
            targetNode.reduceTransitionCount(1);
            transitionCount -= targetNode.getIncomingTransition();
            //Change the transition of from the startNode to the targetNode
            //to be to the equivNode
            startNode.changeTransition(labelChar, targetNode, equivNode);
        }
    }

    /**
     * Get the starting index for the substring we need to add based on previous
     * @param prev the previously added string into the graph
     * @param curr the current string being added to the graph
     * @return the index of the substring to get or -1 if we need a new graph to start
     */
    private int calcStartIndex(String prev, String curr) {
        int i;
        if (!curr.startsWith(prev)) {
            int min = Math.min(curr.length(), prev.length());
            for (i = 0; i < min; i++) {
                if (prev.charAt(i) != curr.charAt(i)) {
                    break;
                }
            }
        }
        else {
            return -1;
        }

        return i;
    }

    /**
     * Check if a word is in the graph
     * @param word the word to check
     * @return true if the word is in the graph or false it not
     */
    @Override
    public boolean search(String word) {
        //Transition from the startNode to the word
        DawgNode target = startNode.transition(word);
        //Check if its a word and if its not null
        return (target != null && target.isWord());
    }

    @Override
    public DawgNode getRootNode() {
        return startNode;
    }

    @Override
    public void insert(File dict) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(dict));
            String curr;
            String prev = "";
            //Read all lines of dictionary
            while ((curr = br.readLine()) != null) {
                int index = calcStartIndex(prev, curr);

                if (index != -1) {
                    //Get the transition sub string and the suffix to add
                    String transSub = prev.substring(0, index);
                    String suffix = prev.substring(index);
                    //Minimize the graph as we go
                    minimize(startNode.transition(transSub), suffix);
                }
                //Add the string to the graph
                addString(curr);
                prev = curr;
            }
            //Minimize the final version
            minimize(startNode, prev);
        } catch (IOException e) {
            System.out.println("Could not read dictionary file");
        }
    }
}
