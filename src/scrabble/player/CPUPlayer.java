/**
 * @author Elijah Johnson
 * @description the cpu player implementation
 * uses backtracking to generate all possible moves
 */

package scrabble.player;

import javafx.scene.layout.HBox;
import scrabble.*;
import scrabble.Dictionary;

import java.util.*;

public class CPUPlayer extends Player {
    /**
     * Member variables for keeping track of
     * the current move and the current highest move
     */
    private String highestScoringLeftOfAnchor;
    private String highestScoringWord;
    private int highestScoring;
    private Position highestAnchorPos;
    private Position highestStartPos;
    private Set<String> legalMoves;
    private List<Tile> currentMove;
    private List<Tile> highestMove;
    private Position currentAnchor;
    private Position currentStartPos;
    private Position currentEndPos;
    private Position highestEndPos;
    private String leftOfAnchor;

    /**
     * Constructor used for command line solver
     * Initialize with a tray
     * @param manager The tile manager handles drawing tiles
     * @param board the current board
     * @param tray the tray to initialize the computer tray to
     */
    public CPUPlayer(TileManager manager, Board board, List<Tile> tray) {
        super(manager, board, tray);
        legalMoves = new HashSet<>();
        currentMove = new ArrayList<>();
        highestMove = new ArrayList<>();
        hand = null;
        leftOfAnchor = null;
        resetValues();
    }

    /**
     * Constructor used for gui version
     * @param manager the Tile manager handles drawing tiles from bag
     * @param board the current board
     * @param hand the HBox representing the computer hand
     */
    public CPUPlayer(TileManager manager, Board board, HBox hand) {
        super(manager, board, hand);
        tray.setTiles(manager.drawTray(7));
        legalMoves = new HashSet<>();
        currentMove = new ArrayList<>();
        highestMove = new ArrayList<>();
        hand.getChildren().addAll(tray.getTileDisplay());
        resetValues();
    }

    /**
     * Reset all values to default values
     */
    private void resetValues() {
        legalMoves.clear();
        highestStartPos = null;
        highestAnchorPos = null;
        highestScoringWord = null;
        highestScoringLeftOfAnchor = null;
        leftOfAnchor = null;
        currentMove.clear();
        highestMove.clear();
        currentAnchor = null;
        currentStartPos = null;
        currentEndPos = null;
        highestEndPos = null;
        highestScoring = 0;
    }

    /**
     * Gets the last word played by this player
     * @return the string representing the last word played
     */
    @Override
    public String getLastWordPlayed() {
        if (highestScoringLeftOfAnchor != null) {
            return highestScoringLeftOfAnchor + highestScoringWord;
        } else {
            return highestScoringWord;
        }
    }

    /**
     * Gets the score of the last word played by this player
     * @return the last highestScoring move
     */
    @Override
    public int getLastWordPlayedScore() {
        return highestScoring;
    }

    /**
     * Takes the turn for the cpu, uses backtracking to generate
     * every possible move
     * @param dict the dictionary to use for checking valid plays
     * @return
     */
    @Override
    public int takeTurn(Dictionary dict) {
        resetValues();
        //Calculate all possible moves for the across plays
        calcMoves(board, dict);
        board.transpose();
        //Transpose the board and calculate all moves for across plays again.
        //Turn into down plays from transposing back
        calcMoves(board, dict);
        board.transpose();
        //No possible moves trade out hand
        if (legalMoves.size() == 0) {
            tray.setTiles(manager.redrawTray(tray.getTiles(), 7));
            return 0;
        }

        //Play the highestScoringWord
        List<Tile> moves = new ArrayList<>(highestMove);
        board.playWord(highestScoringWord, highestMove, highestStartPos, highestEndPos);
        //Remove tiles we played
        tray.removeAll(moves);
        if (hand != null) {
            hand.getChildren().clear();
        }
        //Redraw up to seven tiles if possible
        if (!manager.isEmpty()) {
            tray.redrawToSeven(manager);
        }
        if (hand != null) {
            hand.getChildren().addAll(tray.getTileDisplay());
        }

        //Debug printing
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(tray);
            System.out.println(legalMoves);
            System.out.println(highestScoringWord);
            System.out.println(highestScoring);
            System.out.println(highestAnchorPos);
            System.out.println(highestStartPos);
            System.out.println(highestEndPos);
            //Debug printing
            System.out.println(highestMove);
        }

        return 0;
    }

    /**
     * Calculates all valid across moves for a board
     * @param board the current board state
     * @param dict the dictionary containing all of the words
     */
    private void calcMoves(Board board, Dictionary dict) {
        //Generate cross checks for the current board
        Map<Integer, Set<Character>> crossChecks =
                board.generateCrossChecks(dict);
        //Empty board, every letter can go anywhere
        if (crossChecks == null) {
            crossChecks = new HashMap<>();
        }
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(crossChecks);
        }
        //Get all of the potential anchor squares
        Set<Position> anchors = board.getPotentialAnchorSquares();
        int count;
        int currentCol;
        for (Position p : anchors) {
            count = 0;
            currentCol = p.getCol() - 1;
            //Get how many tiles to the left of current anchor
            //p to be used in left part recursively
            while (currentCol >= 0
                    && !anchors.contains(new Position(p.getRow(), currentCol))) {
                count++;
                currentCol--;
            }
            //Can only play up to the amount on the tray to the left of anchor
            count = Math.min(tray.size(), count);
            //Copy into current anchor start and end
            currentAnchor = new Position(p);
            currentStartPos = new Position(p);
            currentEndPos = new Position(p);
            if (count > 0 && !board.isEmpty(p.getRow(), p.getCol() - 1)) {
                StringBuilder sb = new StringBuilder();
                currentCol = p.getCol() - 1;
                while (count > 0 && !board.isEmpty(p.getRow(), currentCol)) {
                    sb.insert(0, board.getTile(p.getRow(), currentCol).getCharacter());
                    count--;
                    currentCol--;
                }
                if (dict.getRootNode().transition(sb.toString()) != null) {
                    leftOfAnchor = sb.toString();
                    extendRight("", dict.getRootNode().transition(leftOfAnchor), p, board, crossChecks);
                }
            } else {
                //Call backtracking algorithm
                leftOfAnchor = null;
                leftPart("", dict.getRootNode(), count, p, board, crossChecks);
            }
        }
    }

    /**
     * Generates all possible left parts of words
     * to the left of the anchor tile, up to limit times
     * @param partialWord the word we are currently building, starts as ""
     * @param n the node we are currently on, starts as the root node of the
     *          dict
     * @param limit the amount of times we can extend left, can be any value
     *              up to tray.size()
     * @param anchor the current anchor tile we are generating moves for
     * @param board the current board state
     * @param crossChecks the crossChecks of the current board state
     */
    private void leftPart(String partialWord, DictNode n, int limit,
                          Position anchor,
                          Board board,
                          Map<Integer, Set<Character>> crossChecks) {
        //Make sure we are still on the board
        if (currentStartPos.getCol() >= 0) {
            //Try to extend this word to the right from node n in dict
            //and the current anchor
            //Only start right generation when it is appropriate
            extendRight(partialWord, n, anchor, board, crossChecks);
            if (limit > 0) {
                //For each edge leaving n check if we can extend the word
                for (Map.Entry<Character, DictNode> entry :
                        n.getCharacterNodeMap().entrySet()) {
                    char key = entry.getKey();
                    //If we have the tile
                    if (tray.isInTray(key)) {
                        //Remove it but save the tile to be added back later
                        Tile t = tray.removeFromTray(key);
                        //Add to the current move we are building
                        currentMove.add(t);
                        //Get the correct start position for the word, based off of the limit
                        currentStartPos = new Position(currentStartPos.getRow(),
                                currentStartPos.getCol() - 1);
                        //Recursively call the left part with limit - 1
                        //The partial word + the key. and the node
                        //transitioned to the next node
                        leftPart(partialWord + key, n.transition(key),
                                limit - 1, anchor, board, crossChecks);
                        currentStartPos = new Position(currentStartPos.getRow(),
                                currentStartPos.getCol() + 1);
                        //Remove from the current move
                        //and add back to the tray
                        currentMove.remove(t);
                        tray.addToTray(t);
                    }
                }
            }
        }
    }

    /**
     * Extends the partial word to the right
     * as much as possible
     * @param partialWord the current word we are building,
     *                    can be empty string or string
     *                    containing left parts
     * @param n the current node in the dictionary we are on
     * @param square the current position we are trying to extend to
     * @param board the current board state
     * @param crossChecks the crossChecks of the current board state
     */
    private void extendRight(String partialWord, DictNode n, Position square,
                             Board board,
                             Map<Integer, Set<Character>> crossChecks) {
        //Make sure we are on the board
        //if not check if we are at a word and return
        if (square.getCol() >= board.getSize()) {
            if (n.isWord()) {
                //Add to legal moves and check if we have new highest
                currentEndPos = new Position(square.getRow(),
                        square.getCol() - 1);
                legalMove(partialWord, currentAnchor, board);
            }
            return;
        }

        //If the current position is empty
        if (board.isEmpty(square.getRow(), square.getCol())) {
            //Check if we have a word
            if (n.isWord()) {
                //Add it to legal moves, check if we have new highest
                currentEndPos = new Position(square.getRow(),
                        square.getCol() - 1);
                legalMove(partialWord, currentAnchor, board);
            }
            //For every edge leaving n
            for (Map.Entry<Character, DictNode> entry :
                    n.getCharacterNodeMap().entrySet()) {
                char key = entry.getKey();
                boolean inCross = false;
                //Check if we have it in the tray
                if (tray.isInTray(key)) {
                    //Get the valid characters for the cross checks
                    Set<Character> crossSet =
                            crossChecks.get(square.getRow() * board.getSize()
                                    + square.getCol());
                    //If it is null anything can go here
                    if (crossSet == null) {
                        inCross = true;
                    } else {
                        //Check if the key is in the cross set
                        if (crossSet.contains(key)) {
                            inCross = true;
                        }
                    }
                    //If it is in cross set
                    if (inCross) {
                        //Save tile to be added back later
                        Tile t = tray.removeFromTray(key);
                        currentMove.add(t);
                        //Call recursively on the partialWord + key the node
                        // transitioned to the key and update the current
                        // we are on
                        extendRight(partialWord + key, n.transition(key),
                                new Position(square.getRow(),
                                        square.getCol() + 1),
                                board, crossChecks);
                        currentMove.remove(t);
                        tray.addToTray(t);
                    }
                }
            }
        }
        //The space is filled
        else {
            //If there is an edge from n that is the character in this square
            Tile t = board.getTile(square.getRow(), square.getCol());
            if (n.getCharacterNodeMap().containsKey(t.getCharacter())) {
                //Extend the word to the right
                extendRight(partialWord + t.getCharacter(),
                        n.transition(t.getCharacter()),
                        new Position(square.getRow(), square.getCol() + 1),
                        board, crossChecks);
            }
        }
    }

    /**
     * Add words to legal move and update highest
     * if it is a new highest word
     * @param word the word that is a legal move
     * @param anchorPos the current anchor position
     * @param board the current board state
     */
    private void legalMove(String word, Position anchorPos, Board board) {
        if (word.length() < 2) {
            return;
        }

        //Make sure the word is played off of an anchor tile
        int i;
        for (i = currentStartPos.getCol(); i <= currentEndPos.getCol(); i++) {
            if (anchorPos.getCol() == i && anchorPos.getRow() == currentStartPos.getRow()) {
                break;
            }
        }
        if (i > currentEndPos.getCol()) {
            return;
        }

        //We dont have a highest word yet, make this word highest
        if (highestScoring == 0) {
            highestMove.clear();
            highestScoringWord = word;
            highestMove.addAll(currentMove);
            highestScoringLeftOfAnchor = leftOfAnchor;
            //Get all of the relavent posistions, based on if
            //the board is transposed or not

            //Check if the end position makes sense for this word
            if (currentEndPos.getCol() !=
                    currentStartPos.getCol() + word.length() - 1) {
                currentEndPos = new Position(currentEndPos.getRow(),
                        currentStartPos.getCol() + word.length() - 1);
            }

            getPositions(anchorPos, board);
            //Get the value of this word if we played it
            highestScoring = board.getValue(word, currentMove, currentStartPos)
                    + (leftOfAnchor == null ? 0 : manager.getValue(leftOfAnchor));
        }
        else {
            //Check if end pos makes sense
            if (currentEndPos.getCol() !=
                    currentStartPos.getCol() + word.length() - 1) {
                currentEndPos = new Position(currentEndPos.getRow(),
                        currentStartPos.getCol() + word.length() - 1);
            }
            //Get the score for this move minus any blanks
            int score = board.getValue(word, currentMove, currentStartPos)
                    + (leftOfAnchor == null ? 0 : manager.getValue(leftOfAnchor));
            //If the score is greater than our highest this is our new highest
            if (score > highestScoring) {
                //Update highest values
                highestMove.clear();
                getPositions(anchorPos, board);
                highestScoringWord = word;
                highestScoring = score;
                highestMove.addAll(currentMove);
                highestScoringLeftOfAnchor = leftOfAnchor;
            }
        }
        //Keep a set of all legal moves
        if (leftOfAnchor != null) {
            legalMoves.add(leftOfAnchor + word);
        } else {
            legalMoves.add(word);
        }
    }

    /**
     * Gets the positions, and transposes if needed
     * @param anchorPos the current anchor position
     * @param board the current board state
     */
    private void getPositions(Position anchorPos, Board board) {
        //Position.transposed() creates new position that
        //is transposed of old
        if (board.isTransposed()) {
            //Transpose the positions if the board is
            highestEndPos = currentEndPos.transposed();
            highestAnchorPos = anchorPos.transposed();
            highestStartPos = currentStartPos.transposed();
        } else {
            //Create new positions
            highestStartPos = new Position(currentStartPos);
            highestEndPos = new Position(currentEndPos);
            highestAnchorPos = new Position(anchorPos);
        }
    }
}
