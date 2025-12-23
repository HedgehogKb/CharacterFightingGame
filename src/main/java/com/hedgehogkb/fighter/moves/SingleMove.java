package com.hedgehogkb.fighter.moves;

public class SingleMove implements Move{
    /**
     * The type of move i.e.
     */
    private final MoveType MOVE_TYPE;

    /**
     * The length that the move lasts before the player can get out of it.
     */
    private final double DURATION;

    public SingleMove(MoveType moveType, double duration) {
        this.MOVE_TYPE = moveType;
        this.DURATION = duration;
    }

    public MoveType getMoveType() {
        return this.MOVE_TYPE;
    }

    public double getDuration() {
        return this.DURATION;
    }

    @Override
    public void startMove() {
       //does nothing
    }
}
