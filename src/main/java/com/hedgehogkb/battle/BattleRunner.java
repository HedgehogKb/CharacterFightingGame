package com.hedgehogkb.battle;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.hitboxes.Hitbox;
import com.hedgehogkb.hitboxes.RectHitbox;
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
                    handleStageCollisions(fighter, collisions, fighter::setXPos, F -> F.getXVel(), P -> P.getCenterX(), P -> P.getWidth());
                }
            } while (!collisions.isEmpty());
            
            // move fighter in Y
            fighter.moveY(deltaTime);
            //detect and handle collisions
            do {
                collisions = checkStageHitboxCollision(fighter, T -> !(T instanceof PhysicalPlatform));
                if (!collisions.isEmpty()) {
                    handleStageCollisions(fighter, collisions, fighter::setYPos, F -> F.getYVel(), 
                                            P -> {
                                                return (P.getCenterY() + 0.5 * P.getHeight());
                                            }, 
                                            P -> P.getHeight());
                }
            } while (!collisions.isEmpty());
        
        
            // check for remaining collisions and apply effects (create a per-fighter instance)
            for (StagePlatform platform : checkStageCollision(fighter, T -> true)) {
                com.hedgehogkb.effects.Effect effect = platform.createEffect(fighter);
                if (effect != null) {
                    fighter.addEffect(effect);
                }
            }
        }
    }

    private ArrayList<RectHitbox> checkStageHitboxCollision(Fighter fighter, Function<StagePlatform, Boolean> filter) {
        ArrayList<RectHitbox> collidingHitboxes = new ArrayList<>();
        
        double xPos = fighter.getXPos();
        double yPos = fighter.getYPos();

        ArrayList<StagePlatform> platforms = stage.getPermanentPlatforms();
        for (StagePlatform platform : platforms) {
            if (filter.apply(platform)) {
                continue;
            }
            platform.colliding(fighter, xPos, yPos, collidingHitboxes); //collding hitboxes passed in so that stages can add to it
        }

        return collidingHitboxes;
    }

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
 

    private void handleStageCollisions(Fighter fighter, ArrayList<RectHitbox> collidingHitboxes, Consumer<Double> posSetter, 
                                        Function<Fighter, Double> veloGetter, Function<RectHitbox, Double> centerGetter, Function<RectHitbox, Double> widthGetter) {
        
        double directionSign = Math.signum(veloGetter.apply(fighter)); //normally takes fighter.getXVelo() in signum
        if (directionSign == 0) {
            throw new IllegalStateException("Cannot handle stage collision if fighter is not moving.");
        }

        //determine largest offset
        boolean initialized = false;
        double directionMax = 0;
        for (RectHitbox hitbox : collidingHitboxes) {
            double hitboxCenter = centerGetter.apply(hitbox); //hitbox.getCenterX();
            double hitboxWidth = widthGetter.apply(hitbox); //hitbox.getWidth();

            double directionOffset = hitboxCenter + (-1 * directionSign) * hitboxWidth;
            directionOffset = Math.abs(directionOffset);
            directionMax = (!initialized)? directionOffset : Math.max(directionMax, directionOffset); 
        }

        double adjustedPosition = fighter.getXPos() + (directionMax + 1) * directionSign;
        posSetter.accept(adjustedPosition); //fighter.setXPos(adjustedPosition);
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
