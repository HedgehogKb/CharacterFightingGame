package com.hedgehogkb.stage.platforms;

import com.hedgehogkb.effects.Effect;
import com.hedgehogkb.fighter.Fighter;

public class PhysicalPlatform extends StagePlatform {
    public PhysicalPlatform() {
        super();
    }

    @Override
    public Effect createEffect(Fighter fighter) {
        // By default physical platforms don't apply any special effect.
        return null;
    }
}
