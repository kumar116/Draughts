package com.kumarsoumya.checker;

import java.awt.Color;
import java.io.Serializable;

enum Side { WHITE, BLACK };

public class Piece implements Serializable {

    private static final long serialVersionUID = 1L;

    private int width, height;

    private final Side side;
    private final Color color;

    public Piece(Side side) {
        this.side = side;
        switch (side) {
            case BLACK:
                color = Constant.pieceColor[1];
                break;
            default:
            case WHITE:
                color = Constant.pieceColor[0];
                break;
        }
        width =  Constant.size.width / Constant.files;
        height =  Constant.size.height / Constant.ranks;
    }

    public Color getColor() {
        return this.color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Side getSide() {
        return side;
    }

}