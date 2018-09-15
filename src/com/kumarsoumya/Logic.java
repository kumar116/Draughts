package com.kumarsoumya;

import java.awt.Point;
import java.util.Stack;

public class Logic {

    private Board board = new Board();

    private Stack<Point> diagonalLeft = new Stack<Point>();
    private Stack<Point> diagonalRight = new Stack<Point>();

    private static Point firstPos = null;

    public Logic() { }

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

    public void selectMoves(int rank, int file) {
        selectMoves(rank, file, true);
        selectMoves(rank, file, false);
    }

    public void selectMoves(int rank, int file, boolean direction) {
        Tile tile = board.getTile(rank, file);

        if (tile.isOccupied()) {
            Stack<Point> diagonal = (direction) ? diagonalLeft : diagonalRight;

            diagonal.push(new Point(rank, file));

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

    public int getRank(int y) {
        return  (y / (Constant.size.height / Constant.ranks));
    }

    public int getFile(int x) {
        return  (x / (Constant.size.width / Constant.files));
    }

}