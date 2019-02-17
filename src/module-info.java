module Scrabble {
    requires javafx.fxml;
    requires javafx.controls;

    opens scrabble to javafx.fxml;

    exports scrabble.trie;
    exports scrabble.dawg;
    exports scrabble.player;
    exports scrabble;
}