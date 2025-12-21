package com.hedgehogkb.fighter.moves;

import java.util.HashMap;


public class MoveHandler {
    private final HashMap<MoveType, Move> moves;

    private boolean holdingUp;
    private boolean holdingDown;
    private boolean holdingRight;
    private boolean holdingLeft;

    private boolean holdingNormal;
    private boolean holdingSpecial;

    private boolean holdingSprint;

    public MoveHandler(HashMap<MoveType, Move> moves) {
        this.moves = moves;
    }

    public Move getCurMove() {
        return moves.get(MoveType.STANDING);
    }
}
