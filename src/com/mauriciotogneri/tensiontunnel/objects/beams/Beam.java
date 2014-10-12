package com.mauriciotogneri.tensiontunnel.objects.beams;

import com.mauriciotogneri.tensiontunnel.engine.Process;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public abstract class Beam extends Process
{
	protected final Sprite sprite;
	private final float speed;

	protected static final int HEIGHT = 3;
	
	public Beam(float x, float y, float speed)
	{
		this.sprite = new Sprite(x, y, Resources.Sprites.BEAM);
		this.speed = speed;
	}

	@Override
	public void update(float delta, float distance, float gameSpeed)
	{
		this.sprite.moveX(-distance);
		this.sprite.moveY(delta * this.speed * gameSpeed);

		if (isFinished())
		{
			finish();
		}
	}
	
	public abstract boolean isFinished();
	
	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.sprite, player.getSprite());
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public void draw(Renderer renderer)
	{
		this.sprite.draw(renderer);
	}
	
	public static int getHeight()
	{
		return Beam.HEIGHT;
	}
}