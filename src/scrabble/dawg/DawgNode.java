package scrabble.dawg;

import scrabble.Node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DawgNode extends Node {
    private int incomingTransition;
    private Integer hashCode;

    public DawgNode(boolean isWord) {
        super();
        hashCode = null;
        this.isWord = isWord;
    }

    public DawgNode(DawgNode node) {
        super();
        this.isWord = node.isWord;
        characterNodeMap = new HashMap<>(node.characterNodeMap);
        hashCode = null;
        for (Node value : characterNodeMap.values()) {
            DawgNode v = (DawgNode) value;
            v.incomingTransition++;
        }
    }

    public int getIncomingTransition() {
        return incomingTransition;
    }

    public boolean hasTransition(char letter) {
        return characterNodeMap.containsKey(letter);
    }

    public DawgNode transition(char letter) {
        return (DawgNode)characterNodeMap.get(letter);
    }

    public DawgNode transition(String transStr) {
        DawgNode temp = this;

        for (int i = 0; i < transStr.length(); i++) {
            temp = temp.transition(transStr.charAt(i));
            if (temp == null) break;
        }

        return temp;
    }

    public DawgNode clone() {
        return new DawgNode(this);
    }

    public DawgNode clone(DawgNode parent, char transLabel) {
        DawgNode clone = new DawgNode(this);
        parent.changeTransition(transLabel, this, clone);

        return clone;
    }

    public List<DawgNode> getPathNodes(String transStr) {
        List<DawgNode> list = new LinkedList<>();
        DawgNode temp = this;

        for (int i = 0; i < transStr.length(); i++) {
            temp = temp.transition(transStr.charAt(i));
            list.add(temp);
            if (temp == null) break;
        }

        return list;
    }

    public void changeTransition(char letter, DawgNode oldNode, DawgNode newNode) {
        oldNode.incomingTransition--;
        newNode.incomingTransition++;

        characterNodeMap.put(letter, newNode);
    }

    public DawgNode addTransition(char letter, boolean isWord) {
        DawgNode newNode = new DawgNode(isWord);
        newNode.incomingTransition++;

        characterNodeMap.put(letter, newNode);

        return newNode;
    }

    public void clearHash() {
        hashCode = null;
    }

    public void removeTransition(char letter) {
        characterNodeMap.remove(letter);
    }

    public boolean hasTransitions() {
        return !characterNodeMap.isEmpty();
    }

    public int getTransitionCount() {
        return characterNodeMap.size();
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

    public static boolean compareTransitions(DawgNode node1, DawgNode node2) {
        Map<Character, Node> map1 = node1.characterNodeMap;
        Map<Character, Node> map2 = node2.characterNodeMap;

        if (map1.size() == map2.size()) {
            for (Map.Entry<Character, Node> keyValue : map1.entrySet()) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof DawgNode)) {
            return false;
        }

        DawgNode that = (DawgNode)obj;
        return (isWord == that.isWord && compareTransitions(this, that));
    }

    @Override
    public int hashCode() {
        if (hashCode == null) {
            int hash = 12;
            hash = 34 * hash + (this.isWord ? 1 : 0);
            hash = 34 * hash + (this.characterNodeMap != null ? characterNodeMap.hashCode() : 0);

            hashCode = hash;
            return hash;
        }
        else {
            return hashCode;
        }
    }
}
