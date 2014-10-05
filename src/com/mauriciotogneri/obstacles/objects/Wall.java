package com.mauriciotogneri.obstacles.objects;

public class Wall extends Enemy
{
	public Wall(float x, float y, float width, float height, int color)
	{
		super(x, y, width, height, color);
	}
	
	@Override
	public void update(float value)
	{
		this.x -= value;
		
		if (this.x < -20)
		{
			this.x = 120;
		}
		
		updateShape();
	}
}