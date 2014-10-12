package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class Wall
{
	private boolean passed = false;
	private final float center;
	
	private final Sprite spriteTop;
	private final Sprite spriteBottom;
	
	private static final int COLOR = Color.argb(255, 90, 110, 120);

	public Wall(float x, float center, float gap, float wallWidth)
	{
		this.center = center;
		
		float y = center + (gap / 2f);
		this.spriteTop = new Sprite(x, y, new Rectangle(wallWidth, Renderer.RESOLUTION_Y - y - Background.getHeight(), Wall.COLOR));
		
		this.spriteBottom = new Sprite(x, Background.getHeight(), new Rectangle(wallWidth, center - (gap / 2f) - Background.getHeight(), Wall.COLOR));
	}

	public void update(float distance)
	{
		this.spriteTop.moveX(-distance);
		this.spriteBottom.moveX(-distance);
	}

	public boolean isFinished()
	{
		return ((this.spriteTop.getX() + this.spriteTop.getWidth()) < 0);
	}

	public boolean isPassed()
	{
		boolean result = false;
		
		if (!this.passed)
		{
			result = getWidth() < Player.getWidth();
			
			if (result)
			{
				this.passed = true;
			}
		}

		return result;
	}
	
	public float getWidth()
	{
		return this.spriteTop.getX() + this.spriteTop.getWidth();
	}
	
	public float getCenter()
	{
		return this.center;
	}

	public boolean collide(Player player)
	{
		return ((GeometryUtils.collide(this.spriteTop, player.getSprite())) || (GeometryUtils.collide(this.spriteBottom, player.getSprite())));
	}

	public boolean insideScreen()
	{
		return this.spriteTop.insideScreen(Renderer.RESOLUTION_X);
	}
	
	public void draw(Renderer renderer)
	{
		this.spriteTop.draw(renderer);
		this.spriteBottom.draw(renderer);
	}
}