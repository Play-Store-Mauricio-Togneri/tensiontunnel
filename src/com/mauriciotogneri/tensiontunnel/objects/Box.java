package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.shapes.Square;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class Box
{
	private final Sprite sprite;
	
	private static final int COLOR_EXTERNAL = Color.argb(255, 185, 120, 90);
	private static final int COLOR_INTERNAL = Color.argb(255, 195, 130, 100);

	private static final int SIZE_EXTERNAL = 3;
	private static final int SIZE_INTERNAL = 2;
	private static final float HALF_SIZE_DIFFERENCE = (Box.SIZE_EXTERNAL - Box.SIZE_INTERNAL) / 2f;
	
	public Box(float x, float y)
	{
		Square squareExternal = new Square(Box.SIZE_EXTERNAL, Box.COLOR_EXTERNAL);
		Square squareInternal = new Square(Box.HALF_SIZE_DIFFERENCE, Box.HALF_SIZE_DIFFERENCE, Box.SIZE_INTERNAL, Box.COLOR_INTERNAL);

		this.sprite = new Sprite(x, y, squareExternal, squareInternal);
	}

	public void update(float distance)
	{
		this.sprite.moveX(-distance);
	}

	public boolean isFinished()
	{
		return ((this.sprite.getX() + this.sprite.getWidth()) < 0);
	}
	
	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.sprite, player.getSprite());
	}
	
	public boolean insideScreen()
	{
		return this.sprite.insideScreen(Renderer.RESOLUTION_X);
	}
	
	public void draw(Renderer renderer)
	{
		this.sprite.draw(renderer);
	}
}