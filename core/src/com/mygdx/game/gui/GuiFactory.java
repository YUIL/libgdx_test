package com.mygdx.game.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GuiFactory {
	XmlReader reader;

	public GuiFactory() {
		reader = new XmlReader();

	}

	/** 从XML中解析一个Stage对象 */
	public Stage getStageFromXml(String guiXmlPath, Skin skin) {
		Stage stage = new Stage(new ScreenViewport());
		setStageFromXml(stage, guiXmlPath, skin);
		return stage;
	}

	/** 从XML中读取并配置一个Stage对象 */
	public void setStageFromXml(Stage stage, String guiXmlPath, Skin skin) {

		Element root = null;
		try {
			root = reader.parse(Gdx.files.internal(guiXmlPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Array<Element> nodes = root.getChildrenByName("actor");
		for (Iterator<?> it = nodes.iterator(); it.hasNext();) {
			Element actorElm = (Element) it.next();
			String type = actorElm.getAttribute("type");
			Actor actor = null;
			if(type.equals( "button")){
				actor = getButtonFromElm(actorElm, skin);
				if (actor != null) {
					stage.addActor(actor);
				}
			}
			if (type.equals("textButton")){
				actor = getTextButtonFromElm(actorElm, skin);
				if (actor != null) {
					stage.addActor(actor);
				}
			}
		}

	}

	/** 根据Name从XML中得到一个Actor */
	public Actor getActorByNameFromXML(String guiXmlPath, String name, Skin skin) {

		Element root = null;
		try {
			root = reader.parse(Gdx.files.internal(guiXmlPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Array<?> nodes = root.getChildrenByName("actor");
		for (Iterator<?> it = nodes.iterator(); it.hasNext();) {
			Element actorElm = (Element) it.next();
			String name2 = actorElm.getAttribute("name");
			if (name.equals(name2)) {
				String type = actorElm.getAttribute("type");
				if(type.equals( "button")){
					return getButtonFromElm(actorElm, skin);
				}
				if(type.equals("textButton")){
					return getTextButtonFromElm(actorElm, skin);
				}
			}
		}
		return null;
	}

	/** 从Element中解析一个Drawble对象 */
	protected Drawable getDrawable(Element elm) {
		return new TextureRegionDrawable(new TextureRegion(new Texture(
				Gdx.files.internal(elm.getChildByName("path").getText())),
				Integer.parseInt(elm.getChildByName("x").getText()),
				Integer.parseInt(elm.getChildByName("y").getText()),
				Integer.parseInt(elm.getChildByName("w").getText()),
				Integer.parseInt(elm.getChildByName("h").getText())));
	}

	/** 从Element中读取并设置actor的一般属性 */
	protected void setActorAttribute(Actor actor, Element actorElm) {
		actor.setX(Float.parseFloat(actorElm.getChildByName("x").getText()));
		actor.setY(Float.parseFloat(actorElm.getChildByName("y").getText()));
		actor.setName(actorElm.getAttribute("name"));
	}

	/** 从Element中解析一个Button对象 */
	private Button getButtonFromElm(Element actorElm, Skin skin) {

		Button button;
		Element skinElm = actorElm.getChildByName("skin");
		if (skinElm != null) {
			button = new Button(skin.get(skinElm.getAttribute("name"),
					ButtonStyle.class));
		} else {
			Drawable up = getDrawable(actorElm.getChildByName("up"));
			Drawable down = getDrawable(actorElm.getChildByName("down"));
			button = new Button(up, down);
		}

		setActorAttribute(button, actorElm);
		return button;
	}

	/** 从Element中解析一个TextButton对象 */
	private TextButton getTextButtonFromElm(Element actorElm, Skin skin) {

		TextButton textButton;
		Element skinElm = actorElm.getChildByName("skin");
		if (skinElm != null) {
			textButton = new TextButton(actorElm.getAttribute("name"),
					skin.get(skinElm.getAttribute("name"),
							TextButtonStyle.class));
			setActorAttribute(textButton, actorElm);
			return textButton;
		} else {
			System.err.println("不能缺少skin元素！");
			return null;
		}

	}
}
