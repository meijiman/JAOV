package com.jaov.moba;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jaov.moba.components.MovementComponent;
import com.jaov.moba.components.PositionComponent;
import com.jaov.moba.components.TextureComponent;
import com.jaov.moba.systems.MovementSystem;
import com.jaov.moba.systems.RenderSystem;
import com.jaov.moba.components.AnimationComponent;
import com.jaov.moba.systems.AnimationSystem;

public class GameScreen extends ScreenAdapter {
    private MobaGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private Engine engine;
    private Entity heroEntity;

    private Vector2 targetPos = new Vector2(640, 360);

    private static final float MAP_WIDTH  = 3000f;
    private static final float MAP_HEIGHT = 3000f;

    public GameScreen(MobaGame game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        // Khởi tạo Ashley Engine
        engine = new Engine();
        engine.addSystem(new MovementSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new RenderSystem(game.batch, shapeRenderer));

        // Tạo hero entity
        heroEntity = new Entity();
        heroEntity.add(new PositionComponent(640, 360));
        heroEntity.add(new MovementComponent(200f));
        Texture[] idleFrames = {
            new Texture(Gdx.files.internal("hero_idle_0.png")),
            new Texture(Gdx.files.internal("hero_idle_1.png")),
            new Texture(Gdx.files.internal("hero_idle_2.png")),
            new Texture(Gdx.files.internal("hero_idle_3.png"))
        };
        Texture[] runFrames = {
            new Texture(Gdx.files.internal("hero_run_0.png")),
            new Texture(Gdx.files.internal("hero_run_1.png")),
            new Texture(Gdx.files.internal("hero_run_2.png")),
            new Texture(Gdx.files.internal("hero_run_3.png"))
        };
        heroEntity.add(new AnimationComponent(idleFrames, runFrames, 0.15f, 192, 192));

        engine.addEntity(heroEntity);

        // Tạo minion entity
        Entity minionEntity = new Entity();
        minionEntity.add(new PositionComponent(400, 300));
        minionEntity.add(new MovementComponent(100f));
        engine.addEntity(minionEntity);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cập nhật camera theo hero
        PositionComponent pos = heroEntity.getComponent(PositionComponent.class);
        camera.position.set(pos.position.x, pos.position.y, 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        drawMap();

        // Vẽ điểm đích
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.circle(targetPos.x, targetPos.y, 8);
        shapeRenderer.end();

        // MovementSystem chạy trước (không cần begin/end)
        // RenderSystem tự quản lý batch/shapeRenderer bên trong
        game.batch.begin();
        engine.update(delta);
        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(click);
            targetPos.set(click.x, click.y);

            // Set target cho hero entity
            MovementComponent move = heroEntity.getComponent(MovementComponent.class);
            move.target.set(click.x, click.y);
            move.moving = true;
        }
    }

    private void drawMap() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
        for (float x = 0; x <= MAP_WIDTH; x += 100)
            shapeRenderer.line(x, 0, x, MAP_HEIGHT);
        for (float y = 0; y <= MAP_HEIGHT; y += 100)
            shapeRenderer.line(0, y, MAP_WIDTH, y);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
