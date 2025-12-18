package com.hedgehogkb.fighter;

import java.util.HashMap;

public class AnimationHandler {
    HashMap<MoveType, Animation> animations;

    public AnimationHandler() {
    }

    public AnimationFrame getAnimationMethod2(MoveType moveType, double currentTime) {
        Animation animation = animations.get(moveType);
        if (animation != null) {
            return animation.getCurrentFrame(currentTime);
        }
        throw new IllegalArgumentException("No animation found for move type: " + moveType);
    }
}
