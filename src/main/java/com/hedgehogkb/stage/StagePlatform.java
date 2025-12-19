package com.hedgehogkb.stage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.hedgehogkb.hitboxes.RectHitbox;

public class StagePlatform {
    private Image sprite;
    private BufferedImage image;
    private ArrayList<RectHitbox> hitboxes;

    public void draw(Graphics g) {
        g.drawImage(sprite, 10, 10, null);
    }
}
