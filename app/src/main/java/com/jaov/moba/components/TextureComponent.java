package com.jaov.moba.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class TextureComponent implements Component {
    public Texture texture;
    public float width;
    public float height;

    public TextureComponent(Texture texture, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }
}
