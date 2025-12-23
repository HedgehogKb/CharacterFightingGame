package com.hedgehogkb.fighter.moves;

public interface Move {
    /**
     * Signals to the 
     * @return
     */
    public void startMove();


    public double getDuration();

    public MoveType getMoveType();
}
