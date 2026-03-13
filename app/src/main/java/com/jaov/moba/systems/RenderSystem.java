package com.jaov.moba.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jaov.moba.components.PositionComponent;
import com.jaov.moba.components.TextureComponent;

public class RenderSystem extends IteratingSystem {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextureComponent> tm =
        ComponentMapper.getFor(TextureComponent.class);

    public RenderSystem(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super(Family.all(PositionComponent.class).get());
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        TextureComponent tex = tm.get(entity); // null nếu không có

        if (tex != null) {
            // Vẽ ảnh — căn giữa tại pos
            batch.draw(tex.texture,
                pos.position.x - tex.width / 2,
                pos.position.y - tex.height / 2,
                tex.width, tex.height);
        } else {
            // Tạm dừng batch để dùng shapeRenderer
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0.8f, 0.2f, 1f);
            shapeRenderer.circle(pos.position.x, pos.position.y, 30);
            shapeRenderer.end();
            batch.begin();
        }
    }
}
