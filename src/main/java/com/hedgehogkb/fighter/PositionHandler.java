package com.hedgehogkb.fighter;

import javax.swing.text.Position;

public class PositionHandler {
    private double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    private double xAcc;
    private double yAcc;

    private double defaultXAcc; //will be used to set the acceleration back to normal after moving
    private double defaultYAcc;

    public PositionHandler(double xPos, double yPos, double maxXVel, double maxYVel) {        
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = 0;
        this.yVel = 0;
        this.xAcc = 0;
        this.yAcc = 0;

    }


    public void updateXPos(double deltaTime, double maxXVel, double deceleration, Direction fighterDirection) {
        int dirMultiplier = fighterDirection.getMultiplier();
        //update acceleration
        
       
        // Update velocities based on acceleration
        if (deceleration > 0) {
            xVel += deceleration * dirMultiplier * deltaTime;
        } else {
            xVel += xAcc * deltaTime;
        }

        // Clamp velocities to max values
        if (xVel > maxXVel) xVel = maxXVel;
        if (xVel < -maxXVel) xVel = -maxXVel;

        // Update positions based on velocities
        xPos += xVel * deltaTime;
    }

    public void updateYPos(double deltaTime, double maxYVel) {
        yVel += yAcc * deltaTime;
        
        //The player can move downard (on the screen that's positive y vel) as fast as they want. I may not even need/want maxYVel.
        //if (yVel > maxYVel) yVel = maxYVel; 
        if (yVel < -maxYVel) yVel = -maxYVel;
        
        yPos += yVel * deltaTime;
    }

    // GETTERS AND SETTERS
        public double getXPos() {
        return xPos;
    }
    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    public double getYPos() {
        return yPos;
    }
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    public double getXVel() {
        return xVel;
    }
    public void setXVel(double xVel) {
        this.xVel = xVel;
    }

    public double getYVel() {
        return yVel;
    }
    public void setYVel(double yVel) {
        this.yVel = yVel;
    }

    public double getXAcc() {
        return xAcc;
    }
    public void setXAcc(double xAcc) {
        this.xAcc = xAcc;
    }

    public double getYAcc() {
        return yAcc;
    }
    public void setYAcc(double yAcc) {
        this.yAcc = yAcc;
    }
}
