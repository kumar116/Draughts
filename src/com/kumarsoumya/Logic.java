    package com.kumarsoumya;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Logic {

    private Board board = new Board();

    private Stack<Point> diagonalLeft = new Stack<Point>();
    private Stack<Point> diagonalRight = new Stack<Point>();

    private static Point firstPos = null;

    private Player playerOne = new Player(Side.WHITE);
    private Player playerTwo = new Player(Side.BLACK);

    private List<Stack<Point>> undoMoves = new LinkedList<Stack<Point>>();
    private List<Stack<Point>> redoMoves = new LinkedList<Stack<Point>>();
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
        diagonalLeft.clear();
        diagonalRight.clear();
    }

    public void clearTurns() {
        playerOne.setTurn(true);
        playerTwo.setTurn(false);
    }

    public List<Stack<Point>> getUndoMoves() {
        return undoMoves;
    }

    public void setUndoMoves(List<Stack<Point>> undoMoves) {
        this.undoMoves = undoMoves;
    }

    public List<Stack<Point>> getRedoMoves() {
        return redoMoves;
    }

    public void setRedoMoves(List<Stack<Point>> redoMoves) {
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
        Stack<Point> diagonal = (direction) ? diagonalLeft : diagonalRight;

        diagonal.push(new Point(rank, file));

        if (tile.isOccupied()) {
            if (firstPos == null) {
                firstPos = diagonal.firstElement();
            }
            Side originalSide = board.getTile(firstPos.x, firstPos.y).getPiece().getSide();

            if (diagonal.size() > 1) {
                Side currSide = tile.getPiece().getSide();
                if (currSide == originalSide) {
                    diagonal.clear();
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
        Tile tile = board.getTile(rank, file);
        Stack<Point> diagonal = (direction) ? diagonalLeft : diagonalRight;
        if (!diagonal.isEmpty()) {
            Point lastPos = diagonal.peek();
            Tile lastTile = board.getTile(lastPos.x, lastPos.y);
            if (lastTile == tile) {
                return true;
            }
        }
        return false;
    }

    public boolean playMoveTo(int rank, int file) {
        return (playMoveTo(rank, file, true) || playMoveTo(rank, file, false));
    }

    private boolean playMoveTo(int rank, int file, boolean direction) {
        Tile tile = board.getTile(rank, file);
        Stack<Point> diagonal = (direction) ? diagonalLeft : diagonalRight;
        if (!diagonal.isEmpty()) {
            Point lastPos = diagonal.peek();
            Tile lastTile = board.getTile(lastPos.x, lastPos.y);
            if (lastTile == tile) {

                playMove(diagonal);

                return true;
            }
        }
        return false;
    }

    public void playMove(Stack<Point> moves) {
        playMove(moves, false, true);
    }

    public void playMove(Stack<Point> moves, boolean reverse, boolean save) {
        if (save) {
            Stack<Point> movesClone = new Stack<Point>();
            movesClone.addAll(moves);
            undoMoves.add(movesClone);
        }

        reverseTurns();
        Point lastPos = moves.pop();
        Tile lastTile = board.getTile(lastPos.x, lastPos.y);
        Point firstPos = moves.firstElement();
        Tile firstTile = board.getTile(firstPos.x, firstPos.y);
        lastTile.setPiece(firstTile.getPiece());

        while (!moves.isEmpty()) {
            Point currPos = moves.pop();
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
            Stack<Point> lastMove = undoMoves.remove(undoMoves.size() - 1);
            Stack<Point> lastMoveClone = new Stack<Point>();
            lastMoveClone.addAll(lastMove);
            redoMoves.add(lastMoveClone);
            Stack<Point> reverseMoves = new Stack<Point>();
            while (!lastMove.isEmpty()) {
                reverseMoves.push(lastMove.pop());
            }
            playMove(reverseMoves, true, false);
        }
    }

    public void redoMoves() {
        if (!redoMoves.isEmpty()) {
            Stack<Point> lastMove = redoMoves.remove(redoMoves.size() - 1);
            Stack<Point> lastMoveClone = new Stack<Point>();
            lastMoveClone.addAll(lastMove);
            undoMoves.add(lastMoveClone);
            playMove(lastMove);
        }
    }

    public int getRank(int y) {
        return  (y / (Constant.size.height / Constant.ranks));
    }

    public int getFile(int x) {
        return  (x / (Constant.size.width / Constant.files));
    }

}
