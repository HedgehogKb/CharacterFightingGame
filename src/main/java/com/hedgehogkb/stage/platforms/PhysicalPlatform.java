package com.hedgehogkb.stage.platforms;

import com.hedgehogkb.effects.Effect;

public class PhysicalPlatform extends StagePlatform {
    public PhysicalPlatform() {
        super();
    }

    @Override
    public Effect createEffect(com.hedgehogkb.fighter.Fighter fighter) {
        // By default physical platforms don't apply any special effect.
        return null;
    }
}
