package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.shapes.Shape;

public class Enemy
{
	private float x;
	private final float y;
	private final float width;
	private final float height;
	private Shape shape;
	
	public Enemy(float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		updateShape();
	}

	public void update(float value)
	{
		this.x -= value;
		
		if (this.x < -20)
		{
			this.x = 120;
		}
		
		updateShape();
	}
	
	private void updateShape()
	{
		this.shape = new Rectangle(this.x, this.y, this.width, this.height, Color.argb(255, 65, 65, 65));
	}
	
	public void draw(int positionLocation, int colorLocation)
	{
		if (this.shape != null)
		{
			this.shape.draw(positionLocation, colorLocation);
		}
	}
}