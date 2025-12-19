package com.hedgehogkb.stage.platforms;

import com.hedgehogkb.effects.Effect;

public class IcePlatform extends StagePlatform {
    @Override
    public Effect createEffect(com.hedgehogkb.fighter.Fighter fighter) {
        // Create a short-lived effect that reduces maximum horizontal speed to simulate ice.
        return new com.hedgehogkb.effects.SpeedModifierEffect(fighter, 2.0, 0.5);
    }
    
}
