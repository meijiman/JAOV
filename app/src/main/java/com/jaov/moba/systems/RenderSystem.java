package com.jaov.moba.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
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

    private Texture towerBlue;
    private Texture towerRed;

    public RenderSystem(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super(Family.all(PositionComponent.class).get());
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        towerBlue = new Texture(Gdx.files.internal("tower_blue.png"));
        towerRed  = new Texture(Gdx.files.internal("tower_red.png"));
    }

    public void dispose() {
        towerBlue.dispose();
        towerRed.dispose();
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

    // Hero sprite = 192px, nhưng phần nhân vật thực tế chỉ chiếm ~2/3 sprite (~128px).
    // Tower = 192px (1:1 với sprite) → trông như ~1.5x nhân vật thực, vừa mắt.
    private static final float TOWER_SIZE = 192f;

    private void drawTower(float cx, float cy, TowerComponent tower) {
        Texture tex = "blue".equals(tower.team) ? towerBlue : towerRed;
        batch.draw(tex, cx - TOWER_SIZE / 2, cy - TOWER_SIZE / 2, TOWER_SIZE, TOWER_SIZE);
    }
}
