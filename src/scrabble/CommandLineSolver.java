/**
 * @name Elijah Johnson
 * @description
 **/

package scrabble;

import scrabble.player.CPUPlayer;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineSolver {
    private CPUPlayer cpuPlayer;
    private Dictionary dict;
    private Board board;
    private TileManager manager;

    public CommandLineSolver(BufferedReader br) {
        dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.TRIE);
        dict.insert(br);
        board = new Board();
        cpuPlayer = null;
        manager = new TileManager();
        BufferedReader letters = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/default_letter_distributions.txt")));
        manager.initialize(letters);
        try {
            letters.close();
            br.close();
        } catch (IOException e) {
            System.out.println("Could not close files");
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CommandLineSolver [DICT FILE]");
            return;
        }
        CommandLineSolver commandLineSolver;
        try {
            commandLineSolver = new CommandLineSolver(new BufferedReader(new FileReader(new File(args[0]))));
        } catch (IOException e) {
            System.out.println("Could not read dictionary file");
            return;
        }
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            long start = System.currentTimeMillis();
            commandLineSolver.findBest(in);
            long end = System.currentTimeMillis();
            System.out.println("Time to find move: " + (end - start) + "ms");
        }
    }

    public void findBest(Scanner in) {
        board.initialize(in, manager);
        String lastLine = in.nextLine();
        lastLine = lastLine.toUpperCase();
        List<Tile> tray = new ArrayList<>();
        for (int i = 0; i < lastLine.length(); i++) {
            tray.add(new Tile(lastLine.charAt(i), manager.getTileValue(lastLine.charAt(i))));
        }
        cpuPlayer = new CPUPlayer(manager, tray);
        cpuPlayer.takeTurn(board, dict);
        System.out.println(board);
        System.out.println("Word score: " + cpuPlayer.getWordScore());
        System.out.println("Word played: " + cpuPlayer.getWordPlayed());
    }
}