/**
 * @name Elijah Johnson
 * @description
 **/

package scrabble;

import scrabble.player.CPUPlayer;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineSolver {
    private CPUPlayer cpuPlayer;
    private Dictionary dict;
    private Board board;
    private TileManager manager;

    public CommandLineSolver(File dictFile) {
        dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.TRIE);
        dict.insert(dictFile);
        board = new Board();
        cpuPlayer = null;
        manager = new TileManager();
        System.out.println(getClass().getResource("../default_letter_distributions.txt").getPath());
        manager.initialize(new File(getClass().getResource("../default_letter_distributions.txt").getPath()));
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CommandLineSolver [DICT FILE]");
            return;
        }
        CommandLineSolver commandLineSolver = new CommandLineSolver(new File(args[0]));
        System.out.println(commandLineSolver.findBest());
    }

    public String findBest() {
        Scanner in = new Scanner(System.in);
        board.initialize(in, manager);
        String lastLine = in.nextLine();
        lastLine = lastLine.toUpperCase();
        List<Tile> tray = new ArrayList<>();
        for (int i = 0; i < lastLine.length(); i++) {
            tray.add(new Tile(lastLine.charAt(i), manager.getTileValue(lastLine.charAt(i))));
        }
        cpuPlayer = new CPUPlayer(manager, tray);
        cpuPlayer.takeTurn(board, dict);
        return cpuPlayer.getWordPlayed();
    }
}