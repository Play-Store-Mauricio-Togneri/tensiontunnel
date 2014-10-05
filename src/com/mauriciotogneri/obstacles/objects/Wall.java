package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class Wall
{
	private float x;

	private final Rectangle rectangle;

	public Wall(float x, float y, float width, float height, int color)
	{
		this.x = x;

		this.rectangle = new Rectangle(x, y, width, height, color);
	}
	
	public void update(float value)
	{
		this.x -= value;

		if (this.x < -20)
		{
			this.x = 120;
		}

		this.rectangle.setX(this.x);
	}

	public void draw(int positionLocation, int colorLocation)
	{
		if (this.rectangle != null)
		{
			this.rectangle.draw(positionLocation, colorLocation);
		}
	}
}