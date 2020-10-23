package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
This is more resemblant of a structured game
With menu, different screens, scenes, scene loading
 */
public class DropsMainGameClass extends Game {
    public SpriteBatch spriteBatch;
    public BitmapFont font;

    public void create() {
        spriteBatch = new SpriteBatch();
        // defaults to arial
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
    }
}
