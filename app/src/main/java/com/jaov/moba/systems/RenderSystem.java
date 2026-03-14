package com.jaov.moba.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jaov.moba.components.AnimationComponent;
import com.jaov.moba.components.MovementComponent;
import com.jaov.moba.components.PositionComponent;
import com.jaov.moba.components.TextureComponent;
import com.jaov.moba.components.TowerComponent;

public class RenderSystem extends IteratingSystem {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextureComponent> tm =
        ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<AnimationComponent> am =
        ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<TowerComponent> towerMapper =
        ComponentMapper.getFor(TowerComponent.class);

    public RenderSystem(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super(Family.all(PositionComponent.class).get());
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        AnimationComponent anim = am.get(entity);
        TextureComponent tex = tm.get(entity);

        TowerComponent tower = towerMapper.get(entity);

        if (tower != null) {
            drawTower(pos.position.x, pos.position.y, tower);
        } else if (anim != null) {
            MovementComponent move = ComponentMapper.getFor(MovementComponent.class).get(entity);
            boolean isMoving = move != null && move.moving;
            Texture frame = anim.getCurrentFrame(isMoving);
            boolean facingRight = move != null && move.facingRight;
            float x = pos.position.x - anim.width / 2;
            float y = pos.position.y - anim.height / 2;
            if (facingRight) {
                batch.draw(frame, x, y, anim.width, anim.height);
            } else {
                batch.draw(frame, x + anim.width, y, -anim.width, anim.height);
            }
        } else if (tex != null) {
            batch.draw(tex.texture,
                pos.position.x - tex.width / 2,
                pos.position.y - tex.height / 2,
                tex.width, tex.height);
        } else {
            // Fallback: hình tròn
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0.8f, 0.2f, 1f);
            shapeRenderer.circle(pos.position.x, pos.position.y, 30);
            shapeRenderer.end();
            batch.begin();
        }
    }

    private void drawTower(float cx, float cy, TowerComponent tower) {
        boolean isBlue = "blue".equals(tower.team);
        float bodyW = "inner".equals(tower.rank) ? 40f : "mid".equals(tower.rank) ? 34f : 28f;
        float bodyH = "inner".equals(tower.rank) ? 54f : "mid".equals(tower.rank) ? 46f : 38f;
        float battleW = bodyW + 10f;
        float battleH = 10f;

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Thân trụ
        if (isBlue) shapeRenderer.setColor(0.2f, 0.45f, 1.0f, 1f);
        else        shapeRenderer.setColor(1.0f, 0.25f, 0.2f, 1f);
        shapeRenderer.rect(cx - bodyW / 2, cy - bodyH / 2, bodyW, bodyH);

        // Đỉnh trụ (battlements)
        shapeRenderer.rect(cx - battleW / 2, cy + bodyH / 2, battleW, battleH);

        // Viền trắng
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 0.8f);
        shapeRenderer.rect(cx - bodyW / 2, cy - bodyH / 2, bodyW, bodyH);
        shapeRenderer.rect(cx - battleW / 2, cy + bodyH / 2, battleW, battleH);

        shapeRenderer.end();
        batch.begin();
    }
}
