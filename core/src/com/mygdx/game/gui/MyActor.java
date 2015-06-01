package com.mygdx.game.gui;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MyActor extends Button {
	String id;
	TextureRegion region;
	public MyActor(String id,Drawable up,Drawable down){
		super(up,down);
		this.id=id;
		
	}
	public MyActor(Drawable up,Drawable down){
		super(up,down);
	}
	
	
/*	@Override
	public void draw(Batch batch,float parentAlpha){
		
		Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	@Override
	public Actor hit (float x, float y, boolean touchable) {
	    if (touchable && getTouchable() != Touchable.enabled) return null;
	    return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? this : null;
	}*/
}
