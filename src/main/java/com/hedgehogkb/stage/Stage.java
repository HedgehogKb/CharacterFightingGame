package com.hedgehogkb.stage;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import com.hedgehogkb.stage.platforms.StagePlatform;

public class Stage {
    private Image backgroundSprite;
    private ArrayList<StagePlatform> permanentPlatforms;
    private ArrayList<StagePlatform> decayingPlatforms;
    
    private Camera camera;
    private double preferedZoom;

    public void updateCameraPosition(ArrayList<Character> characters) {

    }

    public void draw(Graphics g) {
        
    }

    public ArrayList<StagePlatform> getPermanentPlatforms() {
        return this.permanentPlatforms;
    }

    public ArrayList<StagePlatform> getDecayingPlatforms() {
        return this.decayingPlatforms;
    }
}
