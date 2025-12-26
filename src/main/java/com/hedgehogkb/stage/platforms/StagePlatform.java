package com.hedgehogkb.stage.platforms;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.hedgehogkb.effects.Effect;
import com.hedgehogkb.fighter.Direction;
import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.hitboxes.RectHitbox;

public abstract class StagePlatform {
    private BufferedImage sprite;
    private ArrayList<RectHitbox> hitboxes;

    public StagePlatform() {
        hitboxes = new ArrayList<>();
    }

    /**
     * extending methods can override this if they want to add their own logic like
     * a platform where you only collide if you are on top.
     * @param o
     * @param oXOffset
     * @param oYOffset
     * @return
     */
    public boolean colliding(Fighter o, double oXOffset, double oYOffset, ArrayList<RectHitbox> results) {
        boolean collision = false;
        RectHitbox oHitbox = o.getEnviromentHitbox();
        for (RectHitbox hitbox : hitboxes) {
            if (hitbox.intersects(0, 0, o.getFighterDirection(), oHitbox, oXOffset, oYOffset, Direction.RIGHT)) { //Direction doesn't actually matter here
                collision = true;
                if (results != null) {
                    results.add(hitbox);
                }
            }
        }
        return collision;
    }

    public void addHitbox(RectHitbox rectHitbox) {
        hitboxes.add(rectHitbox);
    }

    public ArrayList<RectHitbox> getHitboxes() {
        return hitboxes;
    }

    /**
     * Create a new Effect instance that will be applied to the provided fighter when
     * they interact with this platform. Return null if the platform does not apply an effect.
     */
    public abstract Effect createEffect(Fighter fighter);
}
