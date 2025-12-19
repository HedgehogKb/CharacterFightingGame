package com.hedgehogkb.effects;

import com.hedgehogkb.fighter.Fighter;

public abstract class Effect {
    private final Fighter fighter;
    private final double DURATION; //in seconds
    private double elapsedTime;
    private boolean initialized = false;

    /**
     * Constructor for Effects. Protected so subclasses can construct one for a specific fighter.
     */
    protected Effect(Fighter fighter, double duration) {
        this.fighter = fighter;
        this.DURATION = duration;
    }

    public abstract void initialEffect();
    public abstract void updateEffect(double deltaTime);
    public abstract void removeEffect();

    /**
     * Called each frame to progress the effect. This will call initialEffect() once,
     * then updateEffect() each tick and advance internal timer.
     */
    public void tick(double deltaTime) {
        if (!initialized) {
            initialEffect();
            initialized = true;
        }
        updateEffect(deltaTime);
        elapsedTime += deltaTime;
    }

    /**
     * Called when the effect is removed to allow subclasses to cleanup.
     */
    public void onRemove() {
        removeEffect();
    }

    public boolean isEffectComplete() {
        return elapsedTime >= DURATION;
    }

    public Fighter getFighter() {
        return this.fighter;
    }
}
