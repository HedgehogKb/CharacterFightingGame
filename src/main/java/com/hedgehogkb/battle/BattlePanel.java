package com.hedgehogkb.battle;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hedgehogkb.fighter.Fighter;
import com.hedgehogkb.projectile.Projectile;
import com.hedgehogkb.stage.Stage;

public class BattlePanel {
    private int FRAMES_PER_SECOND = 30;

    private JPanel panel;
    private Canvas canvas;

    private List<Fighter> fighters;
    private Stage stage;
    private ArrayList<Projectile> projectiles;
    private double deltaTime;

    public BattlePanel(Stage stage, Fighter ... fighters) {
        if (fighters.length > 4) {
            throw new IllegalArgumentException("A maximum of 4 fighters are allowed in a battle.");
        }

        this.fighters = Arrays.asList(fighters);
        this.stage = stage;

        this.panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.red);
                g.fillRect(0, 0, 800, 800);
            }
        };

        panel.setDoubleBuffered(true);
    }

    public void repaint() {
        SwingUtilities.invokeLater(() -> panel.repaint());
    }

    public void drawFrame(double deltaTime) {
        //draw stage
        //draw fighters
        //draw projectiles
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
