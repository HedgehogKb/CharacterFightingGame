package com.hedgehogkb.fighter.animation;

import java.util.HashMap;

import com.hedgehogkb.fighter.moves.MoveType;

public class AnimationHandler {
    private HashMap<MoveType, Animation> animations;
    private Animation curAnimation;

    public AnimationHandler(HashMap<MoveType, Animation> animations) {
        this.animations = animations;
        this.curAnimation = null;
    }

    public void setAnimation(MoveType moveType) { //origionally called getAnimationMethod2 if you want to be funny
        Animation animation = animations.get(moveType);
        if (animation == null) {
            throw new IllegalArgumentException("No animation found for move type: " + moveType);
        }

        if (curAnimation == animation) { //this should work because they point to the same animation. you can use equals method otherwise
            return;
        }

        curAnimation = animation;
        curAnimation.startAnimation();
    }

    public AnimationFrame getCurrentFrame(double deltaTime, boolean charging) {
        if (curAnimation instanceof ChargeAnimation ca) {
            ca.setCharging(charging);
        }

        return curAnimation.getCurrentFrame(deltaTime);
    }
}
