package com.hedgehogkb.effects;

import com.hedgehogkb.fighter.Fighter;

public abstract class Effect {
    private final Fighter fighter;
    private final double DURATION; //in seconds
    private double elapsedTime;

    public Effect(Fighter fighter, double duration) {
        this.fighter = fighter;
        this.DURATION = duration;
    }

    public abstract void initialEffect();
    public abstract void updateEffect(double deltaTime);
    public abstract void removeEffect();

    public boolean isEffectComplete() {
        return elapsedTime >= DURATION;
    }

    public Fighter getFighter() {
        return this.fighter;
    }
}
