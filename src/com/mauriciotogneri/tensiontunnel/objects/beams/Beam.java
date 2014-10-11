package com.mauriciotogneri.tensiontunnel.objects.beams;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public abstract class Beam
{
	protected final Rectangle rectangle;
	private final float speed;

	protected static final int COLOR = Color.argb(255, 245, 240, 125);
	protected static final int WIDTH = 1;
	protected static final int HEIGHT = Beam.WIDTH * 3;

	public Beam(Rectangle rectangle, float speed)
	{
		this.rectangle = rectangle;
		this.speed = speed;
	}
	
	public void update(float delta, float distance)
	{
		this.rectangle.moveX(-distance);
		this.rectangle.moveY(delta * this.speed);
	}

	public abstract boolean isFinished();

	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.rectangle, player.getShape());
	}
	
	public void draw(Renderer renderer)
	{
		this.rectangle.draw(renderer);
	}

	public static int getHeight()
	{
		return Beam.HEIGHT;
	}
}