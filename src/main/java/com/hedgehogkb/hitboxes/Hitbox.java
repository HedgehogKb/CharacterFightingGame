package com.hedgehogkb.hitboxes;

public interface Hitbox<T extends Hitbox> {
    public boolean intersects(T o);
}
