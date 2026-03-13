package com.jaov.moba.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class AnimationComponent implements Component {
    public Texture[] idleFrames;
    public Texture[] runFrames;
    public float frameDuration;
    public float stateTime = 0f;
    public float width;
    public float height;

    public AnimationComponent(Texture[] idleFrames, Texture[] runFrames,
                               float frameDuration, float width, float height) {
        this.idleFrames = idleFrames;
        this.runFrames = runFrames;
        this.frameDuration = frameDuration;
        this.width = width;
        this.height = height;
    }

    public Texture getCurrentFrame(boolean moving) {
        Texture[] frames = moving ? runFrames : idleFrames;
        int index = (int)(stateTime / frameDuration) % frames.length;
        return frames[index];
    }
}
