package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.util.GeometryUtils;

public abstract class Beam
{
	protected final Rectangle rectangle;
	private final float speed;

	protected static final int COLOR = Color.argb(255, 245, 240, 125);
	protected static final int BEAM_WIDTH = 1;
	protected static final int BEAM_HEIGHT = Beam.BEAM_WIDTH * 3;

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

	protected abstract boolean isFinished();

	public boolean collide(MainCharacter mainCharacter)
	{
		return GeometryUtils.collide(this.rectangle, mainCharacter.getShape());
	}
	
	public void draw(Renderer renderer)
	{
		this.rectangle.draw(renderer);
	}
}