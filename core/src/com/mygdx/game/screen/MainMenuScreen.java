package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.inputprocessor.InputProcessor;

public class MainMenuScreen implements Screen {
	Game game;
	SpriteBatch batch;
	BitmapFont font;
	
	int topSpacing=50;
	int lineSpacing=30;
	
	
	public MainMenuScreen(Game game){
		this.game=game;
		batch=new SpriteBatch();
		font=new BitmapFont();
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		batch.begin();
		font.draw(batch, "Press 1 to open ModelCreateScreen", 100, Gdx.graphics.getHeight()-topSpacing-lineSpacing);
		font.draw(batch, "Press 2 to open ModelLoadScreen", 100, Gdx.graphics.getHeight()-topSpacing-lineSpacing*2);
		font.draw(batch, "Press 3 to open ShaderTestScreen", 100, Gdx.graphics.getHeight()-topSpacing-lineSpacing*3);
		font.draw(batch, "Press 4 to open ShaderTest2Screen", 100, Gdx.graphics.getHeight()-topSpacing-lineSpacing*4);
		font.draw(batch, "Press 5 to open FrustumCullingTestScreen", 100, Gdx.graphics.getHeight()-topSpacing-lineSpacing*5);
		font.draw(batch, "Press 6 to open RayPickingTestScreen", 100, Gdx.graphics.getHeight()-topSpacing-lineSpacing*6);
		font.draw(batch, "Press 7 to open ShapeTestScreen", 100, Gdx.graphics.getHeight()-topSpacing-lineSpacing*7);
		font.draw(batch, "Press 8 to open BulletTestScreen", 100, Gdx.graphics.getHeight()-topSpacing-lineSpacing*8);
		batch.end();
		
		InputProcessor.handleInput(game);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

		font.dispose();
		batch.dispose();
	}

}
