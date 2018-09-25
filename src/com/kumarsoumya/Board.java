package com.kumarsoumya;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Board {

    private Tile[][] tiles = new Tile[Constant.ranks][Constant.files];
    private Point[][] positions = new Point[Constant.ranks][Constant.files];

    private List<Tile> tileObjs = new LinkedList<Tile>();

    private List<Piece> pieceObjs = new LinkedList<Piece>();

    public Board() {
        initializeTiles();
        initializePieces();
        initializePositions();
        setupBoard();
    }

    private Color getColor(int rank, int file) {
        int x = rank + file;
        return ((x & 1) == 0) ? Constant.boardColor[0] : Constant.boardColor[1];
    }

    private void initializePieces() {
        int count = (Constant.ranks * (Constant.ranks - 2)) / 2;
        for (int i = 0; i < count / 2; i++) {
            pieceObjs.add(new Piece(Side.BLACK));
        }
        for (int i = count / 2; i < count; i++) {
            pieceObjs.add(new Piece(Side.WHITE));
        }
    }

    private void initializeTiles() {
        for (int rank = 0; rank < Constant.ranks; rank++) {
            for (int file = 0; file < Constant.files; file++) {
                tileObjs.add(new Tile(getColor(rank, file)));
            }
        }
    }

    private void initializePositions() {
        for (int rank = 0; rank < Constant.ranks; rank++) {
            for (int file = 0; file < Constant.files; file++) {
                positions[rank][file] = new Point(rank, file);
            }
        }
    }

    public void setupBoard() {

        Iterator<Tile> tileIterator = tileObjs.iterator();
        Iterator<Piece> pieceIterator = pieceObjs.iterator();

        for (int rank = 0; rank < Constant.ranks; rank++) {
            for (int file = 0; file < Constant.files; file++) {
                Tile tile = tileIterator.next();
                tile.removePiece();
                if (rank != (Constant.ranks / 2) && rank != (Constant.ranks / 2 - 1)) {
                    if (tile.getColor() == Color.GREEN) {
                        tile.setPiece(pieceIterator.next());
                    }
                }
                tiles[rank][file] = tile;
            }
        }
    }

    public Tile getTile(int rank, int file) {
        return tiles[rank][file];
    }

    public Point getPosition(int rank, int file) {
        return positions[rank][file];
    }

}