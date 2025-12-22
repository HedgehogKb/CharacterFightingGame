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
import com.hedgehogkb.keybinds.InputType;
import com.hedgehogkb.keybinds.KeybindSettings;
import com.hedgehogkb.keybinds.KeybindSettings.Keybinds;

public class Fighter {

    /*
     * These are basically final, but due to effects I'm leaving them
     * as not final
     */

    /**
     * How long coyotee times lasts.
     */
    private  double max_grounded_time;

    /**
     * The maximum number of jumps can can be completed before touching on the ground again.
     * This includes the ground jump, so a value of 1 would be no air jumps.
     */
    private  int max_jumps;

    /**
     * How much xAcc decreases each second of a standing animation
     */
    private double standing_decel;

    /**
     * How much xAcc decreases each second of directionless airtime
     */
    private double air_decel;

    /**
     * How much velocity changes after one second of walking
     */
    private double walking_acc;

    /**
     * The highest value of velocity when walking.
     */
    private double max_walking_vel;

    /**
     * How much velocity changes after one second of sprinting.
     */
    private double sprinting_acc;

    /**
     * The highest value of velocity when sprinting.
     */
    private double max_sprinting_vel;

    /**
     * the quickest that the character can move vertically upward
     */
    private double max_y_vel;

    //Handlers (and adjacent)
    private final AnimationHandler animHandler;
    private final MoveHandler moveHandler;
    private final PositionHandler posHandler;
    private final InputDetector  inputDetector;
    private final Keybinds keybinds;

    //Hitboxes and move information
    private RectHitbox enviromentHitbox;
    private ArrayList<TubeHitbox> hurtboxes;
    private ArrayList<AttackHitbox> attackHitboxes;
    private Move curMove;
    private Attack attack;
    private double groundedCountdown;
    private int jumps;


    //damage and effect information
    private int stocks;
    private double damage;
    private double stunCountdown;
    private double invincibleCountdown;
    private final ArrayList<Effect> effects;    

    //Visual information
    private Direction fighterFacing;
    private BufferedImage sprite;
    


    public Fighter(KeybindSettings keySettings, AnimationHandler animHandler, MoveHandler moveHandler, PositionHandler posHandler, int stocks) {
        this.max_grounded_time = 0.066;
        this.max_jumps = 2;

        this.animHandler = animHandler;
        this.moveHandler = moveHandler;
        this.posHandler = posHandler;
        this.inputDetector = new InputDetector();
        this.keybinds = keySettings.getKeybinds(1);

        this.groundedCountdown = 0;

        this.damage = 0;
        this.stocks = stocks;
        this.invincibleCountdown = 0;
        this.stunCountdown = 0;

        effects = new ArrayList<>();

        fighterFacing = Direction.RIGHT;
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

        Move newMove = moveHandler.getCurMove(deltaTime, groundedCountdown, max_grounded_time, jumps, max_jumps, stunCountdown);

        if (newMove != curMove) { //TODO: getCurMove method needs to take some stuff in. like being stunned or grounded
            curMove = newMove;
            animHandler.setAnimation(curMove.getMoveType());

            if (curMove instanceof Attack A) {
                this.attack = A;
            }

            if (curMove.getMoveType() == MoveType.JUMPING) {
                jumps++;
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
        double deceleration = 0;
        if (groundedCountdown < max_grounded_time) {
            deceleration = air_decel;
        } else if (curMove.getMoveType() == MoveType.STANDING) {
            deceleration = standing_decel;
        }

        double maxXVelo = max_sprinting_vel;
        if (curMove.getMoveType() == MoveType.WALKING) maxXVelo = max_walking_vel;

        posHandler.updateXPos(deltaTime, maxXVelo, deceleration, fighterFacing);
    }

    public void moveY(double deltaTime) {
        posHandler.updateYPos(deltaTime, max_y_vel);
    }

    public void setGrounded() {
        groundedCountdown = max_grounded_time;
        jumps = 0;
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

    public Direction getFighterDirection() {
        /* if (curMove.getMoveType() == MoveType.TURNING) {
            return (fighterFacing == Direction.RIGHT) ? Direction.LEFT : Direction.RIGHT;
        } */
        return fighterFacing;
    }

    public ArrayList<Effect> getEffects() {
        return this.effects;
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
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            InputType input = keybinds.getKeybind(keyCode);
            if (input != null) {
                moveHandler.setPressed(keybinds.getKeybind(keyCode));
                boolean grounded = groundedCountdown >= max_grounded_time; //ensures player is actually grounded and not in coyote time
                boolean attacking = attack != null;

                // Player can change direction if on ground and attacking.
                if (input == InputType.FORWARD && grounded && !attacking) {
                    fighterFacing = Direction.RIGHT;
                } else if (input == InputType.BACKWARD && grounded && !attacking) {
                    fighterFacing = Direction.LEFT;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keybinds.getKeybind(keyCode) != null) {
                moveHandler.setReleased(keybinds.getKeybind(keyCode));
            }        
        }
    }
}
