package scrabble;

import scrabble.dawg.Dawg;
import scrabble.trie.Trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Test {
    public static void main(String[] args) throws Exception {
        Thread.sleep(10000);
        Trie trie = new Trie();
        BufferedReader br = new BufferedReader(new FileReader("./resources/dict.txt"));
        String word;
        long start = System.nanoTime();
        while ((word = br.readLine()) != null) {
            trie.insert(word);
        }
        long endTime = System.nanoTime();
        System.out.println((endTime - start) / 1000000);
        //System.out.println(trie.getAcceptedStates());
        //System.out.println(trie.getNonAcceptedStates());
        //System.out.println(trie.toString());
        start = System.nanoTime();
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        trie.search("ZYMOID");
        endTime = System.nanoTime();
        System.out.println((endTime - start));
        //System.out.println(trie.search("AAA"));

        start = System.nanoTime();
        Dawg dawg = new Dawg(new File("./resources/dict.txt"));
        endTime = System.nanoTime();
        System.out.println((endTime - start) / 1000000);
        System.out.println(dawg.search("ZYMOID"));

    }
}

