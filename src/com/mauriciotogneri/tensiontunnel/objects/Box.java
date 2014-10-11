package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Square;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class Box
{
	private final Square square;

	private static final int COLOR = Color.argb(255, 185, 120, 90);
	
	protected static final int SIZE = 3;

	public Box(float x, float y)
	{
		this.square = new Square(x, y, Box.SIZE, Box.COLOR);
	}
	
	public void update(float distance)
	{
		this.square.moveX(-distance);
	}
	
	public boolean isFinished()
	{
		return ((this.square.getX() + this.square.getWidth()) < 0);
	}

	public float getWidth()
	{
		return this.square.getX() + this.square.getWidth();
	}

	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.square, player.getShape());
	}

	public boolean insideScreen()
	{
		return this.square.insideScreen(Renderer.RESOLUTION_X);
	}

	public void draw(Renderer renderer)
	{
		this.square.draw(renderer);
	}
}