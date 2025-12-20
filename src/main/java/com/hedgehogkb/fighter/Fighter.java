package com.hedgehogkb.fighter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import com.hedgehogkb.attack.Attack;
import com.hedgehogkb.effects.Effect;
import com.hedgehogkb.fighter.animation.AnimationHandler;
import com.hedgehogkb.hitboxes.AttackHitbox;
import com.hedgehogkb.hitboxes.RectHitbox;
import com.hedgehogkb.hitboxes.TubeHitbox;

public class Fighter {
    private final AnimationHandler animHandler;
    private final MoveHandler moveHandler;
    private final PositionHandler posHandler;
    private final InputDetector  inputDetector;

    private RectHitbox enviromentHitbox;
    private ArrayList<TubeHitbox> hurtboxes;
    private ArrayList<AttackHitbox> attackHitboxes;
    private Attack attack;

    private double damage;
    private double stunCountdown;
    private int stocks;
    private boolean invincible;

    private final ArrayList<Effect> effects;    


    public Fighter(AnimationHandler animHandler, MoveHandler moveHandler, PositionHandler posHandler, int stocks) {
        this.animHandler = animHandler;
        this.moveHandler = moveHandler;
        this.posHandler = posHandler;
        this.inputDetector = new InputDetector();

        this.damage = 0;
        this.stocks = stocks;
        this.invincible = false;

        effects = new ArrayList<>();
    }

    public void moveX(double deltaTime) {
        posHandler.updateXPos(deltaTime);
    }

    public void moveY(double deltaTime) {
        posHandler.updateYPos(deltaTime);
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }

    public void removeEffect(Effect effect) {
        this.effects.remove(effect);
    }

    public boolean canHit(Fighter o) {
        if (attack == null) {
            return true;
        }
        
        return !attack.isMarked(o);
    }

    public void applyDamage(double damage) {
        if (invincible) return;

        this.damage += damage;
    }

    public void applyStun(double duration) {
        if (stunCountdown > 0) return;
        stunCountdown = duration;
    }

    public java.util.List<Effect> getEffects() {
        return this.effects;
    }

    public double getMaxXVel() {
        return this.posHandler.getMaxXVel();
    }

    public void setMaxXVel(double maxXVel) {
        this.posHandler.setMaxXVel(maxXVel);
    }

    //Getters and setters

    public double getXPos() {
        return posHandler.getXPos();
    }
    public void setXPos(double xPos) {
        posHandler.setXPos(xPos);
    }

    public double getYPos() {
        return posHandler.getYPos();
    }
    public void setYPos(double yPos) {
        posHandler.setYAcc(yPos);
    }

    public double getXVel() {
        return posHandler.getXVel();
    }

    public double getYVel() {
        return posHandler.getYVel();
    }

    public RectHitbox getEnviromentHitbox() {
        return this.enviromentHitbox;
    }

    public ArrayList<TubeHitbox> getHurtboxes() {
        return this.hurtboxes;
    }

    public ArrayList<AttackHitbox> getAttackHitboxes() {
        return this.attackHitboxes;
    }

    public InputDetector getInputDetector() {
        return this.inputDetector;
    }

    // PRIVATE CLASSES

    private class InputDetector implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void keyReleased(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
