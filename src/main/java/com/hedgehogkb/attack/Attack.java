package com.hedgehogkb.attack;

import java.util.ArrayList;

import com.hedgehogkb.fighter.Fighter;

public class Attack {
    private final double REHIT_DURAITON; //the amount of time before the attack can affect a fighter again.
    
    private ArrayList<FighterTimer> hitFighters;

    public Attack(double rehitDuration) {
        REHIT_DURAITON = rehitDuration;
    }

    public void markFighter(Fighter fighter) {
        hitFighters.add(new FighterTimer(fighter, REHIT_DURAITON));
    }

    public void advanceTimer(double deltaTime) {
        for (int i = hitFighters.size() -1; i >= 0; i++) {
            hitFighters.get(i).subtractCountDown(deltaTime);
            if (hitFighters.get(i).countdownOver()) {
                hitFighters.remove(i);
            }
        }
    }

    /**
     * Returns if a fighter has been hit by the attack and the rehit duration
     * hasn't ended.
     * @param fighter
     */
    public boolean isMarked(Fighter fighter) {
        for (FighterTimer timer : hitFighters) {
            if (timer.containsFighter(fighter)) return true;
        }
        return false;
    }

    private class FighterTimer {
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
}
