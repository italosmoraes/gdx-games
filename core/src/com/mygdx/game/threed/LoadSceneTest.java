package com.mygdx.game.threed;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public class LoadSceneTest implements ApplicationListener {
    public PerspectiveCamera camera;
    public ModelInstance modelInstance;
    public ModelBatch modelBatch;
    public AssetManager assetManager;
    public Array<ModelInstance> modelInstances = new Array<ModelInstance>();
    public Environment environment;
    public CameraInputController cameraController;
    public boolean loading;

    public Array<ModelInstance> blocks = new Array<ModelInstance>();
    public Array<ModelInstance> invaders = new Array<ModelInstance>();
    public ModelInstance ship;
    public ModelInstance space;

    // debug and profile
    public BitmapFont textFont;
    public SpriteBatch spriteBatch;

    @Override
    public void create() {
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 7f, 10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300;
        camera.update();

        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);

        assetManager = new AssetManager();
        assetManager.load("ship.obj", Model.class);
        assetManager.load("block.obj", Model.class);
        assetManager.load("invader.obj", Model.class);
        assetManager.load("spacesphere.obj", Model.class);
        loading = true;

        // Debug/profile
        spriteBatch = new SpriteBatch();
        textFont = new BitmapFont();
        textFont.setColor(Color.WHITE);
    }

    private void doneLoading() {
        ship = new ModelInstance(assetManager.get("ship.obj", Model.class));
        ship.transform.setToRotation(Vector3.Y, 180).trn(0,0,6f);
        modelInstances.add(ship);

        Model blockModel = assetManager.get("block.obj", Model.class);
        for (float x = -5f; x <= 5f; x += 2f) {
            ModelInstance block = new ModelInstance(blockModel);
            block.transform.setToTranslation(x, 0, 3f);
            modelInstances.add(block);
            invaders.add(block);
        }

        Model invaderModel = assetManager.get("invader.obj", Model.class);
        for (float x = -5f; x <= 5f; x += 2f) {
            for (float z = -8f; z <= 0f; z += 2f) {
                ModelInstance invader = new ModelInstance(invaderModel);
                invader.transform.setToTranslation(x, 0, z);
                modelInstances.add(invader);
                invaders.add(invader);
            }
        }

        space = new ModelInstance(assetManager.get("spacesphere.obj", Model.class));

        loading = false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        if (loading && assetManager.update())
            doneLoading();
        cameraController.update();

        Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.render(modelInstances, environment);
        if (space != null)
            modelBatch.render(space);
        modelBatch.end();

        // debug/profile
        // showing the camera position
        spriteBatch.begin();
        textFont.draw(spriteBatch, "Camera position: " + cameraController.camera.position, 15, 15);
        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        modelInstances.clear();
        assetManager.dispose();
    }
}
