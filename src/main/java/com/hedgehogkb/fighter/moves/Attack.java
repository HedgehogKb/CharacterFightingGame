package com.hedgehogkb.fighter.moves;

import com.hedgehogkb.fighter.Fighter;

public interface Attack extends Move {
    public void markFighter(Fighter fighter);
    public void advanceTimers(double deltaTime);
    public boolean isMarked(Fighter fighter);
}
