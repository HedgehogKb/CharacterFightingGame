package com.hedgehogkb.fighter.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.hedgehogkb.hitboxes.AttackHitbox;
import com.hedgehogkb.hitboxes.TubeHitbox;


public class AnimationFrame {
    public BufferedImage sprite;
    public double duration;
    public ArrayList<TubeHitbox> hurtboxes;
    public ArrayList<AttackHitbox> attackHitboxes;
    //public List<Projectile> projectiles;

    public boolean changeXVel;
    public double xVel;
    public boolean changeXAcc;
    public double xAcc;

    public boolean changeYVel;
    public double yVel;
    public boolean changeYAcc;
    public double yAcc;
}
