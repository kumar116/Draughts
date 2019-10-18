package com.kumarsoumya.checkers;

public class Player {

    private boolean turn = false;
    private Side side;

    public Player(Side side) {
        this.side = side;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isTurn() {
        return turn;
    }

    public Side getSide() {
        return side;
    }

}