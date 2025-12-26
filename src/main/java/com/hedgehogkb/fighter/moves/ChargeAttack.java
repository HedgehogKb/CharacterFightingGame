package com.hedgehogkb.fighter.moves;

import java.util.HashMap;
import java.util.function.Function;

import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.keybinds.InputType;

public class ChargeAttack implements Attack{
    private MoveType moveType;

    private SingleAttack resultAttack;
    private double chargeTime;
    private boolean resultAttackStarted;
    private Function<Double, Double> damageMultiplier;

    private final double CHARGE_DURATION;

    public ChargeAttack(MoveType moveType, double duration, double rehitDuration, double chargeDuration, Function<Double, Double> damageMultiplier) {
        this.moveType = moveType;
        resultAttack = new SingleAttack(moveType, duration, rehitDuration);
        this.CHARGE_DURATION = chargeDuration;
        this.damageMultiplier = damageMultiplier;
    }

    /**
     * returns the result of a function that uses charge time to calculate the damage multiplier.
     * Often, this function will just be an if else i.e. a stepped function.
     * @return
     */
    public double getDamageMultiplier() {
        return damageMultiplier.apply(chargeTime);
    }

    @Override
    public void startMove() {
        resultAttackStarted = false;
        chargeTime = 0;
    }

    public void endCharge() {
        resultAttackStarted = true;
        resultAttack.startMove();
    }

    public boolean charging() {
        return !resultAttackStarted;
    }

    @Override
    public double getDuration() {
        if (!resultAttackStarted) return this.CHARGE_DURATION;
        return resultAttack.getDuration();
    }

    @Override
    public MoveType getMoveType() {
        return this.moveType;
    }

    @Override
    public void markFighter(Fighter fighter) {
        resultAttack.markFighter(fighter);
    }

    @Override
    public void advanceTimers(double deltaTime) {
        if (!resultAttackStarted) {
            if (chargeTime >= CHARGE_DURATION) resultAttackStarted = true;
            chargeTime += deltaTime;
            return;
        } 

        resultAttack.advanceTimers(deltaTime);
    }

    @Override
    public boolean isMarked(Fighter fighter) {
        if (!resultAttackStarted) return false;
        return resultAttack.isMarked(fighter);
    }
    
}
