package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class Wall
{
	private final Rectangle rectangle;

	public Wall(float x, float y, float width, float height, int color)
	{
		this.rectangle = new Rectangle(x, y, width, height, color);
	}
	
	public void update(float value)
	{
		if ((this.rectangle.getX() + this.rectangle.getWidth()) < 0)
		{
			this.rectangle.setX(120);
		}

		this.rectangle.moveX(-value);
	}

	public void draw(Renderer renderer)
	{
		this.rectangle.draw(renderer);
	}
}