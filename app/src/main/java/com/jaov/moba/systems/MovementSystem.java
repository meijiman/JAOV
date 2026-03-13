package com.jaov.moba.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.jaov.moba.components.MovementComponent;
import com.jaov.moba.components.PositionComponent;

public class MovementSystem extends IteratingSystem {
    
    private ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private ComponentMapper<MovementComponent> mm =
        ComponentMapper.getFor(MovementComponent.class);

    public MovementSystem() {
        super(Family.all(PositionComponent.class, MovementComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        MovementComponent move = mm.get(entity);

        if (!move.moving) return;

        Vector2 direction = new Vector2(move.target).sub(pos.position);

        if (direction.len() > 5f) {
            direction.nor();
            move.facingRight = direction.x > 0;
            direction.scl(move.speed * deltaTime);
            pos.position.add(direction);
        } else {
            move.moving = false;
        }
    }

}
