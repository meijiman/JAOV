package com.jaov.moba;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jaov.moba.components.AnimationComponent;
import com.jaov.moba.components.PositionComponent;
import com.jaov.moba.components.TowerComponent;

public class MiniMap {

    // World size: 100x100 tiles @ 64px
    private static final float WORLD = 6400f;

    // Minimap display size and screen position (top-left corner)
    private static final float SIZE   = 200f;
    private static final float SCREEN_X = 10f;
    private static final float SCREEN_Y = 510f; // 720 - 10 - 200

    private static final float SCALE = SIZE / WORLD;

    private final ShapeRenderer shapeRenderer;
    private final Engine engine;
    private final Entity heroEntity;
    private final OrthographicCamera screenCam;

    private final ComponentMapper<PositionComponent> pm  = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<TowerComponent>    twm = ComponentMapper.getFor(TowerComponent.class);
    private final ComponentMapper<AnimationComponent> am  = ComponentMapper.getFor(AnimationComponent.class);

    public MiniMap(ShapeRenderer shapeRenderer, Engine engine, Entity heroEntity,
                   OrthographicCamera screenCam) {
        this.shapeRenderer = shapeRenderer;
        this.engine        = engine;
        this.heroEntity    = heroEntity;
        this.screenCam     = screenCam;
    }

    public void render() {
        shapeRenderer.setProjectionMatrix(screenCam.combined);

        // --- Filled layer ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Background
        shapeRenderer.setColor(0.08f, 0.18f, 0.08f, 1f);
        shapeRenderer.rect(SCREEN_X, SCREEN_Y, SIZE, SIZE);

        drawTerrain();
        drawEntities();

        shapeRenderer.end();

        // --- Border ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.75f, 0.75f, 0.75f, 1f);
        shapeRenderer.rect(SCREEN_X, SCREEN_Y, SIZE, SIZE);
        shapeRenderer.end();
    }

    // ---------------------------------------------------------------------------
    // Terrain — approximate shapes matching moba_map.tmx tile layout
    // World y: row 99 (TMX bottom) → y=0, row 0 (TMX top) → y=6336
    //   world_y = (99 - row) * 64
    // ---------------------------------------------------------------------------
    private void drawTerrain() {
        // River: row ≈ col → world goes from top-left (0,6336) to bottom-right (6336,0)
        shapeRenderer.setColor(0.10f, 0.38f, 0.70f, 0.65f);
        for (int i = 0; i < 100; i++) {
            float wx = i * 64 * SCALE;
            float wy = (99 - i) * 64 * SCALE;
            shapeRenderer.rect(SCREEN_X + wx, SCREEN_Y + wy, 3 * 64 * SCALE, 3 * 64 * SCALE);
        }

        // Mid lane: row+col ≈ 99 → world from bottom-left (0,0) to top-right (6336,6336)
        shapeRenderer.setColor(0.55f, 0.42f, 0.08f, 0.75f);
        for (int i = 0; i < 100; i++) {
            float wx = i * 64 * SCALE;
            float wy = i * 64 * SCALE;
            shapeRenderer.rect(SCREEN_X + wx, SCREEN_Y + wy, 4 * 64 * SCALE, 4 * 64 * SCALE);
        }

        // Top lane — vertical strip (cols 3-6, rows 3-88)
        //   world x: 192-448, world y: 704-6144
        shapeRenderer.rect(SCREEN_X + s(192), SCREEN_Y + s(704),  s(256), s(5440));
        // Top lane — horizontal strip (rows 3-6, cols 6-99)
        //   world x: 384-6400, world y: 5952-6208
        shapeRenderer.rect(SCREEN_X + s(384), SCREEN_Y + s(5952), s(6016), s(256));

        // Bot lane — horizontal strip (rows 93-96, cols 0-93)
        //   world x: 0-5952, world y: 192-448
        shapeRenderer.rect(SCREEN_X + s(0),    SCREEN_Y + s(192),  s(5952), s(256));
        // Bot lane — vertical strip (cols 93-96, rows 3-93)
        //   world x: 5952-6208, world y: 448-6144
        shapeRenderer.rect(SCREEN_X + s(5952), SCREEN_Y + s(448),  s(256), s(5696));

        // Blue base (bottom-left: cols 0-11, rows 88-99 → world x 0-768, y 0-768)
        shapeRenderer.setColor(0.15f, 0.30f, 0.75f, 0.55f);
        shapeRenderer.rect(SCREEN_X, SCREEN_Y, s(768), s(768));

        // Red base (top-right: cols 88-99, rows 0-11 → world x 5632-6400, y 5632-6400)
        shapeRenderer.setColor(0.75f, 0.15f, 0.15f, 0.55f);
        shapeRenderer.rect(SCREEN_X + s(5632), SCREEN_Y + s(5632), s(768), s(768));
    }

    // ---------------------------------------------------------------------------
    // Entities
    // ---------------------------------------------------------------------------
    private void drawEntities() {
        for (Entity entity : engine.getEntities()) {
            PositionComponent pos = pm.get(entity);
            if (pos == null) continue;

            float mx = SCREEN_X + pos.position.x * SCALE;
            float my = SCREEN_Y + pos.position.y * SCALE;

            TowerComponent tower = twm.get(entity);
            if (tower != null) {
                if ("blue".equals(tower.team)) shapeRenderer.setColor(0.30f, 0.60f, 1.00f, 1f);
                else                            shapeRenderer.setColor(1.00f, 0.30f, 0.30f, 1f);
                shapeRenderer.rect(mx - 2f, my - 2f, 4f, 4f);
                continue;
            }

            if (entity == heroEntity) {
                // Hero: bright white circle
                shapeRenderer.setColor(1f, 1f, 1f, 1f);
                shapeRenderer.circle(mx, my, 4f);
                continue;
            }

            AnimationComponent anim = am.get(entity);
            if (anim != null) {
                // Other animated hero/champion
                shapeRenderer.setColor(0.3f, 1f, 0.3f, 1f);
                shapeRenderer.circle(mx, my, 3f);
            } else {
                // Minion or unknown entity
                shapeRenderer.setColor(0.5f, 1f, 0.5f, 0.8f);
                shapeRenderer.circle(mx, my, 2f);
            }
        }
    }

    /** World units → minimap pixel offset */
    private static float s(float world) {
        return world * SCALE;
    }
}
