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
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.utils.Array;


public class LoadModelsTest implements ApplicationListener {
    public PerspectiveCamera camera;
    public Model model;
    public ModelInstance modelInstance;
    public ModelBatch modelBatch;
    public AssetManager assetManager;
    public Array<ModelInstance> modelInstances = new Array<ModelInstance>();
    public Environment environment;
    public CameraInputController cameraController;
    public boolean loading;

    @Override
    public void create() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300;
        camera.update();


//        ModelLoader modelLoader = new ObjLoader();
//        model = modelLoader.loadModel(Gdx.files.internal("ship.obj")); // gets assets from the android/assets for whatever reason
//        modelInstance = new ModelInstance(model);

        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);

        assetManager = new AssetManager();
        assetManager.load("ship.obj", Model.class);
        assetManager.load("ship.g3db", Model.class); // fbx file converted to libgdx model suitable for release
        loading = true;
    }

    private void doneLoading() {
        Model ship = assetManager.get("ship.obj", Model.class);
        Model motherShip = assetManager.get("ship.g3db", Model.class);
        for (float x = -5f; x<= 5f; x+= 2f) {
            for (float z = -5f; z <= 5f; z+=2f) {
                ModelInstance shipInstance = new ModelInstance(ship);
                shipInstance.transform.setToTranslation(x, 0, z);
                modelInstances.add(shipInstance);
            }
        }
        ModelInstance motherShipInstance = new ModelInstance(motherShip);
        motherShipInstance.transform.setToTranslation(2.5f, 5, 2.5f);
        modelInstances.add(motherShipInstance);

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
        modelBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        model.dispose();
        modelBatch.dispose();
        modelInstances.clear();
    }
}
