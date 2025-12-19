package com.hedgehogkb.battle;

import java.util.ArrayList;

import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.projectile.Projectile;
import com.hedgehogkb.stage.Stage;

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
        moveFighers(deltaTime);
        runEffects(deltaTime);
        attackCollision(deltaTime);
    }

    private void moveFighers(double deltaTime) {
        for (Fighter curFighter : fighters) {
            // move fighter in X
            // check if fighter colliding with stage
            // if colliding use binary collision handling      
            
            //repeat for y
        }
    }

    private void runEffects(double deltaTime) {

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
