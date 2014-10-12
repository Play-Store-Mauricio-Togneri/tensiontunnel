package com.mauriciotogneri.tensiontunnel.objects;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public class Box
{
	private final Sprite sprite;
	
	public Box(float x, float y)
	{
		this.sprite = new Sprite(x, y, Resources.Sprites.BOX);
	}

	public void update(float distance)
	{
		this.sprite.moveX(-distance);
	}

	public boolean isFinished()
	{
		return ((this.sprite.getX() + this.sprite.getWidth()) < 0);
	}
	
	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.sprite, player.getSprite());
	}
	
	public boolean insideScreen()
	{
		return this.sprite.insideScreen(Renderer.RESOLUTION_X);
	}
	
	public void draw(Renderer renderer)
	{
		this.sprite.draw(renderer);
	}
}