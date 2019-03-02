/**
 * @author Elijah Johnson
 * @description the user player implementation extends the player class
 */

package scrabble.player;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import scrabble.*;
import scrabble.Dictionary;

import java.util.*;

public class UserPlayer extends Player {
    /**
     * Member variables for the current move and what
     * type of move the player is doing
     */
    private Map<Tile, Position> currentMove;
    private List<Position> currentPositions;
    private List<Position> otherPos;
    private boolean playMove;
    private boolean finishExchangeMove;
    private boolean exchanging;
    private String currentWord;
    private String lastWord;
    private int lastScore;
    private Position endPos;
    private Position startPos;

    /**
     * Constructor for gui version,
     * no console based user player
     * @param manager the Tilemanager handles drawing tiles and
     *                anything to do with tiles
     * @param board the current board/ a reference to the board
     * @param hand the HBox representing the display of the player hand
     */
    public UserPlayer(TileManager manager, Board board, HBox hand) {
        //Call player constructor
        super(manager, board, hand);
        //Initialize all values
        currentMove = new HashMap<>();
        currentPositions = new ArrayList<>();
        otherPos = new ArrayList<>();
        playMove = false;
        currentWord = null;
        startPos = null;
        endPos = null;
        finishExchangeMove = false;
        exchanging = false;
        tray.setTiles(manager.drawTray(7));
        //Display the tiles
        hand.getChildren().addAll(tray.getTileDisplay());
    }

    /**
     * Takes the user turn, returns 1 when a move hasnt been played and
     * 0 otherwise
     * @param dict the dictionary to use for checking valid plays
     * @return 1 if the player hasnt taken a turn yet otherwise 0
     */
    @Override
    public int takeTurn(Dictionary dict) {
        //If the tray isnt set up for drag and drop
        //Set it up
        if (!tray.isDragAndDrop()) {
            tray.setDragAndDrop(hand,
                    (GridPane)hand.getParent().getChildrenUnmodifiable().get(1),
                    this::dropCallback,
                    this::returnedCallback);
        }

        if (playMove) {
            //Play the word
            List<Tile> moves = new ArrayList<>(currentMove.keySet());
            board.playWord(currentWord, new ArrayList<>(currentMove.keySet()), startPos, endPos);
            playMove = false;
            //Remove used tiles
            tray.removeAll(moves);
            hand.getChildren().clear();
            //Draw as many as possible
            tray.redrawToSeven(manager);
            //Make sure all tiles are drag and droppable
            tray.setDragAndDrop(hand,
                    (GridPane)hand.getParent().getChildrenUnmodifiable().get(1),
                    this::dropCallback,
                    this::returnedCallback);
            hand.getChildren().addAll(tray.getTileDisplay());
            //Reset values
            startPos = null;
            endPos = null;
            lastWord = currentWord;
            currentWord = null;
            currentPositions.clear();
            currentMove.clear();
            return 0;
        } else if (finishExchangeMove) {
            //They exchanged tiles this turn instead of playing a word
            finishExchangeMove = false;
            //Update drag and drop
            tray.setDragAndDrop(hand,
                    (GridPane)hand.getParent().getChildrenUnmodifiable().get(1),
                    this::dropCallback,
                    this::returnedCallback);
            currentPositions.clear();
            currentMove.clear();
            startPos = null;
            endPos = null;
            return 0;
        }
        return 1;
    }

    /**
     * The last word played by the user
     * @return the last word member variable
     */
    @Override
    public String getLastWordPlayed() {
        return lastWord;
    }

    /**
     * Return the last played score
     * for the word that was last played
     * @return the last score member variable
     */
    @Override
    public int getLastWordPlayedScore() {
        return lastScore;
    }

    /**
     * Attempts to play a move
     * @param mouseEvent the mouse event that triggers this,
     *                   should be a button press
     * @param dict the representation of the dictionary
     *             used for checking valid moves
     */
    public void attemptPlayMove(MouseEvent mouseEvent, Dictionary dict) {
        Button source = (Button)mouseEvent.getSource();
        //If they are not exchanging tiles try to play the move
        if (source.getId().equals("playMove") && !exchanging) {
            //Check if any tiles are out
            if (currentMove.size() > 0) {
                //Check if it is a legal move
                if (legalMove(dict)) {
                    playMove = true;
                }
            } else {
                //Show feed back
                createAndShowAlert(Alert.AlertType.INFORMATION,
                        "Invalid Move",
                        "That is not a valid move",
                        "");
            }
        } else {
            //They finished exchanging all the tiles they wanted to
            finishExchangeMove = true;
        }

        mouseEvent.consume();
    }

    /**
     * Exchange a tile with a random tile from the bag
     * @param mouseDragEvent the drag event that triggered this function
     *                       call
     */
    public void exchangeTile(MouseDragEvent mouseDragEvent) {
        //They are exchanging tiles
        exchanging = true;
        //Get the gesture source, will always be a pane
        Pane p = (Pane)mouseDragEvent.getGestureSource();
        board.resetPlaced();
        //Get the board node
        GridPane board =
                (GridPane)hand.getParent().getChildrenUnmodifiable().get(1);
        //Tray should always contain the tile whose pane is p
        if (tray.contains(p)) {
            //Get the tile from the pane
            Tile t = tray.getTile(p);
            //If it was on the board we need to fire an event to the board
            if (p.getParent().equals(board))  {
                DropEvent dropEvent = new DropEvent(null, t, true);
                //Fire the dropEvent (really a reset)
                board.fireEvent(dropEvent);
                //Remove it from the currentMove list
                returnedCallback(t);
                //Reset translates
                p.setTranslateX(0);
                p.setTranslateY(0);
            }
            //Remove all children from the hand HBox
            hand.getChildren().clear();
            //Replace the tile t with a new one from manager
            tray.replace(manager, t);
            hand.getChildren().addAll(tray.getTileDisplay());
        }

        mouseDragEvent.consume();
    }

    /**
     * Resets a tile to the players hand
     * @param mouseDragEvent the event that was fired
     */
    public void resetTile(MouseDragEvent mouseDragEvent) {
        //Get the pane and board
        Pane p = (Pane)mouseDragEvent.getGestureSource();
        GridPane board =
                (GridPane)hand.getParent().getChildrenUnmodifiable().get(1);
        if (tray.contains(p)) {
            //Get the tile
            Tile t = tray.getTile(p);
            //Only need to do anything if the parent of p is the board
            if (p.getParent().equals(board))  {
                DropEvent dropEvent = new DropEvent(null, t, true);
                //Fire board event
                board.fireEvent(dropEvent);
                returnedCallback(t);
                p.setTranslateX(0);
                p.setTranslateY(0);
                //Add p to the children of hand
                hand.getChildren().add(p);
            }
        }

        mouseDragEvent.consume();
    }

    /**
     * Convenience function for showing alerts
     * @param type the type of alert
     * @param title the title of the alert
     * @param header the header of the alert
     * @param content the content of the alert
     */
    private void createAndShowAlert(Alert.AlertType type,
                                    String title,
                                    String header,
                                    String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        //Create and show alert
        alert.showAndWait();
    }

    /**
     * Checks if the current move stored in the user class
     * is a legal move
     * @param dict the dictionary used for searching for words
     * @return true if it is a valid word otherwise false
     */
    private boolean legalMove(Dictionary dict) {
        currentPositions = new ArrayList<>(currentMove.values());
        //Sort the positions from top left to bottom right
        Collections.sort(currentPositions);
        //The first pos should be starting position
        startPos = currentPositions.get(0);
        //Last pos should be ending position
        endPos = currentPositions.get(currentPositions.size() - 1);
        //Get the direction of the play
        Position.Direction dir = startPos.getDirection(endPos);
        //If the direction is both and the play is not one character long
        //it is invalid
        if (dir == Position.Direction.BOTH && !startPos.equals(endPos)) {
            createAndShowAlert(Alert.AlertType.INFORMATION,
                    "Invalid Move",
                    "That is not a valid move",
                    "");
            return false;
        }
        //Check if we need to add any positions to the current positions
        if (dir == Position.Direction.BOTH) {
            if (checkForPrefix(startPos, Position.Direction.ACROSS) != 0) {
                dir = Position.Direction.ACROSS;
                checkForSuffix(startPos, dir);
                //Empty if statement but check for suffix has side effects
            } else if (checkForSuffix(startPos, Position.Direction.ACROSS) != 0) {

            } else if (checkForPrefix(startPos, Position.Direction.DOWN) != 0) {
                dir = Position.Direction.DOWN;
                checkForSuffix(startPos, dir);
            } else {
                checkForSuffix(startPos, Position.Direction.DOWN);
            }
        } else {
            checkForPrefix(startPos, dir);
            checkForSuffix(endPos, dir);
            checkForInfix(startPos, endPos, dir);
        }
        //Make sure positions are still sorted
        Collections.sort(currentPositions);
        //Get anchor positions
        Set<Position> anchors = board.getPotentialAnchorSquares();
        boolean hasAnchorSomewhere = false;
        Position prev = null;
        //Check that there is an anchor somewhere in the play
        //And they are not trying to play diagonally
        for (Position p : currentPositions) {
            if (anchors.contains(p)) {
                hasAnchorSomewhere = true;
            }
            if (prev != null) {
                //Trying to play in more than one direction
                if (p.distance(prev) > 1) {
                    currentPositions.removeAll(otherPos);
                    otherPos.clear();
                    createAndShowAlert(Alert.AlertType.INFORMATION,
                            "Invalid Move",
                            "That is not a valid move",
                            "Move must be all down or all across");
                    return false;
                }
            }
            prev = p;
        }

        //They are not playing off of another tile or the middle
        if (!hasAnchorSomewhere) {
            currentPositions.removeAll(otherPos);
            otherPos.clear();
            createAndShowAlert(Alert.AlertType.INFORMATION,
                    "Invalid Move",
                    "That is not a valid move",
                    "Move must start from another tile");
            return false;
        }

        //Get start position, could have changed from checkForSuffix
        //or checkForPrefix or checkForInfix
        startPos = currentPositions.get(0);
        endPos = currentPositions.get(currentPositions.size() - 1);
        //Get the direction of the play, should only be across or
        //down by this point
        dir = startPos.getDirection(endPos);
        currentWord = "";
        Map<Integer, Set<Character>> crossChecks;
        StringBuilder sb = new StringBuilder();
        switch (dir) {
            case ACROSS:
                //Get the cross checks for the board
                crossChecks = board.generateCrossChecks(dict);
                for (int i = startPos.getCol(); i <= endPos.getCol(); i++) {
                    //Check that the letter at [startPos.getRow][i] on the board is valid
                    Set<Character> cross =
                            crossChecks.get(startPos.getRow() * board.getSize() + i);
                    if (cross != null &&
                            !cross.contains(board.getTile(startPos.getRow(), i)
                                    .getCharacter())) {
                        currentPositions.removeAll(otherPos);
                        otherPos.clear();
                        //Down words made are not valid
                        createAndShowAlert(Alert.AlertType.INFORMATION,
                                "Invalid Move",
                                "That is not a valid move",
                                "All sub words are not a word");
                        return false;
                    }
                    //Build up the current word
                    sb.append(board.getTile(startPos.getRow(), i).getCharacter());
                }
                //Get the score of the word
                lastScore = board.getValue(sb.toString(),
                        new ArrayList<>(currentMove.keySet()), startPos);
                break;
            case DOWN:
                //Transpose board for down moves
                board.transpose();
                //Same as above just with row and col switched because
                //the board is transposed
                crossChecks = board.generateCrossChecks(dict);
                for (int i = startPos.getRow(); i <= endPos.getRow(); i++) {
                    Set<Character> cross =
                            crossChecks.get(startPos.getCol() * board.getSize() + i);
                    if (cross != null &&
                            !cross.contains(board.getTile(startPos.getCol(), i)
                                    .getCharacter())) {
                        currentPositions.removeAll(otherPos);
                        otherPos.clear();
                        createAndShowAlert(Alert.AlertType.INFORMATION,
                                "Invalid Move",
                                "That is not a valid move",
                                "All sub words are not a word");
                        return false;
                    }
                    sb.append(board.getTile(startPos.getCol(), i)
                            .getCharacter());
                }
                lastScore = board.getValue(sb.toString(),
                        new ArrayList<>(currentMove.keySet()),
                        startPos.transposed());
                //Make sure board is reset back to normal
                board.transpose();
                break;
        }
        //Get the current word
        currentWord = sb.toString();
        //Check that the word is in the dictionary
        if (!dict.search(currentWord)) {
            currentPositions.removeAll(otherPos);
            otherPos.clear();
            lastScore = -1;
            //If not reset values and give feed back
            createAndShowAlert(Alert.AlertType.INFORMATION,
                    "Invalid Word",
                    "That is not a valid word",
                    "The word you are trying to play isn't in the dictionary");
            return false;
        }
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(currentWord);
            System.out.println(currentPositions);
        }
        //Clear placeholder list
        otherPos.clear();
        return true;
    }

    /**
     * Checks to see if any tiles that were already on the board
     * needs to be added to current positions
     * @param startPos the current start position of the word
     * @param endPos the current end position of the word
     * @param dir the direction the word goes
     */
    private void checkForInfix(Position startPos,
                               Position endPos,
                               Position.Direction dir) {
        switch (dir) {
            case ACROSS:
                for (int i = startPos.getCol(); i <= endPos.getCol(); i++) {
                    //Loop through all positions between start and end
                    //and add any positions that arent in current positions
                    // already
                    if (!board.isEmpty(startPos.getRow(), i)) {
                        otherPos.add(new Position(startPos.getRow(), i));
                        currentPositions.add(
                                new Position(startPos.getRow(), i));
                    }
                }
                break;
            case DOWN:
                for (int i = startPos.getRow(); i <= endPos.getRow(); i++) {
                    if (!board.isEmpty(i, startPos.getCol())) {
                        otherPos.add(new Position(i, startPos.getCol()));
                        currentPositions.add(
                                new Position(i, startPos.getCol()));
                    }
                }
                break;
        }
    }

    /**
     * Checks if any positions after endPos that were already on the board
     * need to be added
     * @param endPos the current endPosition of the word
     * @param dir the direction the word is goign, can be across or down
     * @return the amount of positions added
     */
    private int checkForSuffix(Position endPos, Position.Direction dir) {
        Position current;
        int returnVal = currentPositions.size();
        switch (dir) {
            case ACROSS:
                //Loop until we hit an empty spot or the end of the
                // board to the right
                current = new Position(endPos.getRow(),
                        endPos.getCol() + 1);
                while (current.getCol() < board.getSize()
                        && !board.isEmpty(current.getRow(), current.getCol())) {
                    otherPos.add(
                            new Position(current.getRow(), current.getCol()));
                    currentPositions.add(
                            new Position(current.getRow(), current.getCol()));
                    current.incrementCol();
                }
                break;
            case DOWN:
                //Loop until we hit an empty spot or the end of the board
                //on the bottom
                current = new Position(endPos.getRow() + 1, endPos.getCol());
                while (current.getRow() < board.getSize()
                        && !board.isEmpty(current.getRow(), current.getCol())) {
                    otherPos.add(
                            new Position(current.getRow(), current.getCol()));
                    currentPositions.add(
                            new Position(current.getRow(), current.getCol()));
                    current.incrementRow();
                }
                break;
        }
        //Return the amount of items added
        return (Math.abs(returnVal - currentPositions.size()));
    }

    /**
     * Checks if an positions before start position need to be
     * added to current positions
     * @param start the current start position
     * @param dir the direction the word is going in can be across or down
     * @return the amount of positions added
     */
    private int checkForPrefix(Position start, Position.Direction dir) {
        Position current;
        int returnVal = currentPositions.size();
        switch (dir) {
            case ACROSS:
                current = new Position(start.getRow(), start.getCol() - 1);
                while (current.getCol() >= 0
                        && !board.isEmpty(current.getRow(), current.getCol())) {
                    otherPos.add(
                            new Position(current.getRow(), current.getCol()));
                    currentPositions.add(
                            new Position(current.getRow(), current.getCol()));
                    current.decrementCol();
                }
                break;
            case DOWN:
                current = new Position(start.getRow() - 1, start.getCol());
                while (current.getRow() >= 0
                        && !board.isEmpty(current.getRow(), current.getCol())) {
                    otherPos.add(
                            new Position(current.getRow(), current.getCol()));
                    currentPositions.add(
                            new Position(current.getRow(), current.getCol()));
                    current.decrementRow();
                }
                break;
        }
        return (Math.abs(returnVal - currentPositions.size()));
    }

    /**
     * Callback for if a tile gets returned to the hand from the board
     * @param returned the tile that got returned
     */
    private void returnedCallback(Tile returned) {
        //Remove it from the map
        currentMove.remove(returned);
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(currentPositions);
            System.out.println(currentMove);
        }
    }

    /**
     * Callback for if a tile gets dropped onto the board
     * @param dropped the tile that was dropped on the board
     * @param pos the position the tile was dropped on the board
     */
    private void dropCallback(Tile dropped, Position pos) {
        //Make sure it is a valid position
        if (!currentMove.containsValue(pos)
                && pos.getCol() >= 0 && pos.getCol() < board.getSize()
                && pos.getRow() >= 0 && pos.getRow() < board.getSize()) {
            currentMove.put(dropped, pos);
        }
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(currentPositions);
            System.out.println(currentMove);
        }
    }
}
