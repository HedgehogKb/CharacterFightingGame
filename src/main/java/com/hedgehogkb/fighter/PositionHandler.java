package com.hedgehogkb.fighter;

import javax.swing.text.Position;

public class PositionHandler {
    private double lastDeltaTime;

    private double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    private double xAcc;
    private double yAcc;

    private double maxXVel;
    private double maxYVel;

    public PositionHandler(double xPos, double yPos, double maxXVel, double maxYVel, double lastDeltaTime) {
        this.lastDeltaTime = lastDeltaTime;
        
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

    public void updatePosition(double deltaTime) {
        double elapsedTime = (deltaTime - lastDeltaTime);
        // Update velocities based on acceleration
        xVel += xAcc * elapsedTime;
        yVel += yAcc * elapsedTime;

        // Clamp velocities to max values
        if (xVel > maxXVel) xVel = maxXVel;
        if (xVel < -maxXVel) xVel = -maxXVel;
        if (yVel > maxYVel) yVel = maxYVel;
        if (yVel < -maxYVel) yVel = -maxYVel;

        // Update positions based on velocities
        xPos += xVel * elapsedTime;
        yPos += yVel * elapsedTime;
        
        lastDeltaTime = deltaTime;
    }
}
