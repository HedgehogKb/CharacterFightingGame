package com.hedgehogkb.fighter.moves;

import java.util.ArrayList;
import java.util.HashMap;

import com.hedgehogkb.fighter.Fighter;

public class SingleAttack implements Attack{
    public MoveType moveType;
    public double duration;

    /**
     * The amount of time before the attack can affect a fighter again.
     */
    private final double REHIT_DURAITON; 
    
    /**
     * An arrayList storing hit fighters
     */
    private HashMap<Fighter, Double> hitFighters; //TODO: change this to a hashmap where fighter is key and coundown is value

    public SingleAttack(MoveType moveType, double duration, double rehitDuration) {
        this.moveType = moveType;
        this.duration = duration;
        REHIT_DURAITON = rehitDuration;

        hitFighters = new HashMap<>();
    }

    @Override
    public void markFighter(Fighter fighter) {
        hitFighters.put(fighter, REHIT_DURAITON);
    }

    @Override
    public void advanceTimers(double deltaTime) {
        for (Fighter key : hitFighters.keySet()) {
            double duration = hitFighters.get(key) - deltaTime;
            hitFighters.put(key, duration);
            if (duration <= 0) hitFighters.remove(key);  
        }
    }

    /**
     * Returns if a fighter has been hit by the attack and the rehit duration
     * hasn't ended.
     * @param fighter
     */
    @Override
    public boolean isMarked(Fighter fighter) {
        return hitFighters.containsKey(fighter);
    }

    @Override
    public void startMove() {
       hitFighters.clear();
    }

    @Override
    public double getDuration() {
        return this.duration;
    }

    @Override
    public MoveType getMoveType() {
       return this.moveType;
    }

    public double getRehitDuration() {
        return this.REHIT_DURAITON;
    }
}
