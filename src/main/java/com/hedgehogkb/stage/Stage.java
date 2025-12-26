package com.hedgehogkb.stage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.hedgehogkb.hitboxes.RectHitbox;
import com.hedgehogkb.stage.platforms.PhysicalPlatform;
import com.hedgehogkb.stage.platforms.StagePlatform;

public class Stage {
    private Image backgroundSprite;
    private ArrayList<StagePlatform> permanentPlatforms;
    private ArrayList<StagePlatform> decayingPlatforms;
    
    private Camera camera;
    private double preferedZoom;

    public Stage() {
        camera = new Camera(new Point2D.Double(0,0), 1);
        permanentPlatforms = new ArrayList<>();
        decayingPlatforms = new ArrayList<>();

        PhysicalPlatform platform = new PhysicalPlatform();
        RectHitbox hitbox = new RectHitbox(0, 64, 64*9, 64);
        platform.addHitbox(hitbox);
        
        permanentPlatforms.add(platform);
    }

    public void updateCameraPosition(ArrayList<Character> characters) {
        //do nothing rn
    }

    public ArrayList<StagePlatform> getPermanentPlatforms() {
        return this.permanentPlatforms;
    }

    public ArrayList<StagePlatform> getDecayingPlatforms() {
        return this.decayingPlatforms;
    }
}
