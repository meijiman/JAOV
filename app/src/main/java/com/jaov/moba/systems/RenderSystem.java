package com.jaov.moba.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jaov.moba.components.PositionComponent;

public class RenderSystem extends IteratingSystem {
    
    private ShapeRenderer shapeRenderer;
    private ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    public RenderSystem(ShapeRenderer shapeRenderer) {
        super(Family.all(PositionComponent.class).get());
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);

        shapeRenderer.setColor(0f, 0.8f, 0.2f, 1f);
        shapeRenderer.circle(pos.position.x, pos.position.y, 30);
    }
}
