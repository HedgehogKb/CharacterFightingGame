package com.hedgehogkb.fighter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Fighter {
    private final AnimationHandler animHandler;
    private final MoveHandler moveHandler;
    private final PositionHandler posHandler;

    private double damage;
    private int stocks;
    private boolean invincible;

    public Fighter(AnimationHandler animHandler, MoveHandler moveHandler, PositionHandler posHandler, int stocks) {
        this.animHandler = animHandler;
        this.moveHandler = moveHandler;
        this.posHandler = posHandler;
        
        this.damage = 0;
        this.stocks = stocks;
        this.invincible = false;
    }

    public void moveX(double deltaTime) {

    }

    public void moveY(double deltaTime) {

    }

    public class InputDetector implements KeyListener {

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
