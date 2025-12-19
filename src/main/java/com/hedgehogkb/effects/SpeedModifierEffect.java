package com.hedgehogkb.effects;

import com.hedgehogkb.fighter.Fighter;

/**
 * Simple effect that multiplies the fighter's max horizontal velocity by a factor
 * for a limited duration.
 */
public class SpeedModifierEffect extends Effect {
    private final double factor;
    private double originalMaxX;

    public SpeedModifierEffect(Fighter fighter, double durationSeconds, double factor) {
        super(fighter, durationSeconds);
        this.factor = factor;
    }

    @Override
    public void initialEffect() {
        // store original and apply multiplier
        originalMaxX = getFighter().getMaxXVel();
        getFighter().setMaxXVel(originalMaxX * factor);
    }

    @Override
    public void updateEffect(double deltaTime) {
        // Nothing per-frame for this simple effect
    }

    @Override
    public void removeEffect() {
        // restore original value
        getFighter().setMaxXVel(originalMaxX);
    }
}
