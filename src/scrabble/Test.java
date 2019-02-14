package scrabble;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File dictionaryFile = new File("./resources/dict.txt");
        DictionaryInterface dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.DAWG);
        dict.insert(dictionaryFile);
        Board board = new Board();
        board.initialize(new File("./resources/default_board.txt"));
        System.out.println(board.toString());
    }
}

