package com.hedgehogkb.fighter.animation;

import java.util.ArrayList;

public class MultiAnimation implements Animation {
    public int curAnimationIndex;
    public ArrayList<ArrayList<AnimationFrame>> animations = new ArrayList<>();
    
     @Override
    public void startAnimation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startAnimation'");
    }

    @Override
    public AnimationFrame getCurrentFrame(double deltaTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentFrame'");
    }
}
