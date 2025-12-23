package com.hedgehogkb.fighter.moves;

import com.hedgehogkb.fighter.Fighter;

public class FighterTimer {
    private Fighter fighter;
    private double countdown;

    public FighterTimer(Fighter fighter, double countdown) {
        this.countdown = countdown;
    }

    public void subtractCountDown(double deltaTime) {
        countdown -= deltaTime;
    }

    public boolean countdownOver() {
        return countdown <= 0;
    }

    public boolean containsFighter(Fighter fighter) {
        return this.fighter.equals(fighter);
    }
}
