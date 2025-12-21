package com.hedgehogkb.fighter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.hedgehogkb.effects.Effect;
import com.hedgehogkb.fighter.animation.AnimationFrame;
import com.hedgehogkb.fighter.animation.AnimationHandler;
import com.hedgehogkb.fighter.moves.Attack;
import com.hedgehogkb.fighter.moves.Move;
import com.hedgehogkb.fighter.moves.MoveHandler;
import com.hedgehogkb.fighter.moves.MoveType;
import com.hedgehogkb.hitboxes.AttackHitbox;
import com.hedgehogkb.hitboxes.RectHitbox;
import com.hedgehogkb.hitboxes.TubeHitbox;

public class Fighter {
    //Handlers (and adjacent)
    private final AnimationHandler animHandler;
    private final MoveHandler moveHandler;
    private final PositionHandler posHandler;
    private final InputDetector  inputDetector;

    //Hitboxes and move information
    private RectHitbox enviromentHitbox;
    private ArrayList<TubeHitbox> hurtboxes;
    private ArrayList<AttackHitbox> attackHitboxes;
    private Move curMove;
    private Attack attack;
    private double groundedCountdown;

    //damage and effect information
    private int stocks;
    private double damage;
    private double stunCountdown;
    private double invincibleCountdown;
    private final ArrayList<Effect> effects;    

    //Visual information
    private BufferedImage sprite;


    public Fighter(AnimationHandler animHandler, MoveHandler moveHandler, PositionHandler posHandler, int stocks) {
        this.animHandler = animHandler;
        this.moveHandler = moveHandler;
        this.posHandler = posHandler;
        this.inputDetector = new InputDetector();

        this.groundedCountdown = 0;

        this.damage = 0;
        this.stocks = stocks;
        this.invincibleCountdown = 0;
        this.stunCountdown = 0;

        effects = new ArrayList<>();
    }

    public void update(double deltaTime) {
        if (groundedCountdown > 0) {
            groundedCountdown -= deltaTime;
        }
        if (invincibleCountdown > 0) {
            invincibleCountdown -= deltaTime;
        }
        if (stunCountdown > 0) {
            stunCountdown -= deltaTime;
        }

        if (attack != null) {
            attack.advanceTimer(deltaTime);
        }

        Move newMove = moveHandler.getCurMove();

        if (newMove != curMove) { //TODO: getCurMove method needs to take some stuff in. like being stunned or grounded
            curMove = moveHandler.getCurMove();
            animHandler.setAnimation(curMove.getMoveType());

            if (curMove instanceof Attack A) {
                this.attack = A;
            }
        }
        AnimationFrame curFrame = animHandler.getCurrentFrame(deltaTime);
        updateAnimationInformation(curFrame);
    }

    private void updateAnimationInformation(AnimationFrame curFrame) {
        this.hurtboxes = curFrame.hurtboxes; //TODO: consider changing this to getter and setter rather than public fields
        this.attackHitboxes = curFrame.attackHitboxs;

        if (curFrame.changeXVel) {
            posHandler.setXVel(curFrame.xVel);
        }
        if (curFrame.changeYVel) {
            posHandler.setYVel(curFrame.yVel);
        }

        this.sprite = curFrame.sprite;
    }

    public void moveX(double deltaTime) {
        posHandler.updateXPos(deltaTime);
    }

    public void moveY(double deltaTime) {
        posHandler.updateYPos(deltaTime);
    }

    public void setGrounded() {
        groundedCountdown = 0.66; // maybe dont just use magic number but instead make this be a customizable property (maybe a passive)
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
        if (invincibleCountdown > 0) return;

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
    public void setXVel(double xVel) {
        posHandler.setXVel(xVel);
    }

    public double getYVel() {
        return posHandler.getYVel();
    }
    public void setYVel(double yVel) {
        posHandler.setYVel(yVel);
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
