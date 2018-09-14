package com.kumarsoumya;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Board {

    private Tile[][] tiles = new Tile[Constant.ranks][Constant.files];

    private List<Tile> tileObjs = new LinkedList<Tile>();

    public Board() {
        initializeTiles();
        setupBoard();
    }

    private Color getColor(int rank, int file) {
        int x = rank + file;
        return ((x & 1) == 0) ? Constant.boardColor[0] : Constant.boardColor[1];
    }

    private void initializeTiles() {
        for (int rank = 0; rank < Constant.ranks; rank++) {
            for (int file = 0; file < Constant.files; file++) {
                tileObjs.add(new Tile(getColor(rank, file)));
            }
        }
    }

    public void setupBoard() {

        Iterator<Tile> tileIterator = tileObjs.iterator();

        for (int rank = 0; rank < Constant.ranks; rank++) {
            for (int file = 0; file < Constant.files; file++) {
                tiles[rank][file] = tileIterator.next();
            }
        }
    }

    public Tile getTile(int rank, int file) {
        return tiles[rank][file];
    }

}