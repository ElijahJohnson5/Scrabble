package scrabble;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File dictionaryFile = new File("./resources/dict.txt");
        DictionaryInterface dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.TRIE);
        dict.insert(dictionaryFile);
        System.out.println(dict.search("AA"));
    }
}

