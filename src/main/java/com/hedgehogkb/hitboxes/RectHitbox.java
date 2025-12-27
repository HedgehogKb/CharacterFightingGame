package com.hedgehogkb.hitboxes;

import java.awt.geom.Point2D;

import com.hedgehogkb.fighter.Direction;

public class RectHitbox implements LocalHitbox<RectHitbox> {
    private Point2D baseCenter;
    private double width;
    private double heigth;

    public RectHitbox(double centerX, double centerY, double width, double height) {
        baseCenter = new Point2D.Double(centerX, centerY);
        this.width = width;
        this.heigth = height;
    }

    @Override
    public boolean intersects(RectHitbox o) {        
       return intersects(0,0, Direction.RIGHT, o,0,0, Direction.RIGHT);
    }

    @Override
    public boolean intersects(double xOffset, double yOffset, Direction direction, RectHitbox o, double oXOffset, double oYOffset, Direction oDirection) {
               // Calculate bounds for this rectangle (center base)
        double thisLeft = baseCenter.getX() - width / 2 + xOffset;
        double thisRight = baseCenter.getX() + width / 2 + xOffset;
        double thisBottom = -1 * (baseCenter.getY() + yOffset);
        double thisTop = -1 * (baseCenter.getY() - heigth + yOffset);
        
        // Calculate bounds for other rectangle
        double otherLeft = o.baseCenter.getX() - o.width / 2 + oXOffset;
        double otherRight = o.baseCenter.getX() + o.width / 2 + oXOffset;
        double otherBottom = -1 * (o.baseCenter.getY() + oYOffset);
        double otherTop = -1 * (o.baseCenter.getY() - o.heigth + oYOffset);

        return ((thisLeft <= otherLeft && thisRight >= otherLeft) 
                || (thisLeft <= otherRight && thisRight >= otherRight))
                && ((thisTop >= otherTop && thisBottom <= otherTop)
                || (thisTop >= otherBottom && thisBottom <= otherBottom));
    }

    //  GETTERS AND SETTERS

    public double getCenterX() {
        return this.baseCenter.getX();
    }

    public double getCenterY() {
        return this.baseCenter.getY();
    }

    public double getWidth() {
        return this.width;
    }
    
    public double getHeight() {
        return this.heigth;
    }
    
}
