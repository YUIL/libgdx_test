package com.mygdx.game.stage;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.gui.GuiFactory;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.util.ActorInputListenner;

public class StageManager {

	public static Game game;
	public static GuiFactory guiFactor = new GuiFactory();
	public static Stage superStage = new Stage(new ScreenViewport());
	public static Skin defaultSkin = new Skin(
			Gdx.files.internal("data/uiskin.json"));
	public static TextButton textButton = (TextButton) guiFactor
			.getActorByNameFromXML("data/SuperStageGui.xml", "MainMenu",
					defaultSkin);

	public static ActorInputListenner testInputListenner;
	public static void init() {
		testInputListenner=new ActorInputListenner(textButton) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				System.out.println(getActor().getName()+"'s testInput!");
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		};
		textButton.addListener(new ActorInputListenner(textButton) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				if (x < 0 
						|| x > getActor().getWidth() 
						|| y < 0
						|| y > getActor().getHeight()) {
					return;
				}
				game.setScreen(new MainMenuScreen(game));
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		});
		superStage.addActor(textButton);
	}
}
