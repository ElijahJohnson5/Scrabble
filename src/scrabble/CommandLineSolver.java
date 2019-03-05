/**
 * @name Elijah Johnson
 * @description Plays the best word based on a board state
 * and a hand
 **/

package scrabble;

import scrabble.player.CPUPlayer;
import scrabble.player.Player;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineSolver {
    private Player cpuPlayer;
    private Dictionary dict;
    private Board board;
    private TileManager manager;
    private final static String LETTER_DIS
            = "/default_letter_distributions.txt";

    /**
     * Create a command line solver
     * @param br the buffered reader representing the
     *           dictionary
     */
    public CommandLineSolver(BufferedReader br) {
        System.out.println("Initializing dictionary");
        dict = DictionaryFactory.createDict(DictionaryFactory.DictionaryType.DAWG);
        dict.insert(br);
        board = new Board();
        cpuPlayer = null;
        manager = new TileManager();
        //Read letters from file
        BufferedReader letters = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream(LETTER_DIS)));
        manager.initialize(letters);
        try {
            letters.close();
            br.close();
        } catch (IOException e) {
            System.out.println("Could not close files");
        }
    }

    /**
     * Main method, runs the command line solver
     * @param args the command line args, need to have at least one
     *             for the dictionary file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CommandLineSolver [DICT FILE]");
            return;
        }
        CommandLineSolver commandLineSolver;
        try {
            //Read the dict file
            commandLineSolver = new CommandLineSolver(
                    new BufferedReader(
                            new FileReader(new File(args[0]))));
        } catch (IOException e) {
            System.out.println("Could not read dictionary file");
            return;
        }
        //Create new scanner
        Scanner in = new Scanner(System.in);
        //Read until EOF
        while (in.hasNext()) {
            System.out.println("Enter board and tray");
            long start = System.currentTimeMillis();
            //Find the best move
            commandLineSolver.findBest(in);
            long end = System.currentTimeMillis();
            System.out.println("Time to find move: " + (end - start) + "ms");
        }
    }

    /**
     * Finds the best move based on a board state and
     * tray
     * @param in where to read the board from and the tray from
     */
    private void findBest(Scanner in) {
        //Initialize the board based on stdin
        board.initialize(in, manager);
        String lastLine = in.nextLine();
        lastLine = lastLine.toUpperCase();
        List<Tile> tray = new ArrayList<>();
        //Read tray characters
        for (int i = 0; i < lastLine.length(); i++) {
            tray.add(new Tile(lastLine.charAt(i),
                    manager.getTileValue(lastLine.charAt(i))));
        }
        //Create cpu player
        cpuPlayer = new CPUPlayer(manager, board, tray);
        //Make the cpu take its turn
        cpuPlayer.takeTurn(dict);
        System.out.println(board);
        //print out the board word played and score received
        System.out.println("Word score: " +
                cpuPlayer.getLastWordPlayedScore());
        System.out.println("Word played: " +
                cpuPlayer.getLastWordPlayed());
    }
}