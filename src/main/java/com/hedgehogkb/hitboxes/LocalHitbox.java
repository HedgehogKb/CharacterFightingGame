package com.hedgehogkb.hitboxes;

import com.hedgehogkb.fighter.Direction;

public interface LocalHitbox<T extends Hitbox<T>> extends Hitbox<T> {
    public abstract boolean intersects(double xOffset, double yOffset, Direction direction, T o, double oXOffset, double oYOffset, Direction oDirection);
}
