package com.hedgehogkb.fighter.animation;

public interface Animation {
    public AnimationFrame getCurrentFrame(double deltaTime);
    public void startAnimation();
}
