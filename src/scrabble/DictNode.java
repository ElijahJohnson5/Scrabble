package scrabble;

import java.util.HashMap;
import java.util.Map;

abstract public class DictNode {
    protected Map<Character, DictNode> characterNodeMap;
    protected boolean isWord;

    public DictNode() {
        characterNodeMap = new HashMap<>();
        isWord = false;
    }

    abstract public DictNode transition(char c);

    abstract public DictNode transition(String trans);

    public Map<Character, DictNode> getCharacterNodeMap() {
        return characterNodeMap;
    }

    public void setWord(boolean word) {
        isWord = word;
    }

    public boolean isWord() {
        return isWord;
    }
}
