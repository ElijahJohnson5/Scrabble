package scrabble.dawg;

import java.util.*;

public class Node {

    private Map<Character, Node> transitionMap;

    private int incomingTransition;
    private Integer hashCode;
    private boolean isWord;

    public Node(boolean isWord) {
        transitionMap = new TreeMap<>();
        hashCode = null;
        this.isWord = isWord;
    }

    public Node(Node node) {
        this.isWord = node.isWord;
        transitionMap = new TreeMap<>(node.transitionMap);
        hashCode = null;
        for (Node value : transitionMap.values()) {
            value.incomingTransition++;
        }
    }

    public Map<Character, Node> getTransitionMap() {
        return transitionMap;
    }

    public int getIncomingTransition() {
        return incomingTransition;
    }

    public boolean hasTransition(char letter) {
        return transitionMap.containsKey(letter);
    }

    public Node transition(char letter) {
        return transitionMap.get(letter);
    }

    public Node transition(String transStr) {
        Node temp = this;

        for (int i = 0; i < transStr.length(); i++) {
            temp = temp.transition(transStr.charAt(i));
            if (temp == null) break;
        }

        return temp;
    }

    public Node clone() {
        return new Node(this);
    }

    public Node clone(Node parent, char transLabel) {
        Node clone = new Node(this);
        parent.changeTransition(transLabel, this, clone);

        return clone;
    }

    public List<Node> getPathNodes(String transStr) {
        List<Node> list = new LinkedList<>();
        Node temp = this;

        for (int i = 0; i < transStr.length(); i++) {
            temp = temp.transition(transStr.charAt(i));
            list.add(temp);
            if (temp == null) break;
        }

        return list;
    }

    public void changeTransition(char letter, Node oldNode, Node newNode) {
        oldNode.incomingTransition--;
        newNode.incomingTransition++;

        transitionMap.put(letter, newNode);
    }

    public Node addTransition(char letter, boolean isWord) {
        Node newNode = new Node(isWord);
        newNode.incomingTransition++;

        transitionMap.put(letter, newNode);

        return newNode;
    }

    public void clearHash() {
        hashCode = null;
    }

    public void removeTransition(char letter) {
        transitionMap.remove(letter);
    }

    public boolean hasTransitions() {
        return !transitionMap.isEmpty();
    }

    public int getTransitionCount() {
        return transitionMap.size();
    }

    public void reduceTransitionCount(int toReduce) {
        incomingTransition -= toReduce;
    }

    public boolean hasMultipleIncomingTrans() {
        return incomingTransition >= 2;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean word) {
        isWord = word;
    }

    public static boolean compareTransitions(Node node1, Node node2) {
        Map<Character, Node> map1 = node1.transitionMap;
        Map<Character, Node> map2 = node2.transitionMap;

        if (map1.size() == map2.size()) {
            for (Map.Entry<Character, Node> keyValue : map1.entrySet()) {
                char current = keyValue.getKey();
                Node currentNode = keyValue.getValue();

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Node)) {
            return false;
        }

        Node that = (Node)obj;
        return (isWord == that.isWord && compareTransitions(this, that));
    }

    @Override
    public int hashCode() {
        if (hashCode == null) {
            int hash = 12;
            hash = 34 * hash + (this.isWord ? 1 : 0);
            hash = 34 * hash + (this.transitionMap != null ? transitionMap.hashCode() : 0);

            hashCode = hash;
            return hash;
        }
        else {
            return hashCode;
        }
    }
}
