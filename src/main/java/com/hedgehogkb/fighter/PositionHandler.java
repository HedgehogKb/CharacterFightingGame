package com.hedgehogkb.fighter;

import javax.swing.text.Position;

public class PositionHandler {
    private double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    private double xAcc;
    private double yAcc;

    private double maxXVel;
    private double maxYVel;

    public PositionHandler(double xPos, double yPos, double maxXVel, double maxYVel) {        
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = 0;
        this.yVel = 0;
        this.xAcc = 0;
        this.yAcc = 0;

        this.maxXVel = maxXVel;
        this.maxYVel = maxYVel;
    }

    public double getXPos() {
        return xPos;
    }
    public double getYPos() {
        return yPos;
    }

    public void updateXPos(double deltaTime) {
        // Update velocities based on acceleration
        xVel += xAcc * deltaTime;

        // Clamp velocities to max values
        if (xVel > maxXVel) xVel = maxXVel;
        if (xVel < -maxXVel) xVel = -maxXVel;

        // Update positions based on velocities
        xPos += xVel * deltaTime;
    }

    public void updateYPos(double deltaTime) {
        yVel += yAcc * deltaTime;
        
        if (yVel > maxYVel) yVel = maxYVel;
        if (yVel < -maxYVel) yVel = -maxYVel;
        
        yPos += yVel * deltaTime;
    }
}
