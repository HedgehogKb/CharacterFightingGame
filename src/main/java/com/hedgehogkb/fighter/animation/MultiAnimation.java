package com.hedgehogkb.fighter.animation;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiAnimation implements Animation {
    public int curAnimationIndex;
    ArrayList<AnimationFrame> curAnimation;
    public ArrayList<ArrayList<AnimationFrame>> animations;

    private double elapsedTime;
    private int currentFrameIndex;
    private double nextEndTime;

    private final double[] ANIMATION_DURATIONS;

    public MultiAnimation(ArrayList<ArrayList<AnimationFrame>> animations) {
        this.animations = animations;
        curAnimationIndex = -1;

        elapsedTime = -1;
        curAnimationIndex = 0;
        nextEndTime = -1;

        ANIMATION_DURATIONS = new double[this.animations.size()];
        for (int i = 0; i < ANIMATION_DURATIONS.length; i++) {
            double duration = 0;
            for (AnimationFrame frame : animations.get(i)) {
                duration += frame.duration;
            }
            this.ANIMATION_DURATIONS[i] = duration;
        }
    }
    
     @Override
    public void startAnimation() {
        if (curAnimationIndex >= animations.size() || curAnimationIndex == -1) {
            curAnimationIndex = 0;
        } else {
            curAnimationIndex++;
        }

        curAnimation = animations.get(curAnimationIndex);

        elapsedTime = 0;
        curAnimationIndex = 0;
        nextEndTime = curAnimation.get(0).duration;
    }

    @Override
    public AnimationFrame getCurrentFrame(double deltaTime) {
        if (elapsedTime == -1) {
            throw new IllegalStateException("Animation hasn't been started yet");
        }

        elapsedTime = (elapsedTime + deltaTime) % ANIMATION_DURATIONS[curAnimationIndex];
        
        if (elapsedTime > nextEndTime) {
            curAnimationIndex++;
            if (curAnimationIndex >= curAnimation.size()) curAnimationIndex = 0;
            nextEndTime = curAnimation.get(curAnimationIndex).duration;
        }

        return curAnimation.get(curAnimationIndex);
    }
}
