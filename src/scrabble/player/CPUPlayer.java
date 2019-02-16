package scrabble.player;

import scrabble.*;
import scrabble.Dictionary;

import java.util.*;

public class CPUPlayer extends Player {
    private String highestScoring;
    private Position highestAnchorPos;
    private Position highestStartPos;
    private Set<String> legalMoves;
    private Position currentAnchor;
    private Position currentStartPos;
    private int toSubtractFromScore;

    public CPUPlayer(TileManager manager) {
        super(manager);
        highestAnchorPos = null;
        highestScoring = null;
        legalMoves = new HashSet<>();
        tray.setTiles(manager.drawTray());
        currentAnchor = null;
        toSubtractFromScore = 0;
    }

    @Override
    public int takeTurn(Board board, Dictionary dict) {
        calcMoves(board, dict);
        board.transpose();
        calcMoves(board, dict);
        board.transpose();
        System.out.println(legalMoves);
        System.out.println(highestScoring);
        System.out.println(highestAnchorPos);
        System.out.println(highestStartPos);
        return 0;
    }

    private void calcMoves(Board board, Dictionary dict) {
        Map<Integer, Set<Character>> crossChecks = board.generateCrossChecks(dict);
        System.out.println(crossChecks);
        Set<Position> anchors = board.getPotentialAnchorSquares();
        int count;
        int currentCol;
        for (Position p : anchors) {
            count = 0;
            currentCol = p.getCol() - 1;
            while (currentCol >= 0 && !anchors.contains(new Position(p.getRow(), currentCol)) && board.isEmpty(p.getRow(), currentCol)) {
                count++;
                currentCol--;
            }
            currentAnchor = new Position(p);
            currentStartPos = new Position(p);
            leftPart("", dict.getRootNode(), count, p, board, crossChecks);
        }
    }

    private void leftPart(String partialWord, Node n, int limit, Position anchor, Board board, Map<Integer, Set<Character>> crossChecks) {
        if (currentStartPos.getCol() >= 0) {
            extendRight(partialWord, n, anchor, board, crossChecks);
            if (limit > 0) {
                for (Map.Entry<Character, Node> entry : n.getCharacterNodeMap().entrySet()) {
                    char key = entry.getKey();
                    if (tray.isInTray(key)) {
                        Tile t = tray.removeFromTray(key);
                        if (t.isBlank()) {
                            toSubtractFromScore -= manager.getTileValue(key);
                        }
                        currentStartPos = new Position(currentStartPos.getRow(), currentStartPos.getCol() - 1);
                        leftPart(partialWord + key, n.transition(key), limit - 1, anchor, board, crossChecks);
                        if (t.isBlank()) {
                            toSubtractFromScore = 0;
                        }
                        tray.addToTray(t);
                    }
                }
            }
        }
    }

    private void extendRight(String partialWord, Node n, Position square, Board board, Map<Integer, Set<Character>> crossChecks) {
        if (square.getCol() >= board.getSize()) {
            if (n.isWord()) {
                legalMove(partialWord, currentAnchor, board);
            }
            return;
        }

        if (board.isEmpty(square.getRow(), square.getCol())) {
            if (n.isWord()) {
                legalMove(partialWord, currentAnchor, board);
            }
            for (Map.Entry<Character, Node> entry : n.getCharacterNodeMap().entrySet()) {
                char key = entry.getKey();
                boolean inCross = false;
                if (tray.isInTray(key)) {
                    Set<Character> crossSet = crossChecks.get(square.getRow() * board.getSize() + square.getCol());
                    if (crossSet == null) {
                        inCross = true;
                    } else {
                        if (crossSet.contains(key)) {
                            inCross = true;
                        }
                    }

                    if (inCross) {
                        Tile t = tray.removeFromTray(key);
                        if (t.isBlank()) {
                            toSubtractFromScore -= manager.getTileValue(key);
                        }
                        extendRight(partialWord + key, n.transition(key), new Position(square.getRow(), square.getCol() + 1), board, crossChecks);
                        if (t.isBlank()) {
                            toSubtractFromScore = 0;
                        }
                        tray.addToTray(t);
                    }
                }
            }
        }
        else {
            Tile t = board.getTile(square.getRow(), square.getCol());
            if (n.getCharacterNodeMap().containsKey(t.getCharacter())) {
                extendRight(partialWord + t.getCharacter(), n.transition(t.getCharacter()), new Position(square.getRow(), square.getCol() + 1), board, crossChecks);
            }
        }
    }

    private void legalMove(String word, Position anchorPos, Board board) {
        if (highestAnchorPos == null && highestScoring == null) {
            highestScoring = word;
            highestStartPos = currentStartPos;
            if (board.isTransposed()) {
                anchorPos.transpose();
            }
            highestAnchorPos = new Position(anchorPos);
        }
        else {
            int score = manager.getValue(word) + board.getScoreFromPos(anchorPos) - toSubtractFromScore;
            int score2 = manager.getValue(highestScoring) + board.getScoreFromPos(highestAnchorPos);
            if (score > score2) {
                if (board.isTransposed()) {
                    anchorPos.transpose();
                    currentStartPos.transpose();
                }
                highestStartPos = currentStartPos;
                highestScoring = word;
                highestAnchorPos = new Position(anchorPos);
            }
        }
        legalMoves.add(word);
    }
}
