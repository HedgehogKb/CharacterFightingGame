package com.hedgehogkb.battle;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.hitboxes.RectHitbox;
import com.hedgehogkb.stage.Stage;
import com.hedgehogkb.stage.platforms.StagePlatform;

public class BattleCanvas {
    private Canvas canvas;
    private Stage stage;
    private ArrayList<Fighter> fighters;

    public BattleCanvas(Stage stage, ArrayList<Fighter> fighters) {
        this.stage = stage;
        this.fighters = fighters;
        this.canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setPreferredSize(new java.awt.Dimension(1920, 1080));
    }

    public void showFrame() {
        BufferStrategy bs = canvas.getBufferStrategy();

        if (bs == null) {
            canvas.createBufferStrategy(2);

            bs = canvas.getBufferStrategy();
        }
        Graphics2D g2d = null;

        do {
            try {
                g2d = (Graphics2D) bs.getDrawGraphics();

                g2d.setColor(Color.blue);
                g2d.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
                double scale = calculateScale();
                drawStage(g2d, scale);
                drawFighters(g2d, scale);

            } finally {
                if (g2d != null) g2d.dispose();
            }
        } while (bs.contentsLost());
        bs.show();
        Toolkit.getDefaultToolkit().sync();
    }

    public double calculateScale() {
        double pixelWidth = 64*7;
        double screenWidth = canvas.getWidth();
        return screenWidth / pixelWidth;
    }

    public void drawStage(Graphics2D g2d, double scale) {
        g2d.setColor(Color.red);
        for (StagePlatform platform : stage.getPermanentPlatforms()) {
            for (RectHitbox hitbox : platform.getHitboxes()) {
               drawRectHitbox(g2d, hitbox, scale);
            }
        }
    }

    public void drawFighters(Graphics2D g2d, double scale) {
        g2d.setColor(Color.GREEN);
        for (Fighter fighter : fighters) {
            drawRectHitbox(g2d, fighter.getEnviromentHitbox(), scale);
            BufferedImage sprite = fighter.getSprite();
            if (sprite != null) {
                double width = sprite.getWidth() * scale;
                double height = sprite.getHeight() * scale;
                double centerX = fighter.getXPos() * scale + 0.5 * canvas.getWidth();
                double centerY = fighter.getYPos() * scale + 0.5 * canvas.getHeight();

                g2d.drawImage(sprite, (int) (centerX - 0.5 * width), (int) (centerY - height), (int) width, (int) height, null);
            }
        }
    }

    public void drawRectHitbox(Graphics2D g2d, RectHitbox hitbox, double scale) {
                double width = hitbox.getWidth() * scale;
                double height = hitbox.getHeight() * scale;
                double centerX = hitbox.getCenterX() * scale + 0.5 * canvas.getWidth();
                double centerY = hitbox.getCenterY() * scale + 0.5 * canvas.getHeight();

                g2d.fillRect((int) (centerX - 0.5 * width), (int) (centerY - height), (int) width, (int) height);
    }

    public Canvas getCanvas() {
        return this.canvas;
    }
}
