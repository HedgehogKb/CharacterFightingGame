package com.hedgehogkb.battle;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
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
    private BattleCanvas canvas;

    private ArrayList<Fighter> fighters;
    private Stage stage;
    private ArrayList<Projectile> projectiles;
    private double deltaTime;

    public BattlePanel(Stage stage, ArrayList<Fighter> fighters) {
        if (fighters.size() > 4) {
            throw new IllegalArgumentException("A maximum of 4 fighters are allowed in a battle.");
        }

        this.fighters = fighters;
        this.stage = stage;

        this.panel = new JPanel(new java.awt.BorderLayout());

        panel.setDoubleBuffered(true);
        panel.setPreferredSize(new Dimension(1920, 1080));
        
        canvas = new BattleCanvas(stage, fighters);
        panel.add(canvas.getCanvas(), java.awt.BorderLayout.CENTER);

        for (Fighter fighter : fighters) {
            panel.addKeyListener(fighter.getInputDetector());
        }
    }

    public void drawFrame() {
        canvas.showFrame();
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
