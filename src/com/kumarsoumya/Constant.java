package com.kumarsoumya.checkers;

import java.awt.Color;
import java.awt.Dimension;

public class Constant {

    private Constant() { }

    public static int ranks = 8, files = 8;
    public static Dimension size = new Dimension(400, 400);
    public static Color[] boardColor = {Color.GREEN, Color.GRAY},
                          pieceColor = {Color.WHITE, Color.BLACK};

}