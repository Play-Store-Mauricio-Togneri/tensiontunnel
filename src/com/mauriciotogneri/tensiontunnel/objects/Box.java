package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Square;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class Box
{
	private final Square squareExternal;
	private final Square squareInternal;

	private static final int COLOR_EXTERNAL = Color.argb(255, 185, 120, 90);
	private static final int COLOR_INTERNAL = Color.argb(255, 195, 130, 100);
	
	private static final int SIZE_EXTERNAL = 3;
	private static final int SIZE_INTERNAL = 2;
	private static final float HALF_SIZE_DIFFERENCE = (Box.SIZE_EXTERNAL - Box.SIZE_INTERNAL) / 2f;

	public Box(float x, float y)
	{
		this.squareExternal = new Square(x, y, Box.SIZE_EXTERNAL, Box.COLOR_EXTERNAL);
		this.squareInternal = new Square(x + Box.HALF_SIZE_DIFFERENCE, y + Box.HALF_SIZE_DIFFERENCE, Box.SIZE_INTERNAL, Box.COLOR_INTERNAL);
	}
	
	public void update(float distance)
	{
		this.squareExternal.moveX(-distance);
		this.squareInternal.moveX(-distance);
	}
	
	public boolean isFinished()
	{
		return ((this.squareExternal.getX() + this.squareExternal.getWidth()) < 0);
	}

	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.squareExternal, player.getShape());
	}

	public boolean insideScreen()
	{
		return this.squareExternal.insideScreen(Renderer.RESOLUTION_X);
	}

	public void draw(Renderer renderer)
	{
		this.squareExternal.draw(renderer);
		this.squareInternal.draw(renderer);
	}
}