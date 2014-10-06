package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.util.GeometryUtils;

public class Wall
{
	private final Rectangle rectangleTop;
	private final Rectangle rectangleBottom;

	private static final int COLOR = Color.argb(255, 90, 110, 120);
	
	public Wall(float x, float center, float gap, float wallWidth, float screenHeight)
	{
		float y = center + (gap / 2f);
		this.rectangleTop = new Rectangle(x, y, wallWidth, screenHeight - y, Wall.COLOR);

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

	public float getWidth()
	{
		return this.rectangleTop.getX() + this.rectangleTop.getWidth();
	}
	
	public boolean collide(MainCharacter mainCharacter)
	{
		return ((GeometryUtils.collide(this.rectangleTop, mainCharacter.getShape())) || (GeometryUtils.collide(this.rectangleBottom, mainCharacter.getShape())));
	}
	
	public boolean insideScreen(int width)
	{
		return this.rectangleTop.insideScreen(width);
	}

	public void draw(Renderer renderer)
	{
		this.rectangleTop.draw(renderer);
		this.rectangleBottom.draw(renderer);
	}
}