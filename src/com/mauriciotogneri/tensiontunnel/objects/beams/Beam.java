package com.mauriciotogneri.tensiontunnel.objects.beams;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public abstract class Beam
{
	protected final Sprite sprite;
	private final float speed;
	
	protected static final int COLOR = Color.argb(255, 245, 240, 125);
	protected static final int WIDTH = 1;
	protected static final int HEIGHT = Beam.WIDTH * 3;
	
	public Beam(float x, float y, float speed)
	{
		this.sprite = new Sprite(x, y, new Rectangle(Beam.WIDTH, Beam.HEIGHT, Beam.COLOR));
		this.speed = speed;
	}

	public void update(float delta, float distance)
	{
		this.sprite.moveX(-distance);
		this.sprite.moveY(delta * this.speed);
	}
	
	public abstract boolean isFinished();
	
	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.sprite, player.getSprite());
	}

	public void draw(Renderer renderer)
	{
		this.sprite.draw(renderer);
	}
	
	public static int getHeight()
	{
		return Beam.HEIGHT;
	}
}