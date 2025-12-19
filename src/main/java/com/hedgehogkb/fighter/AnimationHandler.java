package com.hedgehogkb.fighter;

import java.util.HashMap;

public class AnimationHandler {
    HashMap<MoveType, Animation> animations;
    Animation curAnimation;

    public AnimationHandler() {
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

    public AnimationFrame getCurrentFrame(double deltaTime) {
        return curAnimation.getCurrentFrame(deltaTime);
    }
}
