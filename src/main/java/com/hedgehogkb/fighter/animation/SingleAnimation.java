package com.hedgehogkb.fighter.animation;

import java.util.ArrayList;

public class SingleAnimation implements Animation {
    protected ArrayList<AnimationFrame> frames;

    private double elapsedTime;
    private int currentFrameIndex;
    private double nextEndTime;
    private final double ANIMATION_DURATION;

    public SingleAnimation(ArrayList<AnimationFrame> frames) {
        if (frames.isEmpty()) {
            //TODO: add this back when done testing.
            //throw new IllegalArgumentException("animaiton must have at least 1 frame");
        }

        this.frames = frames;

        this.elapsedTime = -1;
        this.currentFrameIndex = 0;
        this.nextEndTime = -1;

        double animationTime = 0;
        for (AnimationFrame frame : frames) {
            animationTime += frame.duration;
        }
        this.ANIMATION_DURATION = animationTime;
    }

    @Override
    public void startAnimation() {
        this.elapsedTime = 0;
        this.currentFrameIndex = 0;
        this.nextEndTime = frames.get(0).duration;
    }

    @Override
    public AnimationFrame getCurrentFrame(double deltaTime) {
        if (elapsedTime == -1) {
            throw new IllegalStateException("Animation hasn't been started");
        }

        if (frames.size() <= 0) return new AnimationFrame();

        //loops animation. MoveHanlder is responsible for actually stopping moves.
        elapsedTime += deltaTime;
        if (elapsedTime > ANIMATION_DURATION) {
            elapsedTime = 0;
            currentFrameIndex = 0;
            nextEndTime = frames.get(0).duration;
        }

        // responsible for advancing or restarting the current frame index.
        // also sets the nextEndTime to the new frame
        if (elapsedTime > nextEndTime) {
            currentFrameIndex++;
            if (currentFrameIndex >= frames.size()) {
                currentFrameIndex = 0;
                nextEndTime = frames.get(0).duration;
            }
            else {
                nextEndTime += frames.get(currentFrameIndex).duration;
            }
        }

        return frames.get(currentFrameIndex);
    }

    // GETTERS AND SETTERS
    public double getDuration() {
        return this.ANIMATION_DURATION;
    }
}
