package scrabble.player;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import scrabble.*;
import scrabble.Dictionary;

import java.util.*;

public class UserPlayer extends Player {
    private Map<Tile, Position> currentMove;
    private List<Position> currentPositions;
    private List<Position> otherPos;
    private boolean playMove;
    private String currentWord;
    private String lastWord;
    private int lastScore;
    private Position endPos;
    private Position startPos;

    public UserPlayer(TileManager manager, Board board, HBox hand) {
        super(manager, board, hand);
        currentMove = new HashMap<>();
        currentPositions = new ArrayList<>();
        otherPos = new ArrayList<>();
        playMove = false;
        currentWord = null;
        startPos = null;
        endPos = null;
        tray.setTiles(manager.drawTray(7));
        hand.getChildren().addAll(tray.getTileDisplay());
    }

    @Override
    public int takeTurn(Dictionary dict) {
        if (!tray.isDragAndDrop()) {
            tray.setDragAndDrop(hand,
                    (GridPane)hand.getParent().getChildrenUnmodifiable().get(1),
                    this::dropCallback,
                    this::returnedCallback);
        }

        if (playMove) {
            List<Tile> moves = new ArrayList<>(currentMove.keySet());
            board.playWord(currentWord, new ArrayList<>(currentMove.keySet()), startPos, endPos);
            playMove = false;
            tray.removeAll(moves);
            hand.getChildren().clear();
            tray.redrawToSeven(manager);
            tray.setDragAndDrop(hand,
                    (GridPane)hand.getParent().getChildrenUnmodifiable().get(1),
                    this::dropCallback,
                    this::returnedCallback);
            hand.getChildren().addAll(tray.getTileDisplay());
            startPos = null;
            endPos = null;
            lastWord = currentWord;
            currentWord = null;
            currentPositions.clear();
            currentMove.clear();
            return 0;
        }
        return 1;
    }

    @Override
    public String getLastWordPlayed() {
        return lastWord;
    }

    @Override
    public int getLastWordPlayedScore() {
        return lastScore;
    }

    public void attemptPlayMove(Dictionary dict) {
        if (currentPositions.size() > 0) {
            if (legalMove(dict)) {
                playMove = true;
            }
        }
    }

    private boolean legalMove(Dictionary dict) {
        currentPositions = new ArrayList<>(currentMove.values());
        Collections.sort(currentPositions);
        startPos = currentPositions.get(0);
        endPos = currentPositions.get(currentPositions.size() - 1);
        Position.Direction dir = startPos.getDirection(endPos);
        if (dir == Position.Direction.BOTH && !startPos.equals(endPos)) {
            return false;
        }
        if (dir == Position.Direction.BOTH) {
            if (checkForPrefix(startPos, Position.Direction.ACROSS) != 0) {
                dir = Position.Direction.ACROSS;
                checkForSuffix(startPos, dir);
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
        Collections.sort(currentPositions);
        Set<Position> anchors = board.getPotentialAnchorSquares();
        boolean hasAnchorSomewhere = false;
        Position prev = null;
        for (Position p : currentPositions) {
            if (anchors.contains(p)) {
                hasAnchorSomewhere = true;
            }
            if (prev != null) {
                if (p.distance(prev) > 1) {
                    currentPositions.removeAll(otherPos);
                    otherPos.clear();
                    return false;
                }
            }
            prev = p;
        }

        if (!hasAnchorSomewhere) {
            currentPositions.removeAll(otherPos);
            otherPos.clear();
            return false;
        }

        startPos = currentPositions.get(0);
        endPos = currentPositions.get(currentPositions.size() - 1);
        dir = startPos.getDirection(endPos);
        currentWord = "";
        Map<Integer, Set<Character>> crossChecks;
        StringBuilder sb = new StringBuilder();
        switch (dir) {
            case ACROSS:
                crossChecks = board.generateCrossChecks(dict);
                for (int i = startPos.getCol(); i <= endPos.getCol(); i++) {
                    Set<Character> cross = crossChecks.get(startPos.getRow() * board.getSize() + i);
                    if (cross != null && !cross.contains(board.getTile(startPos.getRow(), i).getCharacter())) {
                        currentPositions.removeAll(otherPos);
                        otherPos.clear();
                        return false;
                    }
                    sb.append(board.getTile(startPos.getRow(), i).getCharacter());
                    lastScore = board.getValue(sb.toString(), new ArrayList<>(currentMove.keySet()), startPos);
                }
                break;
            case DOWN:
                board.transpose();
                crossChecks = board.generateCrossChecks(dict);
                for (int i = startPos.getRow(); i <= endPos.getRow(); i++) {
                    Set<Character> cross = crossChecks.get(startPos.getCol() * board.getSize() + i);
                    if (cross != null && !cross.contains(board.getTile(startPos.getCol(), i).getCharacter())) {
                        currentPositions.removeAll(otherPos);
                        otherPos.clear();
                        return false;
                    }
                    sb.append(board.getTile(startPos.getCol(), i).getCharacter());
                }
                lastScore = board.getValue(sb.toString(), new ArrayList<>(currentMove.keySet()), startPos.transposed());
                board.transpose();
                break;
        }
        currentWord = sb.toString();
        if (!dict.search(currentWord)) {
            currentPositions.removeAll(otherPos);
            otherPos.clear();
            lastScore = -1;
            return false;
        }
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(currentWord);
            System.out.println(currentPositions);
        }
        otherPos.clear();
        return true;
    }

    private void checkForInfix(Position startPos, Position endPos, Position.Direction dir) {
        switch (dir) {
            case ACROSS:
                for (int i = startPos.getCol(); i <= endPos.getCol(); i++) {
                    if (!board.isEmpty(startPos.getRow(), i)) {
                        otherPos.add(new Position(startPos.getRow(), i));
                        currentPositions.add(new Position(startPos.getRow(), i));
                    }
                }
                break;
            case DOWN:
                for (int i = startPos.getRow(); i <= endPos.getRow(); i++) {
                    if (!board.isEmpty(i, startPos.getCol())) {
                        otherPos.add(new Position(i, startPos.getCol()));
                        currentPositions.add(new Position(i, startPos.getCol()));
                    }
                }
                break;
        }
    }

    private int checkForSuffix(Position endPos, Position.Direction dir) {
        Position current;
        int returnVal = currentPositions.size();
        switch (dir) {
            case ACROSS:
                current = new Position(endPos.getRow(), endPos.getCol() + 1);
                while (current.getCol() < board.getSize() && !board.isEmpty(current.getRow(), current.getCol())) {
                    otherPos.add(new Position(current.getRow(), current.getCol()));
                    currentPositions.add(new Position(current.getRow(), current.getCol()));
                    current.incrementCol();
                }
                break;
            case DOWN:
                current = new Position(endPos.getRow() + 1, endPos.getCol());
                while (current.getRow() < board.getSize() && !board.isEmpty(current.getRow(), current.getCol())) {
                    otherPos.add(new Position(current.getRow(), current.getCol()));
                    currentPositions.add(new Position(current.getRow(), current.getCol()));
                    current.incrementRow();
                }
                break;
        }

        return (Math.abs(returnVal - currentPositions.size()));
    }

    private int checkForPrefix(Position start, Position.Direction dir) {
        Position current;
        int returnVal = currentPositions.size();
        switch (dir) {
            case ACROSS:
                current = new Position(start.getRow(), start.getCol() - 1);
                while (current.getCol() >= 0 && !board.isEmpty(current.getRow(), current.getCol())) {
                    otherPos.add(new Position(current.getRow(), current.getCol()));
                    currentPositions.add(new Position(current.getRow(), current.getCol()));
                    current.decrementCol();
                }
                break;
            case DOWN:
                current = new Position(start.getRow() - 1, start.getCol());
                while (current.getRow() >= 0 && !board.isEmpty(current.getRow(), current.getCol())) {
                    otherPos.add(new Position(current.getRow(), current.getCol()));
                    currentPositions.add(new Position(current.getRow(), current.getCol()));
                    current.decrementRow();
                }
                break;
        }
        return (Math.abs(returnVal - currentPositions.size()));
    }


    private void returnedCallback(Tile returned) {
        currentMove.remove(returned);
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(currentPositions);
            System.out.println(currentMove);
        }
    }

    private void dropCallback(Tile dropped, Position pos) {
        currentMove.put(dropped, pos);
        if (ScrabbleGui.DEBUG_PRINT) {
            System.out.println(currentPositions);
            System.out.println(currentMove);
        }
    }
}
