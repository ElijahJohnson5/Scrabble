package scrabble;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File dictionaryFile = new File("./resources/dict.txt");
        DictionaryInterface dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.DAWG);
        dict.insert(dictionaryFile);
        System.out.println(dict.search("AAA"));


        TileManager tileManager = new TileManager();
        tileManager.initialize(new File("./resources/default_letter_distributions.txt"));
        Board board = new Board();
        board.initialize(new File("./resources/default_board.txt"), tileManager);
        System.out.println(board.toString());
    }
}

