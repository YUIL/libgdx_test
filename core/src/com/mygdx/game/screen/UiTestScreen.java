package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.inputprocessor.InputProcessor;
import com.mygdx.game.stage.StageManager;

public class UiTestScreen implements Screen{
	Game game;
	String guiXmlPath;
	Stage stage;
	Skin skin;
	BitmapFont bf;
	SpriteBatch batch;
	TextureAtlas textureAtlas;
	String str;
	public UiTestScreen(Game game){
		this.game=game;
		batch=new SpriteBatch();
		skin=StageManager.defaultSkin;
	//	StageManager.guiFactor.setStageFromXml(stage, guiXmlPath, skin);
		 textureAtlas=new TextureAtlas("data/uiskin.atlas");
		
		bf=new BitmapFont();
		
		
		JsonReader reader=new JsonReader();
		JsonValue jsonValue=reader.parse(Gdx.files.internal("data/uiskin.json"));
		str=jsonValue.get(1).name;
		//GameManager.setInputProcessor(stage);
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		batch.begin();
		bf.draw(batch, str, 100, 100);
		//batch.draw(textureAtlas.findRegion("default"),100,100);
		batch.end();
		
	
		InputProcessor.handleInput(game, delta);
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
		batch.dispose();
	}

}
