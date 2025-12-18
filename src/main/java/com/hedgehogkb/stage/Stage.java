package com.hedgehogkb.stage;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

public class Stage {
    private Image backgroundSprite;
    private ArrayList<StagePlatform> permanentPlatforms;
    private ArrayList<StagePlatform> decayingPlatoforms;
    
    private Camera camera;
    private double preferedZoom;

    public void updateCameraPosition(ArrayList<Character> characters) {

    }

    public void draw(Graphics g) {
        
    }

}
