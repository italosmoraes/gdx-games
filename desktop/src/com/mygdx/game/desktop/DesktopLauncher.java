package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Drops;
import com.mygdx.game.DropsMainGameClass;
import com.mygdx.game.threed.Basic3DTest;
import com.mygdx.game.threed.LoadModelsTest;
import com.mygdx.game.threed.LoadSceneTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drops - the game";
		config.width = 800;
		config.height = 480;
//		new LwjglApplication(new Drops(), config);
//		new LwjglApplication(new DropsMainGameClass(), config);
//		new LwjglApplication(new Basic3DTest(), config);
//		new LwjglApplication(new LoadModelsTest(), config);
		new LwjglApplication(new LoadSceneTest(), config);
	}
}
