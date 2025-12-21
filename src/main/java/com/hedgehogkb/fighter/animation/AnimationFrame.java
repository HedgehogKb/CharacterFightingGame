package com.hedgehogkb.fighter.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.hedgehogkb.hitboxes.AttackHitbox;
import com.hedgehogkb.hitboxes.TubeHitbox;


public class AnimationFrame {
    public BufferedImage sprite;
    public double duration;
    public ArrayList<TubeHitbox> hurtboxes;
    public ArrayList<AttackHitbox> attackHitboxs;
    //public List<Projectile> projectiles;

    public boolean changeXVel;
    public double xVel;
    public boolean changeYVel;
    public double yVel;
}
