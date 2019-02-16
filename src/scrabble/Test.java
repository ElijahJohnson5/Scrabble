package scrabble;

import scrabble.player.CPUPlayer;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File dictionaryFile = new File("./resources/sowpods.txt");
        Dictionary dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.TRIE);
        dict.insert(dictionaryFile);
        System.out.println(dict.search("AE"));

        TileManager tileManager = new TileManager();
        tileManager.initialize(new File("./resources/default_letter_distributions.txt"));
        Board board = new Board();
        board.initialize(new File("./resources/default_board.txt"), tileManager);
        CPUPlayer player = new CPUPlayer(tileManager);

        long start = System.nanoTime();
        player.takeTurn(board, dict);
        long end = System.nanoTime();
        System.out.println("Time for computer to take turn " + (end - start) / 1000000);
    }
}

