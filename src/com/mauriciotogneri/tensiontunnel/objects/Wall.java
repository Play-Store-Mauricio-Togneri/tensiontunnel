package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class Wall
{
	private boolean passed = false;
	private final float center;
	private final Rectangle rectangleTop;
	private final Rectangle rectangleBottom;
	
	private static final int COLOR = Color.argb(255, 90, 110, 120);

	public Wall(float x, float center, float gap, float wallWidth)
	{
		this.center = center;
		
		float y = center + (gap / 2f);
		this.rectangleTop = new Rectangle(x, y, wallWidth, Renderer.RESOLUTION_Y - y, Wall.COLOR);
		
		this.rectangleBottom = new Rectangle(x, 0, wallWidth, center - (gap / 2f), Wall.COLOR);
	}

	public void update(float distance)
	{
		this.rectangleTop.moveX(-distance);
		this.rectangleBottom.moveX(-distance);
	}

	public boolean isFinished()
	{
		return ((this.rectangleTop.getX() + this.rectangleTop.getWidth()) < 0);
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
		return this.rectangleTop.getX() + this.rectangleTop.getWidth();
	}
	
	public float getCenter()
	{
		return this.center;
	}

	public boolean collide(Player player)
	{
		return ((GeometryUtils.collide(this.rectangleTop, player.getShape())) || (GeometryUtils.collide(this.rectangleBottom, player.getShape())));
	}

	public boolean insideScreen()
	{
		return this.rectangleTop.insideScreen(Renderer.RESOLUTION_X);
	}
	
	public void draw(Renderer renderer)
	{
		this.rectangleTop.draw(renderer);
		this.rectangleBottom.draw(renderer);
	}
}