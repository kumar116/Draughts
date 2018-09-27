    package com.kumarsoumya;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Stack;

public class Logic {

    private String created = new SimpleDateFormat("MM-dd-yy HH:mm:ss").format(new Date());

    private Board board = new Board();

    private LinkedList<Point> diagonalLeft = new LinkedList<Point>();
    private LinkedList<Point> diagonalRight = new LinkedList<Point>();

    private static Point firstPos = null;

    private Player playerOne = new Player(Side.WHITE);
    private Player playerTwo = new Player(Side.BLACK);

    private LinkedList<LinkedList<Point>> undoMoves = new LinkedList<LinkedList<Point>>();
    private LinkedList<LinkedList<Point>> redoMoves = new LinkedList<LinkedList<Point>>();
    private Stack<Piece> removedPieces = new Stack<Piece>();

    public Logic() {
        playerOne.setTurn(true);
    }

    public Board getBoard() {
        return board;
    }

    public void clearSelection() {
        for (int rank = 0; rank < Constant.ranks; rank++) {
            for (int file = 0; file < Constant.files; file++) {
                board.getTile(rank, file).setSelected(false);
            }
        }
    }

    public void clearMoves() {
        firstPos = null;
        while (!diagonalLeft.isEmpty()) {
            diagonalLeft.remove();
        }
        while (!diagonalRight.isEmpty()) {
            diagonalRight.remove();
        }
    }

    public void clearTurns() {
        playerOne.setTurn(true);
        playerTwo.setTurn(false);
    }

    public LinkedList<LinkedList<Point>> getUndoMoves() {
        return undoMoves;
    }

    public void setUndoMoves(LinkedList<LinkedList<Point>> undoMoves) {
        this.undoMoves = undoMoves;
    }

    public LinkedList<LinkedList<Point>> getRedoMoves() {
        return redoMoves;
    }

    public void setRedoMoves(LinkedList<LinkedList<Point>> redoMoves) {
        this.redoMoves = redoMoves;
    }

    public Stack<Piece> getRemovedPieces() {
        return removedPieces;
    }

    public void setRemovedPieces(Stack<Piece> removedPieces) {
        this.removedPieces = removedPieces;
    }

    private void reverseTurns() {
        if (playerOne.isTurn()) {
            playerOne.setTurn(false);
            playerTwo.setTurn(true);
        } else if (playerTwo.isTurn()) {
            playerTwo.setTurn(false);
            playerOne.setTurn(true);
        }
    }

    public void selectMoves(int rank, int file) {
        Tile tile = board.getTile(rank, file);
        if (tile.isOccupied()) {
            if (playerOne.isTurn() && tile.getPiece().getSide() != playerOne.getSide()
                || playerTwo.isTurn() && tile.getPiece().getSide() != playerTwo.getSide()) {
                return;
            }
            selectMoves(rank, file, true);
            selectMoves(rank, file, false);
        }
    }

    private void selectMoves(int rank, int file, boolean direction) {
        Tile tile = board.getTile(rank, file);
        LinkedList<Point> diagonal = (direction) ? diagonalLeft : diagonalRight;

        diagonal.add(board.getPosition(rank, file));

        if (tile.isOccupied()) {
            if (firstPos == null) {
                firstPos = diagonal.getFirst();
            }
            Side originalSide = board.getTile(firstPos.x, firstPos.y).getPiece().getSide();

            if (diagonal.size() > 1) {
                Side currSide = tile.getPiece().getSide();
                if (currSide == originalSide) {
                    while (!diagonal.isEmpty()) {
                        diagonal.remove();
                    }
                    return;
                }
            }

            int selectRank = (originalSide == Side.BLACK) ? rank + 1 : (originalSide == Side.WHITE) ? rank - 1 : Constant.ranks;
            int selectFile = (direction) ? file - 1 : file + 1;
            if (selectRank >= 0 && selectRank < Constant.ranks && selectFile >= 0 && selectFile < Constant.files) {
                selectMoves(selectRank, selectFile, direction);
            }
        } else {
            tile.setSelected(true);
        }
    }

    public boolean canPlayMoveTo(int rank, int file) {
        return (canPlayMoveTo(rank, file, true) || canPlayMoveTo(rank, file, false));
    }

    private boolean canPlayMoveTo(int rank, int file, boolean direction) {
        LinkedList<Point> diagonal = (direction) ? diagonalLeft : diagonalRight;
        if (!diagonal.isEmpty()) {
            Point lastPos = diagonal.getLast();
            if (rank == lastPos.x && file == lastPos.y) {
                return true;
            }
        }
        return false;
    }

    public boolean playMoveTo(int rank, int file) {
        return (playMoveTo(rank, file, true) || playMoveTo(rank, file, false));
    }

    private boolean playMoveTo(int rank, int file, boolean direction) {
        LinkedList<Point> diagonal = (direction) ? diagonalLeft : diagonalRight;
        if (!diagonal.isEmpty()) {
            Point lastPos = diagonal.getLast();
            if (rank == lastPos.x && file == lastPos.y) {
                playMove(diagonal);
                return true;
            }
        }
        return false;
    }

    public void playMove(LinkedList<Point> moves) {
        playMove(moves, false, true);
    }

    @SuppressWarnings("unchecked")
    public void playMove(LinkedList<Point> moves, boolean reverse, boolean save) {
        if (save) {
            undoMoves.add((LinkedList<Point>) moves.clone());
        }

        reverseTurns();

        Point lastPos = reverse ? moves.getFirst() : moves.getLast();
        Tile lastTile = board.getTile(lastPos.x, lastPos.y);
        Point firstPos = reverse ? moves.getLast() : moves.getFirst();
        Tile firstTile = board.getTile(firstPos.x, firstPos.y);
        lastTile.setPiece(firstTile.getPiece());

        for (int i = 0; i < moves.size() - 1; i++) {
            int index = reverse ? moves.size() - i - 1: i;
            Point currPos = moves.get(index);
            Tile currTile = board.getTile(currPos.x, currPos.y);
            if (currTile != firstTile) {
                if (reverse) {
                    currTile.setPiece(removedPieces.pop());
                    continue;
                } else {
                    removedPieces.push(currTile.getPiece());
                }
            }
            currTile.setPiece(null);
        }
    }

    public void undoMoves() {
        if (!undoMoves.isEmpty()) {
            LinkedList<Point> lastMove = undoMoves.remove(undoMoves.size() - 1);
            redoMoves.add(lastMove);
            playMove(lastMove, true, false);
        }
    }

    public void redoMoves() {
        if (!redoMoves.isEmpty()) {
            LinkedList<Point> lastMove = redoMoves.remove(redoMoves.size() - 1);
            undoMoves.add(lastMove);
            playMove(lastMove);
        }
    }

    public int getRank(int y) {
        return  (y / (Constant.size.height / Constant.ranks));
    }

    public int getFile(int x) {
        return  (x / (Constant.size.width / Constant.files));
    }

    @Override
    public String toString() {
        return created;
    }

}
