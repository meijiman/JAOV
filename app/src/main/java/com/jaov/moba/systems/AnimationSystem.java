package com.jaov.moba.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jaov.moba.components.AnimationComponent;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<AnimationComponent> am =
        ComponentMapper.getFor(AnimationComponent.class);

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent anim = am.get(entity);
        anim.stateTime += deltaTime; // tăng thời gian mỗi frame
    }
}
