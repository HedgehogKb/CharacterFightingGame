package com.hedgehogkb.fighter;

public enum Direction {
    LEFT(-1), RIGHT(1);

    private int multiplier;

    private Direction(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return this.multiplier;
    }
}
