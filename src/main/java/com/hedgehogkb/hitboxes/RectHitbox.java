package com.hedgehogkb.hitboxes;

import java.awt.geom.Point2D;

public class RectHitbox implements Hitbox<RectHitbox> {
    private Point2D baseCenter;
    private double width;
    private double heigth;

    @Override
    public boolean intersects(RectHitbox o) {        
        // Calculate bounds for this rectangle (center base)
        double thisLeft = baseCenter.getX() - width / 2;
        double thisRight = baseCenter.getX() + width / 2;
        double thisBottom = baseCenter.getY();
        double thisTop = baseCenter.getY() - heigth;
        
        // Calculate bounds for other rectangle
        double otherLeft = o.baseCenter.getX() - o.width / 2;
        double otherRight = o.baseCenter.getX() + o.width / 2;
        double otherBottom = o.baseCenter.getY();
        double otherTop = o.baseCenter.getY() - o.heigth;
        
        return ((thisLeft <= otherLeft && thisRight >= otherLeft) 
                || (thisLeft <= otherRight && thisRight >= otherRight))
                && ((thisTop >= otherTop && thisBottom <= otherTop)
                || (thisTop >= otherBottom && thisBottom <= otherBottom));
    }
    
}
