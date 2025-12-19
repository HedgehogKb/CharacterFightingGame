package com.hedgehogkb.fighter.animation;

import java.awt.Image;
import java.util.List;

import com.hedgehogkb.hitboxes.TubeHitbox;


public class AnimationFrame {
    public Image sprite;
    public double duration;
    public List<TubeHitbox> hurtboxes;
    public List<TubeHitbox> attackHitboxs;
    //public List<Projectile> projectiles;

    public double xVelo;
    public double yVelo;
}
