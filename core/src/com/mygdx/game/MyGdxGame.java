package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screen.MainMenuScreen;


public class MyGdxGame extends Game {

	@Override
	public void create(){
		this.setScreen(new MainMenuScreen(this));
	}

}
