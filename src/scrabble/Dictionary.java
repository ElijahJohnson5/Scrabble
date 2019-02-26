package scrabble;

import java.io.BufferedReader;

public abstract class Dictionary {
    protected DictNode root;

    abstract public boolean search(String word);

    abstract public void insert(BufferedReader br);

    public DictNode getRootNode() {
        return root;
    }
}
