package com.hedgehogkb.hitboxes;

import java.awt.geom.Point2D;

import com.hedgehogkb.Vector2;

public class AttackHitbox extends TubeHitbox{
    private double damage;
    private double stunDuration;
    private Vector2<Double> knockback;
    

    public AttackHitbox(Point2D circle1, Point2D circle2, double radius) {
        super(circle1, circle2, radius);
        damage = 0;
        stunDuration = 0;
        knockback = new Vector2<>(0.0, 0.0);
    }

    public AttackHitbox(Point2D circle1, Point2D circle2, double radius, double damage, double stunDuration, double knockbackAmount, double knockbackDirection) {
        super(circle1, circle2, radius);
        this.damage = damage;
        this.stunDuration = stunDuration;
        this.knockback = new Vector2<>(knockbackAmount, knockbackDirection);
    }

    public double getDamage() {
        return damage;
    }
    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getStunDuration() {
        return stunDuration;
    }
    public void setStunDuration(double stunDuration) {
        this.stunDuration = stunDuration;
    }

    public double getKnockbackAmount() {
        return knockback.magnitude;
    }
    public void setKnockbackAmount(double knockbackAmount) {
        this.knockback.magnitude = knockbackAmount;
    }

    public double getKnockbackDirection() {
        return knockback.direction;
    }
    public void setKnockbackDirection(double knockbackDirection) {
        this.knockback.direction = knockbackDirection;
    }
}
