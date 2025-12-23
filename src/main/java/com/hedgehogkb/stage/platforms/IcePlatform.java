package com.hedgehogkb.stage.platforms;

import com.hedgehogkb.effects.Effect;
import com.hedgehogkb.effects.IceEffect;;

public class IcePlatform extends StagePlatform {
    @Override
    public Effect createEffect(com.hedgehogkb.fighter.Fighter fighter) {
        // Create a short-lived effect that reduces maximum horizontal speed to simulate ice.
        return new IceEffect(fighter);
    }
    
}
