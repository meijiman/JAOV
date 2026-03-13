package com.jaov.moba.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component {
    public float speed;
    public Vector2 target = new Vector2();
    public boolean moving = false;
    public boolean facingRight = false;

    public MovementComponent(float speed) {
        this.speed = speed;
    }
}
