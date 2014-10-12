package com.mauriciotogneri.tensiontunnel.objects;

import com.mauriciotogneri.tensiontunnel.engine.Process;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public class Box extends Process
{
	private final Sprite sprite;

	public Box(float x, float y)
	{
		this.sprite = new Sprite(x, y, Resources.Sprites.BOX);
	}
	
	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.sprite, player.getSprite());
	}

	@Override
	public void update(float delta, float distance)
	{
		this.sprite.moveX(-distance);
		
		if (this.sprite.getRight() < 0)
		{
			finish();
		}
	}

	@Override
	public boolean isVisible()
	{
		return this.sprite.insideScreen(Renderer.RESOLUTION_X);
	}

	@Override
	public void draw(Renderer renderer)
	{
		this.sprite.draw(renderer);
	}
}