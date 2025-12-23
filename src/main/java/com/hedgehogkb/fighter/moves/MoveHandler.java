package com.hedgehogkb.fighter.moves;

import java.util.HashMap;

import com.hedgehogkb.keybinds.InputType;


public class MoveHandler {
    private Move curMove;
    private double moveTimer;
    private final HashMap<MoveType, Move> moves;
    private final HashMap<InputType, Boolean> holding;

    private boolean normalReset;
    private boolean specialReset;

    public MoveHandler(HashMap<MoveType, Move> moves) {
        curMove = moves.get(MoveType.STANDING);
        this.moves = moves;
        holding = new HashMap<>();
        for (InputType inputType : InputType.values()) {
            holding.put(inputType, false);
        }

        this.normalReset = true;
        this.specialReset = true;
    }

    public Move getCurMove(double deltaTime, double groundedCooldown, double maxGroundedCooldown, int jumps, int maxJumps, double stunCooldown) {
        Move newMove = resolveCurMove(deltaTime, groundedCooldown, maxGroundedCooldown, jumps, maxJumps, stunCooldown);
        if (curMove != newMove) {
            newMove.startMove();
            moveTimer = 0;
        }
        curMove = newMove;

         //these actually aren't held, so it makes sure you need to click it again to use them.
        holding.put(InputType.NORMAL, false);
        holding.put(InputType.SPECIAL, false);
        return curMove;
    }

    public Move resolveCurMove(double deltaTime, double groundedCooldown, double maxGroundedCooldown, int jumps, int maxJumps, double stunCooldown) {
        if (stunCooldown > 0) {
            return moves.get(MoveType.STUNNED);
        }
        moveTimer += deltaTime; //may need to move this to end...

        // If already attacking then return the current move
        if (currentlyAttacking() && moveTimer <= curMove.getDuration()) {
            //System.out.println("attacking again: " + moveTimer);
            return curMove;
        }

        boolean inAir = groundedCooldown < maxGroundedCooldown;

        MoveType newAttack = resolveAttack(inAir);
        if (newAttack != null) return moves.get(newAttack);

        if (holding.get(InputType.FORWARD) && !holding.get(InputType.BACKWARD) ||
            holding.get(InputType.BACKWARD) && !holding.get(InputType.FORWARD)) {
            if (!inAir) {
                if (holding.get(InputType.SPRINT)) return moves.get(MoveType.SPRINTING);
                return moves.get(MoveType.WALKING);
            }
        }

        boolean canJump = jumps <= maxJumps;

        if (curMove.getMoveType() == MoveType.JUMPING) {
            if (moveTimer <= curMove.getDuration()) {
                return curMove;
            }
        }

        if (canJump && holding.get(InputType.UP)) {
            return moves.get(MoveType.JUMPING);
        }

        if (inAir) return moves.get(MoveType.FALLING);

        return moves.get(MoveType.STANDING);
    }

    private MoveType resolveAttack(boolean inAir) {
        if (holding.get(InputType.NORMAL) && normalReset) {
            normalReset = false;
            
            if (inAir) {
                if (holding.get(InputType.FORWARD)) return MoveType.FAIR_ATTACK;
                if (holding.get(InputType.BACKWARD)) return MoveType.NAIR_ATTACK; // optional
                if (holding.get(InputType.UP)) return MoveType.UAIR_ATTACK;
                if (holding.get(InputType.DOWN)) return MoveType.DAIR_ATTACK;

                return MoveType.NAIR_ATTACK;
            } else {
                if (holding.get(InputType.FORWARD)) return MoveType.FORWARD_ATTACK;
                if (holding.get(InputType.UP)) return MoveType.UP_ATTACK;
                if (holding.get(InputType.DOWN)) return MoveType.DOWN_ATTACK;

                return MoveType.NORMAL_ATTACK;
            }
        }

        if (holding.get(InputType.SPECIAL) && specialReset) {
            specialReset = false;

            if (holding.get(InputType.FORWARD) ||
                holding.get(InputType.BACKWARD)) return MoveType.FORWARD_SPECIAL;
            if (holding.get(InputType.UP)) return MoveType.UP_SPECIAL;
            if (holding.get(InputType.DOWN)) return MoveType.DOWN_SPECIAL;

            return MoveType.NORMAL_SPECIAL;
        }

        return null;
    }

    public boolean currentlyAttacking() {
        return curMove instanceof Attack;
    }

    public void setPressed(InputType inputType) {
        holding.put(inputType, true);
    }

    public void setReleased(InputType inputType) {
        holding.put(inputType, false);
        if (inputType == InputType.NORMAL) {
            normalReset = true;
        } else if (inputType == InputType.SPECIAL) {
            specialReset = true;
        }
    }
}
