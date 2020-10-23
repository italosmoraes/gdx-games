package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import sun.security.jgss.GSSCaller;

import javax.print.DocFlavor;
import java.util.Iterator;

/*
First tutorial of the game
where all the flow, from screen setup to model loading
is done here
 */
public class Drops extends ApplicationAdapter {
	private Texture dropImage;
	private Texture bucketImage;
	private Rectangle bucket;

	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;

	private Array<Rectangle> rainDrops;
	private long lastDropTime;

	private int currentKeyPressed;
	
	@Override
	public void create () {
		dropImage = new Texture(Gdx.files.internal("drop.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop_sound.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain_sound.mp3"));

		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		spriteBatch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 /2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.width = 64;

		rainDrops = new Array<Rectangle>();
		spawnRainDrop();
	}

	private void spawnRainDrop() {
		Rectangle rainDrop = new Rectangle();
		rainDrop.x = MathUtils.random(0, 800-64);
		rainDrop.y = 480;
		rainDrop.width = 64;
		rainDrop.height = 64;
		rainDrops.add(rainDrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: rainDrops) {
			spriteBatch.draw(dropImage, raindrop.x, raindrop.y);
		}
		spriteBatch.end();

		if (Gdx.input.isTouched()) {
			Vector3 touchPosition = new Vector3();
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPosition);
			bucket.x = touchPosition.x - 64 / 2;
		}

		// process user input
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			bucket.x -= 300 * Gdx.graphics.getDeltaTime();
			currentKeyPressed = Input.Keys.LEFT;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			bucket.x += 300 * Gdx.graphics.getDeltaTime();
			currentKeyPressed = Input.Keys.RIGHT;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			if (currentKeyPressed == Input.Keys.LEFT) {
				bucket.x -= 200 * Gdx.graphics.getDeltaTime();
			}
			if (currentKeyPressed == Input.Keys.RIGHT) {
				bucket.x += 200 * Gdx.graphics.getDeltaTime();
			}
		}

		// make sure bucket stays within screen bounds
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		// define the timings to create new raindrops
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop();

		// move raindrop down, and remove from screen if bottom is reached
		for (Iterator<Rectangle> iter = rainDrops.iterator(); iter.hasNext(); ) {
			Rectangle rainDrop = iter.next();
			rainDrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(rainDrop.y + 64 < 0) iter.remove();

			if (rainDrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}
		}
	}
	
	@Override
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		spriteBatch.dispose();
	}
}
