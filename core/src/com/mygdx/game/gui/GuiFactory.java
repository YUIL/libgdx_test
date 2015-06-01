package com.mygdx.game.gui;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GuiFactory {
	public Stage getStageFromXml(String xmlPath) {
		Stage stage = new Stage(new ScreenViewport());
		SAXReader reader = new SAXReader();
		reader.setEncoding("utf-8");
		try {
			Document document = reader.read(Gdx.files.internal(xmlPath).read());
			Element root = document.getRootElement();
			List<?> nodes = root.elements("actor");
			for (Iterator<?> it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				String type = elm.attributeValue("type");
				switch (type) {
				case "button":
					Drawable up = getDrawable(elm.element("up"));
					Drawable down = getDrawable(elm.element("down"));
					MyActor button = new MyActor(elm.attribute("id").getText(),up, down);
					button.setX(Float.parseFloat(elm.element("x").getText()));
					button.setY(Float.parseFloat(elm.element("y").getText()));
					stage.addActor(button);
					break;

				default:
					break;
				}
			}
			return stage;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	Drawable getDrawable(Element elm) {
		return new TextureRegionDrawable(new TextureRegion(new Texture(
				Gdx.files.internal(elm.element("path").getText())),
				Integer.parseInt(elm.element("x").getText()),
				Integer.parseInt(elm.element("y").getText()),
				Integer.parseInt(elm.element("w").getText()),
				Integer.parseInt(elm.element("h").getText())));
	}

}
