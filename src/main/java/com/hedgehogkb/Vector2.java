package com.hedgehogkb;

public class Vector2<T> {
    public T magnitude;
    public T direction;

    public Vector2(T magnitude, T direction) {
        this.magnitude = magnitude;
        this.direction = direction;
    }
}
