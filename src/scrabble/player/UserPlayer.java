package scrabble.player;

import javafx.scene.layout.HBox;
import scrabble.Board;
import scrabble.Dictionary;
import scrabble.TileManager;

import java.util.ArrayList;
import java.util.HashSet;

public class UserPlayer extends Player {


    public UserPlayer(TileManager manager, HBox hand) {
        super(manager, hand);
        tray.setTiles(manager.drawTray(7));
        tray.setDragAndDrop(hand);
        hand.getChildren().addAll(tray.getTileDisplay());
    }

    @Override
    public int takeTurn(Board board, Dictionary dict) {
        return 1;
    }
}
