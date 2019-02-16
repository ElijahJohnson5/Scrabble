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
        board.initialize(new File("./resources/test_board.txt"), tileManager);
        System.out.println(board.toString());
        System.out.println(board.getPotentialAnchorSquares());
        board.generateCrossChecks(dict);
        CPUPlayer player = new CPUPlayer(tileManager);
        player.takeTurn(board, dict);
    }
}

