package scrabble;
import java.io.File;

public interface DictionaryInterface {

    boolean search(String word);

    void insert(File dict);

    default Node getRootNode() {
        return null;
    }
}
