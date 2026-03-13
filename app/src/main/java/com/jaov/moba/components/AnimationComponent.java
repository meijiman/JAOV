package com.jaov.moba.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class AnimationComponent implements Component {
    public Texture[] frames;     // mảng các frame
    public float frameDuration;  // giây mỗi frame
    public float stateTime = 0f; // thời gian đã trôi qua
    public float width;
    public float height;

    public AnimationComponent(Texture[] frames, float frameDuration, float width, float height) {
        this.frames = frames;
        this.frameDuration = frameDuration;
        this.width = width;
        this.height = height;
    }

    public Texture getCurrentFrame() {
        int index = (int)(stateTime / frameDuration) % frames.length;
        return frames[index];
    }
}
