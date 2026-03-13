package com.jaov.moba.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public Vector2 position = new Vector2();

    public PositionComponent(float x, float y) {
        position.set(x, y);
    }
}
