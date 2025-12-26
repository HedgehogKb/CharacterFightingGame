package com.hedgehogkb.fighter.animation;

import java.util.ArrayList;

public class ChargeAnimation implements Animation{
    private boolean charging;

    private SingleAnimation chargeAnimation;
    private SingleAnimation animation;

    public ChargeAnimation(ArrayList<AnimationFrame> chargeFrames, ArrayList<AnimationFrame> frames) {
        chargeAnimation = new SingleAnimation(chargeFrames);
        animation = new SingleAnimation(frames);
    }
    
    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    @Override
    public AnimationFrame getCurrentFrame(double deltaTime) {
        if (charging) {
            return chargeAnimation.getCurrentFrame(deltaTime);
        }
        return animation.getCurrentFrame(deltaTime);
    }

    @Override
    public void startAnimation() {
        charging = true;
        chargeAnimation.startAnimation();;
        animation.startAnimation();
    }
    
}
