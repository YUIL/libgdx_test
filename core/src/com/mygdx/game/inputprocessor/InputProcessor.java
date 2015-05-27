package com.mygdx.game.inputprocessor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.mygdx.game.screen.BulletTestScreen;
import com.mygdx.game.screen.FrustumCullingTestScreen;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.screen.ModelCreateScreen;
import com.mygdx.game.screen.ModelLoadScreen;
import com.mygdx.game.screen.RayPickingTestScreen;
import com.mygdx.game.screen.ShaderTest2Screen;
import com.mygdx.game.screen.ShaderTestScreen;
import com.mygdx.game.screen.ShapeTestScreen;

public class InputProcessor {

	public static void handleInput(Game game){
		if (Gdx.input.isKeyJustPressed(Keys.NUM_0)){
			//game.getScreen().dispose();
			game.setScreen(new MainMenuScreen(game));
			
		}
		else if (Gdx.input.isKeyJustPressed(Keys.NUM_1)){
			//game.getScreen().dispose();
			game.setScreen(new ModelCreateScreen(game));
			
		}
		else if (Gdx.input.isKeyJustPressed(Keys.NUM_2)){
			//game.getScreen().dispose();
			game.setScreen(new ModelLoadScreen(game));
			
		}		
		else if (Gdx.input.isKeyJustPressed(Keys.NUM_3)){
			//game.getScreen().dispose();
			game.setScreen(new ShaderTestScreen(game));
			
		}
		else if (Gdx.input.isKeyJustPressed(Keys.NUM_4)){
			//game.getScreen().dispose();
			game.setScreen(new ShaderTest2Screen(game));
			
		}
		else if (Gdx.input.isKeyJustPressed(Keys.NUM_5)){
			//game.getScreen().dispose();
			game.setScreen(new FrustumCullingTestScreen(game));
			
		}
		else if (Gdx.input.isKeyJustPressed(Keys.NUM_6)){
			//game.getScreen().dispose();
			game.setScreen(new RayPickingTestScreen(game));
			
		}
		else if (Gdx.input.isKeyJustPressed(Keys.NUM_7)){
			//game.getScreen().dispose();
			game.setScreen(new ShapeTestScreen(game));
			
		}
		else if (Gdx.input.isKeyJustPressed(Keys.NUM_8)){
			//game.getScreen().dispose();
			game.setScreen(new BulletTestScreen(game));
			
		}
		
	}
}
