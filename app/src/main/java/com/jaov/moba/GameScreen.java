package com.jaov.moba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen extends ScreenAdapter {
    private MobaGame game;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private Vector2 heroPos = new Vector2(640, 360); // vị trí tướng
    private Vector2 targetPos = new Vector2(640, 360); // điểm đích
    private static final float SPEED = 200f; // pixel/giây

    private static final float MAP_WIDTH = 3000f;
    private static final float MAP_HEIGHT = 3000f;
    

    public GameScreen(MobaGame game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1208, 720);
    }

    @Override
    public void render(float delta) {
        handleInput();
        update(delta);
        draw();
    }

    private void handleInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(click);
            targetPos.set(click.x, click.y);
        }
    }

    private void update(float delta) {
        Vector2 direction = new Vector2(targetPos).sub(heroPos);
        if (direction.len() > 5f) {
            direction.nor().scl(SPEED * delta);
            heroPos.add(direction);
        }

        // // Camera follow tướng
        camera.position.set(heroPos.x, heroPos.y, 0);
        camera.update();
    }

    private void draw() {
        // Xóa màn hình
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        drawMap();

        // Vẽ thử một hình tròn đại diện cho tướng
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Vẽ điểm đích (chấm trắng nhỏ)
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.circle(targetPos.x, targetPos.y, 5);

        
        // Vẽ tướng (hình tròn xanh)
        shapeRenderer.setColor(0f, 0.8f, 0.2f, 1f);
        shapeRenderer.circle(heroPos.x, heroPos.y, 30);

        shapeRenderer.end();
    }

    private void drawMap() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);

        // Vẽ lưới 100x100
        for (float x = 0; x <= MAP_WIDTH; x += 100) {
            shapeRenderer.line(x, 0, x, MAP_HEIGHT);
        }
        for (float y = 0; y <= MAP_HEIGHT; y += 100) {
            shapeRenderer.line(0, y, MAP_WIDTH, y);
        }
        
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
