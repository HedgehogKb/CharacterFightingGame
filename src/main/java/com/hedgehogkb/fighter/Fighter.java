package com.hedgehogkb.stage;

import java.awt.Graphics;

public class GameCharacter {
    private AnimationHandler animHandler;
    private double xPos;
    private double yPos;

    public GameCharacter(AnimationHandler animHandler) {
        this.animHandler = new AnimationHandler(this);
        this.xPos = 0;
        this.yPos = 0;
    }

    public double getXPos() {
        return this.xPos;
    }

    public void draw(Graphics g) {
        AnimationFrame frame = animHandler.getAnimationMethod2(xPos);
        //draw some stuff with the frame
    }
}
