package com.hedgehogkb.hitboxes;

public interface LocalHitbox<T extends Hitbox<T>> extends Hitbox<T> {
    public abstract boolean intersects(double xOffset, double yOffset, T o, double oXOffset, double oYOffset);
}
