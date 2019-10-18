package com.kumarsoumya.checkers;

import java.awt.Color;

public class Tile {

    private boolean selected = false;

    private final Color color;

    private int width;
    private int height;

    private Piece piece = null;

    public Tile(Color color) {
        width =  Constant.size.width / Constant.files;
        height =  Constant.size.height / Constant.ranks;
        this.color = color;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Color getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isOccupied() {
        return piece != null;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

}
