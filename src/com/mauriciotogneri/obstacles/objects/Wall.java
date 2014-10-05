package com.mauriciotogneri.obstacles.objects;

import java.util.Random;
import android.graphics.Color;
import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class Wall
{
	private final Rectangle rectangle;

	private static final int COLOR = Color.argb(255, 90, 110, 120);

	public Wall(float x, float y, float width, float height)
	{
		Random random = new Random();
		this.rectangle = new Rectangle(x, y, width, height, Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
	}
	
	public void update(float value)
	{
		this.rectangle.moveX(-value);
	}
	
	public boolean isFinished()
	{
		return ((this.rectangle.getX() + this.rectangle.getWidth()) < 0);
	}
	
	public Rectangle getRectangle()
	{
		return this.rectangle;
	}

	public boolean insideScreen(int width)
	{
		return this.rectangle.insideScreen(width);
	}

	public void draw(Renderer renderer)
	{
		this.rectangle.draw(renderer);
	}
}