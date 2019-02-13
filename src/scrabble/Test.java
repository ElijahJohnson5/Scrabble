package scrabble;

import scrabble.dawg.Dawg;
import scrabble.trie.Trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Test {
    public static void main(String[] args) throws Exception {
        //Thread.sleep(10000);
        Trie trie = new Trie();
        BufferedReader br = new BufferedReader(new FileReader("./resources/dict.txt"));
        String word;
        long start = System.nanoTime();
        while ((word = br.readLine()) != null) {
            trie.insert(word);
        }
        long endTime = System.nanoTime();
        System.out.println("Time to create trie " + (endTime - start) / 1000000);
        //System.out.println(trie.getAcceptedStates());
        //System.out.println(trie.getNonAcceptedStates());
        //System.out.println(trie.toString());
        start = System.nanoTime();
        br = new BufferedReader(new FileReader("./resources/dict.txt"));
        int count = 0;
        while ((word = br.readLine()) != null) {
            if (trie.search(word)) {
                count++;
            }
        }
        System.out.println("Number of words in trie " + count);
        endTime = System.nanoTime();
        System.out.println("Time to search " + count + " words in trie " + (endTime - start) / 1000000);
        trie.getPossibleWords("ABCDEFG");
        //System.out.println(trie.search("AAA"));

        start = System.nanoTime();
        Dawg dawg = new Dawg(new File("./resources/dict.txt"));
        endTime = System.nanoTime();
        System.out.println("Time to create dawg " + (endTime - start) / 1000000);
        start = System.nanoTime();
        br = new BufferedReader(new FileReader("./resources/dict.txt"));
        count = 0;
        while ((word = br.readLine()) != null) {
            if (dawg.search(word)) {
                count++;
            }
        }

        System.out.println("Number of words in dawg " + count);
        endTime = System.nanoTime();
        System.out.println("Time to search " + count + " words in dawg " + (endTime - start) / 1000000);
        System.out.println(dawg.search("ZYMOID"));

    }
}

