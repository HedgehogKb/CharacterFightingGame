package com.hedgehogkb.battle;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.hedgehogkb.effects.Effect;
import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.hitboxes.AttackHitbox;
import com.hedgehogkb.hitboxes.Hitbox;
import com.hedgehogkb.hitboxes.RectHitbox;
import com.hedgehogkb.hitboxes.TubeHitbox;
import com.hedgehogkb.projectile.Projectile;
import com.hedgehogkb.stage.Stage;
import com.hedgehogkb.stage.platforms.PhysicalPlatform;
import com.hedgehogkb.stage.platforms.StagePlatform;

public class BattleRunner implements Runnable {
    private ArrayList<Fighter> fighters;
    private Stage stage;
    private ArrayList<Projectile> projectiles;

    private final long TARGET_FPS = 1;
    private final long FRAME_TIME = 1_000_000_000 / TARGET_FPS; // in nanoseconds

    private boolean running = false;

    @Override
    public void run() {
        running = true;

        while (running) {
            long lastTime = System.nanoTime();
            long accumulator = 0;

            while (running) {
                long now = System.nanoTime();
                long delta = now - lastTime;
                lastTime = now;

                accumulator += delta;

                // update a ideal frame duration until the game is caught up.
                // this prevents lag from slowing down actual gameplay. For example if
                // it takes 10 frames to render for some reason, 10 updates will happen here.
                while (accumulator >= FRAME_TIME) {
                    update(FRAME_TIME / 1_000_000_000.0);
                    accumulator -= FRAME_TIME;
                }

                render();

                // Sleep for the remaining time in the frame.
                long sleepTime = FRAME_TIME - (System.nanoTime() - now);
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(
                            sleepTime / 1_000_000,
                            (int)(sleepTime % 1_000_000)
                        );
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    // UPDATE METHODS \\

    private void update(double deltaTime) {
        //if panels ever move that would come before moving fighters so that collision could work :)
        moveFighers(deltaTime);
        runEffects(deltaTime);
        attackCollision(deltaTime);
    }

    private void moveFighers(double deltaTime) {
        for (Fighter fighter : fighters) {
            ArrayList<RectHitbox> collisions;
            
            // move fighter in X
            fighter.moveX(deltaTime);
            //detect and handle collisions
            do {
                collisions = checkStageHitboxCollision(fighter, T -> !(T instanceof PhysicalPlatform));
                if (!collisions.isEmpty()) {
                    handleXStageCollision(fighter, collisions);
                    fighter.setXVel(0);
                }
            } while (!collisions.isEmpty());
            
            // move fighter in Y
            fighter.moveY(deltaTime);
            //detect and handle collisions
            do {
                collisions = checkStageHitboxCollision(fighter, T -> !(T instanceof PhysicalPlatform));
                if (!collisions.isEmpty()) {
                    if (fighter.getYVel() > 0) {
                        fighter.setGrounded();
                    }
                    handleYStageCollision(fighter, collisions);
                    fighter.setYVel(0);
                }
            } while (!collisions.isEmpty());
        
        
            // check for remaining collisions and apply effects (create a per-fighter instance)
            for (StagePlatform platform : checkStageCollision(fighter, T -> true)) {
                Effect effect = platform.createEffect(fighter);
                if (effect != null) {
                    fighter.addEffect(effect);
                }
            }
        }
    }

    /**
     * Returns each hitbox that the player collides with.
     * Doesn't provie information about the stage platform, only it's hitboxes.
     * @param fighter
     * @param filter
     * @return
     */
    private ArrayList<RectHitbox> checkStageHitboxCollision(Fighter fighter, Function<StagePlatform, Boolean> filter) {
        ArrayList<RectHitbox> collidingHitboxes = new ArrayList<>();
        
        double xPos = fighter.getXPos();
        double yPos = fighter.getYPos();

        ArrayList<StagePlatform> platforms = stage.getPermanentPlatforms();
        for (StagePlatform platform : platforms) {
            if (filter.apply(platform)) {
                continue;
            }
            platform.colliding(fighter, xPos, yPos, collidingHitboxes); //colldingHitboxes passed in so that platforms can add to it
        }

        return collidingHitboxes;
    }

    /**
     * Returns the stage platforms that the player collides with.
     * This allows for the effects to be applied.
     * @param fighter
     * @param filter
     * @return
     */
    private ArrayList<StagePlatform> checkStageCollision(Fighter fighter, Function<StagePlatform, Boolean> filter) {
        ArrayList<StagePlatform> collidingHitboxes = new ArrayList<>();
        
        double xPos = fighter.getXPos();
        double yPos = fighter.getYPos();

        ArrayList<StagePlatform> platforms = stage.getPermanentPlatforms();
        for (StagePlatform platform : platforms) {
            if (filter.apply(platform)) {
                continue;
            }
            if (platform.colliding(fighter, xPos, yPos, null)) {
                collidingHitboxes.add(platform);
            }
        }

        return collidingHitboxes;
    }
 

    /* Good idea but didn't work. Maybe implment in future because cool :)

    private void handleStageCollision(Fighter fighter, ArrayList<RectHitbox> collidingHitboxes, Consumer<Double> posSetter, 
                                        Function<Fighter, Double> veloGetter, Function<RectHitbox, Double> centerGetter, Function<RectHitbox, Double> widthGetter) {
        
        double directionSign = Math.signum(veloGetter.apply(fighter)); //normally takes fighter.getXVelo() in signum
        if (directionSign == 0) {
            throw new IllegalStateException("Cannot handle stage collision if fighter is not moving.");
        }

        //determine largest offset
        boolean initialized = false;
        double furthestHitboxBound = 0;
        for (RectHitbox hitbox : collidingHitboxes) {
            double hitboxCenter = centerGetter.apply(hitbox); //hitbox.getCenterX();
            double hitboxWidth = widthGetter.apply(hitbox); //hitbox.getWidth();


        }

        double adjustedPosition;
        posSetter.accept(adjustedPosition); //fighter.setXPos(adjustedPosition);
    } */

    private void handleXStageCollision(Fighter fighter, ArrayList<RectHitbox> collidingHitboxes) {
        double directionSign = Math.signum(fighter.getXVel()); //normally takes fighter.getXVelo() in signum
        if (directionSign == 0) {
            throw new IllegalStateException("Cannot handle stage collision if fighter is not moving.");
        }

        //if plus smallest; if minus largest
        double furthestHitboxDir = 0;
        boolean initialized = false;
        for (RectHitbox hitbox : collidingHitboxes) {
            double hitboxCenterX = hitbox.getCenterX();
            double hitboxWidth = hitbox.getWidth();

            double xBound = hitboxCenterX + directionSign * -1 * 0.5 * hitboxWidth;
            if (directionSign < 0) {
                furthestHitboxDir = (!initialized)? xBound : Math.min(xBound, furthestHitboxDir);
            } else {
                furthestHitboxDir = (!initialized)? xBound : Math.max(xBound, furthestHitboxDir);
            }
            initialized = true;
        }

        double adjustedPosition = furthestHitboxDir - directionSign * (0.5 * fighter.getEnviromentHitbox().getWidth() + 1);
        fighter.setXPos(adjustedPosition);
    }

    private void handleYStageCollision(Fighter fighter, ArrayList<RectHitbox> collidingHitboxes) {
        double directionSign = Math.signum(fighter.getXVel()); //normally takes fighter.getXVelo() in signum
        if (directionSign == 0) {
            throw new IllegalStateException("Cannot handle stage collision if fighter is not moving.");
        }

        //if plus smallest; if minus largest
        double furthestHitboxDir = 0;
        boolean initialized = false;
        for (RectHitbox hitbox : collidingHitboxes) {
            double hitboxCenterY = hitbox.getCenterY();
            double hitboxHeight = hitbox.getHeight() * ((directionSign > 0)? 1 : 0); //if going up on screen (negative velocity) important height is at bottom

            double yBound = hitboxCenterY - hitboxHeight;
            if (directionSign < 0) {
                furthestHitboxDir = (!initialized)? yBound : Math.min(yBound, furthestHitboxDir);
            } else {
                furthestHitboxDir = (!initialized)? yBound : Math.max(yBound, furthestHitboxDir);
            }
            initialized = true;
        }

        double adjustedPosition = furthestHitboxDir + fighter.getEnviromentHitbox().getHeight() * 0.5 * ((directionSign > 0)? 0 : 1) - directionSign;
        fighter.setYPos(adjustedPosition);
    }

    private void runEffects(double deltaTime) {
        // iterate over each fighter and progress / remove their active effects
        for (Fighter fighter : fighters) {
            java.util.Iterator<com.hedgehogkb.effects.Effect> it = fighter.getEffects().iterator();
            while (it.hasNext()) {
                com.hedgehogkb.effects.Effect effect = it.next();
                effect.tick(deltaTime);
                if (effect.isEffectComplete()) {
                    effect.onRemove();
                    it.remove();
                }
            }
        }
    }

    private void attackCollision(double deltaTime) { //not sure deltaTime is actually needed
        for (int i = 0; i < fighters.size(); i++) {
            Fighter curFighter = fighters.get(i);
            double xOffset = curFighter.getXPos();
            double yOffset = curFighter.getYPos();
            //loop over all of the other fighters
            for (int v = 0; v < fighters.size(); i++) {
                if (v == i) continue;
                Fighter oFighter = fighters.get(v);
                
                if (!oFighter.canHit(curFighter)) continue;

                double oXOffset = oFighter.getXPos();
                double oYOffset = oFighter.getYPos();

                double mostDamage = 0;
                double mostStun = 0;
                //same for knockback, but might change stuff, so do that later.
                //loop over all of the player's hurtboxes
                for (TubeHitbox hurtBox : curFighter.getHurtboxes()) {
                    for (AttackHitbox attackHitbox : oFighter.getAttackHitboxes()) {
                        if (hurtBox.intersects(xOffset, yOffset, attackHitbox, oXOffset, oYOffset)) {
                            mostDamage = Math.max(mostDamage, attackHitbox.getDamage());
                            mostStun = Math.max(mostStun, attackHitbox.getStunDuration());
                            //Same for knockback stuff
                        }
                    }
                }
                curFighter.applyDamage(mostDamage);
                curFighter.applyStun(mostStun);
                //TODO: APPLY KNOCKBACK
            }
    }

    // RENDER METHOD \\

    private void render() {
        //call battlePanel's repaint method.
    }

    public static void main(String[] args) {
        Thread gameThread = new Thread(new BattleRunner(), "battle");
        gameThread.start();
    }
}
