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
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jaov.moba.components.TowerComponent;
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
    private OrthographicCamera screenCamera;
    private ShapeRenderer shapeRenderer;

    private Engine engine;
    private Entity heroEntity;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private MiniMap miniMap;

    // Blue base center ≈ (350, 350) trong world coords (100x100 tiles @ 64px)
    private Vector2 targetPos = new Vector2(350, 350);

    public GameScreen(MobaGame game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, 1280, 720);

        // Load Tiled Map
        tiledMap = new TmxMapLoader().load("maps/moba_map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Khởi tạo Ashley Engine
        engine = new Engine();
        engine.addSystem(new MovementSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new RenderSystem(game.batch, shapeRenderer));

        // Tạo hero entity
        heroEntity = new Entity();
        heroEntity.add(new PositionComponent(350, 350)); // Blue base
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

        // Spawn tower entities từ TMX object layer
        MapLayer towerLayer = tiledMap.getLayers().get("Towers");
        if (towerLayer != null) {
            for (MapObject obj : towerLayer.getObjects()) {
                MapProperties props = obj.getProperties();
                String type = props.get("type", String.class);
                if (!"tower".equals(type)) continue;
                float tx = props.get("x", Float.class) + 32f;
                float ty = props.get("y", Float.class) + 32f;
                String team = props.get("team", String.class);
                String lane = props.get("lane", String.class);
                String rank = props.get("rank", String.class);
                Entity tower = new Entity();
                tower.add(new PositionComponent(tx, ty));
                tower.add(new TowerComponent(team, lane, rank));
                engine.addEntity(tower);
            }
        }

        // Tạo minion entity
        Entity minionEntity = new Entity();
        minionEntity.add(new PositionComponent(400, 300));
        minionEntity.add(new MovementComponent(100f));
        engine.addEntity(minionEntity);

        miniMap = new MiniMap(shapeRenderer, engine, heroEntity, screenCamera);
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

        mapRenderer.setView(camera);
        mapRenderer.render();

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

        // MiniMap luôn hiển thị góc trên bên trái (screen-space)
        miniMap.render();
        shapeRenderer.setProjectionMatrix(camera.combined);
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

    @Override
    public void dispose() {
        mapRenderer.dispose();
        tiledMap.dispose();
        shapeRenderer.dispose();
        engine.getSystem(RenderSystem.class).dispose();
    }
}
