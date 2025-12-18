package com.hedgehogkb.fighter;

import java.util.ArrayList;

public class Animation {
    ArrayList<AnimationFrame> frames;
    private double startTime;
    private int currentFrameIndex;
    private double nextEndTime;
    private final double ANIMATION_DURATION;

    public Animation(ArrayList<AnimationFrame> frames) {
        this.frames = frames;
        this.startTime = -1;
        this.currentFrameIndex = 0;
        this.nextEndTime = -1;

        double animationTime = 0;
        for (AnimationFrame frame : frames) {
            animationTime += frame.duration;
        }
        this.ANIMATION_DURATION = animationTime;
    }

    public void startAnimation(double currentTime) {
        this.startTime = currentTime;
        this.currentFrameIndex = 0;
        this.nextEndTime = frames.get(0).duration;
    }

    public AnimationFrame getCurrentFrame(double currentTime) {
        if (startTime == -1) {
            startTime = currentTime;
        }

        double elapsedTime = (currentTime - startTime) % ANIMATION_DURATION; //loops animation. MoveHanlder is responsible for actually stopping moves.
        double frameTime = 0;

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
}
