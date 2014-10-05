package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.shapes.Shape;

public abstract class Enemy
{
	protected float x;
	protected final float y;
	protected final float width;
	protected final float height;
	protected final int color;
	
	private Shape shape;
	
	public Enemy(float x, float y, float width, float height, int color)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		
		updateShape();
	}

	public void update(float value)
	{
	}
	
	protected void updateShape()
	{
		this.shape = new Rectangle(this.x, this.y, this.width, this.height, this.color);
	}
	
	public void draw(int positionLocation, int colorLocation)
	{
		if (this.shape != null)
		{
			this.shape.draw(positionLocation, colorLocation);
		}
	}
}