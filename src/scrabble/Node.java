package scrabble;

import java.util.HashMap;
import java.util.Map;

abstract public class Node {
    protected Map<Character, Node> characterNodeMap;
    protected boolean isWord;

    public Node() {
        characterNodeMap = new HashMap<>();
        isWord = false;
    }

    abstract public Node transition(char c);

    abstract public Node transition(String trans);

    public Map<Character, Node> getCharacterNodeMap() {
        return characterNodeMap;
    }

    public void setWord(boolean word) {
        isWord = word;
    }

    public boolean isWord() {
        return isWord;
    }
}
