package com.hedgehogkb;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestDisplay {
    private JFrame frame;
    private JPanel panel;



    public TestDisplay(String title, int width, int height) {
        ArrayList<TubeHitbox> hitboxes = new ArrayList<>();
        hitboxes.add(new TubeHitbox(100, 100, 400, 400, 20));
        hitboxes.add(new TubeHitbox(250, 300, 100, 400, 40));

        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width, height);

                g.setColor(Color.RED);
                for (TubeHitbox hitbox : hitboxes) {
                    hitbox.draw(g);
                }
            }
        };
        frame.add(panel);
        frame.setVisible(true);

        System.out.println("Intersecting: " + hitboxes.get(0).intersects(hitboxes.get(1)));
    }

    public static void main(String[] args) {
        new TestDisplay("Test Display", 800, 600);
    }
}
