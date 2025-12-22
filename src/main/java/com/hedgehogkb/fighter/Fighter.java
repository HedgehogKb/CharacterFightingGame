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
    private final double MAX_GROUNDED_TIME;
    private final int MAX_JUMPS;

    /**
     * How much xAcc decreases each frame of a standing animation
     */
    private final double STANDING_DECEL = 0.1;

    /**
     * How much xAcc decreases each frame of directionless airtime
     */
    private final double AIR_DECEL = 0.05;

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
        this.MAX_GROUNDED_TIME = 0.066;
        this.MAX_JUMPS = 2;

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

        Move newMove = moveHandler.getCurMove(deltaTime, groundedCountdown, MAX_GROUNDED_TIME, jumps, MAX_JUMPS, stunCountdown);

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
        posHandler.updateXPos(deltaTime);
    }

    public void moveY(double deltaTime) {
        posHandler.updateYPos(deltaTime);
    }

    public void setGrounded() {
        groundedCountdown = MAX_GROUNDED_TIME;
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
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            InputType input = keybinds.getKeybind(keyCode);
            if (input != null) {
                moveHandler.setPressed(keybinds.getKeybind(keyCode));
                boolean grounded = groundedCountdown >= MAX_GROUNDED_TIME; //ensures player is actually grounded and not in coyote time
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
