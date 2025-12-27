package com.hedgehogkb.fighter.moves;

import java.util.ArrayList;
import java.util.HashMap;

import com.hedgehogkb.fighter.Fighter;

public class MultiAttack implements Attack {
    public MoveType moveType;
    private double[] durations;
    private int curDurationIndex;

    private final double[] REHIT_DURATIONS;
    private HashMap<Fighter, Double> hitfighters;

    public MultiAttack(MoveType moveType, double[] durations, double[] rehitDuration) {
        this.moveType = moveType;
        this.durations = durations;
        this.curDurationIndex = -1;

        this.REHIT_DURATIONS = rehitDuration;
        hitfighters = new HashMap<>();
    }

    @Override
    public void startMove() {
        curDurationIndex++;
        if (curDurationIndex > durations.length) curDurationIndex = 0;

        hitfighters.clear();
    }

    @Override
    public void markFighter(Fighter fighter) {
        hitfighters.put(fighter, getDuration());
    }

    @Override
    public void advanceTimers(double deltaTime) {
        for (Fighter key : hitfighters.keySet()) {
            double duration = hitfighters.get(key) - deltaTime;
            hitfighters.put(key, duration);
            if (duration <= 0) hitfighters.remove(key);
        }
    }

    @Override
    public boolean isMarked(Fighter fighter) {
        return hitfighters.containsKey(fighter);
    }

    @Override
    public double getDuration() {
        return durations[curDurationIndex];
    }
    public double[] getDurations() {
        return this.durations;
    }

    @Override
    public MoveType getMoveType() {
        return this.moveType;
    }

    public double[] getRehitDurations() {
        return REHIT_DURATIONS;
    }
}
