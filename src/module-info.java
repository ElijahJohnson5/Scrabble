module Scrabble {
    requires javafx.fxml;
    requires javafx.controls;

    exports scrabble.trie;
    exports scrabble.dawg;
    exports scrabble.player;
    exports scrabble;
}