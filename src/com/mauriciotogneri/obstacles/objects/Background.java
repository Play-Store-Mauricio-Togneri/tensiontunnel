package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class Background
{
	private Rectangle rect1;
	private Rectangle rect2;
	private Rectangle rect3;
	private Rectangle rect4;
	
	private float x1 = 0;
	private float x2 = 0;
	private float x3 = 0;
	private float x4 = 0;
	
	private final int height;

	private static final int WIDTH = 40;

	public Background(int height)
	{
		this.height = height;

		this.x1 = 0;
		this.x2 = Background.WIDTH;
		this.x3 = Background.WIDTH * 2;
		this.x4 = Background.WIDTH * 3;
		
		updateRectangles();
	}
	
	public void update(float value)
	{
		this.x1 -= value;
		
		if ((this.x1 + Background.WIDTH) < 0)
		{
			this.x1 += Background.WIDTH * 4;
		}
		
		this.x2 -= value;

		if ((this.x2 + Background.WIDTH) < 0)
		{
			this.x2 += Background.WIDTH * 4;
		}

		this.x3 -= value;

		if ((this.x3 + Background.WIDTH) < 0)
		{
			this.x3 += Background.WIDTH * 4;
		}

		this.x4 -= value;

		if ((this.x4 + Background.WIDTH) < 0)
		{
			this.x4 += Background.WIDTH * 4;
		}

		updateRectangles();
	}

	private void updateRectangles()
	{
		this.rect1 = new Rectangle(this.x1, 0, Background.WIDTH, this.height, Color.RED);
		this.rect2 = new Rectangle(this.x2, 0, Background.WIDTH, this.height, Color.BLUE);
		this.rect3 = new Rectangle(this.x3, 0, Background.WIDTH, this.height, Color.GREEN);
		this.rect4 = new Rectangle(this.x4, 0, Background.WIDTH, this.height, Color.MAGENTA);
	}
	
	public void draw(int positionLocation, int colorLocation)
	{
		this.rect1.draw(positionLocation, colorLocation);
		this.rect2.draw(positionLocation, colorLocation);
		this.rect3.draw(positionLocation, colorLocation);
		this.rect4.draw(positionLocation, colorLocation);
	}
}