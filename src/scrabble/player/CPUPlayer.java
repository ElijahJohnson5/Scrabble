/**
 * @author Elijah Johnson
 * @description the cpu player implementation
 * uses backtracking to generate all possible moves
 */

package scrabble.player;

import scrabble.*;
import scrabble.Dictionary;

import java.util.*;

public class CPUPlayer extends Player {
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
    private int toSubtractFromScore;

    /**
     * Constructor that takes tile manager, sets all
     * values to correct initial state
     * @param manager the manager for the tiles,
     *                handles drawing tiles
     */
    public CPUPlayer(TileManager manager) {
        super(manager);
        highestAnchorPos = null;
        highestScoringWord = null;
        legalMoves = new HashSet<>();
        tray.setTiles(manager.drawTray());
        System.out.println(tray);
        currentAnchor = null;
        toSubtractFromScore = 0;
        highestScoring = 0;
        currentMove = new ArrayList<>();
        highestMove = new ArrayList<>();
    }

    /**
     * Takes the turn for the cpu, uses backtracking to generate
     * every possible move
     * @param board the current board state
     * @param dict the dictionary to use for checking valid plays
     * @return
     */
    @Override
    public int takeTurn(Board board, Dictionary dict) {
        //TODO move this into a function
        legalMoves.clear();
        highestStartPos = null;
        highestAnchorPos = null;
        highestScoringWord = null;
        currentMove.clear();
        highestMove.clear();
        currentAnchor = null;
        currentStartPos = null;
        currentEndPos = null;
        highestEndPos = null;
        toSubtractFromScore = 0;
        highestScoring = 0;
        //Calculate all possible moves for the across plays
        calcMoves(board, dict);
        board.transpose();
        //Transpose the board and calculate all moves for across plays again.
        //Turn into down plays from transposing back
        calcMoves(board, dict);
        board.transpose();
        //No possible moves trade out hand
        if (legalMoves.size() == 0) {
            tray.setTiles(manager.redrawTray(tray.getTiles()));
            return 0;
        }
        //Debug printing
        System.out.println(highestMove);
        //Play the highestScoringWord
        List<Tile> moves = new ArrayList<>(highestMove);
        board.playWord(highestScoringWord, highestMove, highestStartPos, highestEndPos);
        //Remove tiles we played
        tray.removeAll(moves);
        //Redraw up to seven tiles if possible
        tray.redrawToSeven(manager);
        //Debug printing
        System.out.println(tray);
        System.out.println(legalMoves);
        System.out.println(highestScoring);
        System.out.println(highestScoringWord);
        System.out.println(highestAnchorPos);
        System.out.println(highestStartPos);
        System.out.println(highestEndPos);
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
        System.out.println(crossChecks);
        //Get all of the potential anchor squares
        Set<Position> anchors = board.getPotentialAnchorSquares();
        int count;
        int currentCol;
        for (Position p : anchors) {
            count = -1;
            currentCol = p.getCol() - 1;
            //Get how many tiles to the left of current anchor
            //p to be used in left part recursively
            while (currentCol >= 0
                    && !anchors.contains(new Position(p.getRow(), currentCol))
                    && board.isEmpty(p.getRow(), currentCol)) {
                count++;
                currentCol--;
            }
            //Can only play up to the amount on the tray to the left of anchor
            count = Math.min(tray.size(), count);
            //Copy into current anchor start and end
            currentAnchor = new Position(p);
            currentStartPos = new Position(p);
            currentEndPos = new Position(p);
            //Call backtracking algorithm
            leftPart("", dict.getRootNode(), count, p, board, crossChecks);
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
    private void leftPart(String partialWord, Node n, int limit,
                          Position anchor,
                          Board board,
                          Map<Integer, Set<Character>> crossChecks) {
        //Make sure we are still on the board
        if (currentStartPos.getCol() >= 0) {
            //Try to extend this word to the right from node n in dict
            //and the current anchor
            extendRight(partialWord, n, anchor, board, crossChecks);
            if (limit > 0) {
                //For each edge leaving n check if we can extend the word
                for (Map.Entry<Character, Node> entry :
                        n.getCharacterNodeMap().entrySet()) {
                    char key = entry.getKey();
                    //If we have the tile
                    if (tray.isInTray(key)) {
                        //Remove it but save the tile to be added back later
                        Tile t = tray.removeFromTray(key);
                        //Add to the current move we are building
                        currentMove.add(t);
                        if (t.isBlank()) {
                            //Make sure the score calculation is correct
                            //if we have a blank
                            toSubtractFromScore += manager.getTileValue(key);
                        }
                        //Get the correct start position for the word, based off of the limit
                        currentStartPos = new Position(currentStartPos.getRow(),
                                anchor.getCol() - limit);
                        //Recursively call the left part with limit - 1
                        //The partial word + the key. and the node
                        //transitioned to the next node
                        leftPart(partialWord + key, n.transition(key),
                                limit - 1, anchor, board, crossChecks);
                        //Reset the to subtractFromScore
                        if (t.isBlank()) {
                            toSubtractFromScore = 0;
                        }
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
    private void extendRight(String partialWord, Node n, Position square,
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
            for (Map.Entry<Character, Node> entry :
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
                        if (t.isBlank()) {
                            toSubtractFromScore += manager.getTileValue(key);
                        }
                        //Call recursively on the partialWord + key the node
                        // transitioned to the key and update the current
                        // we are on
                        extendRight(partialWord + key, n.transition(key),
                                new Position(square.getRow(),
                                        square.getCol() + 1),
                                board, crossChecks);
                        if (t.isBlank()) {
                            toSubtractFromScore = 0;
                        }
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
        //We dont have a highest word yet, make this word highest
        if (highestAnchorPos == null && highestScoringWord == null) {
            highestMove.clear();
            highestScoringWord = word;
            highestMove.addAll(currentMove);
            //Get all of the relavent posistions, based on if
            //the board is transposed or not
            getPositions(anchorPos, board);
            //Check if the end position makes sense for this word
            if (highestEndPos.getCol() !=
                    highestStartPos.getCol() + word.length() - 1) {
                //If it doesnt update the end position based on the word
                highestEndPos = new Position(highestStartPos.getRow(),
                        highestStartPos.getCol() + word.length() - 1);
            }
            //Get the value of this word if we played it
            highestScoring = board.getValue(word, currentMove, currentStartPos)
                    - toSubtractFromScore;
        }
        else {
            //Check if end pos makes sense
            if (currentEndPos.getCol() !=
                    currentStartPos.getCol() + word.length() - 1) {
                currentEndPos = new Position(currentEndPos.getRow(),
                        currentStartPos.getCol() + word.length() - 1);
            }
            //Get the score for this move minus any blanks
            int score = board.getValue(word, currentMove, currentStartPos) - toSubtractFromScore;
            //If the score is greater than our highest this is our new highest
            if (score > highestScoring) {
                //Update highest values
                highestMove.clear();
                getPositions(anchorPos, board);
                highestScoringWord = word;
                highestScoring = score;
                highestMove.addAll(currentMove);
            }
        }
        //Keep a set of all legal moves
        legalMoves.add(word);
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
