package com.hedgehogkb.fighter.moves;

import java.util.HashMap;

import com.hedgehogkb.keybinds.InputType;


public class MoveHandler {
    private final HashMap<MoveType, Move> moves;
    private final HashMap<InputType, Boolean> holding;

    public MoveHandler(HashMap<MoveType, Move> moves) {
        this.moves = moves;
        holding = new HashMap<>();
        for (InputType inputType : InputType.values()) {
            holding.put(inputType, false);
        }
    }

    public Move getCurMove() {
        return moves.get(MoveType.STANDING);
    }

    public void setPressed(InputType inputType) {
        holding.put(inputType, true);
    }

    public void setReleased(InputType inputType) {
        holding.put(inputType, false);
    }
}
