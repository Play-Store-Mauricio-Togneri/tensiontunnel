package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.util.GeometryUtils;

public class Beam
{
	private final Rectangle rectangle;
	private final int screenHeight;
	
	private static final int COLOR = Color.argb(255, 245, 240, 125);
	private static final int BEAM_WIDTH = 1;
	private static final int BEAM_HEIGHT = Beam.BEAM_WIDTH * 3;
	
	public Beam(float x, float y, int screenHeight)
	{
		this.screenHeight = screenHeight;
		this.rectangle = new Rectangle(x, y, Beam.BEAM_WIDTH, Beam.BEAM_HEIGHT, Beam.COLOR);
	}

	public void update(float delta, float distance)
	{
		this.rectangle.moveX(-distance);
		this.rectangle.moveY(delta * 50);
	}
	
	public boolean isFinished()
	{
		return ((this.rectangle.getY() + this.rectangle.getHeight()) > (this.screenHeight - Background.WALL_HEIGHT));
	}
	
	public boolean collide(MainCharacter mainCharacter)
	{
		return GeometryUtils.collide(this.rectangle, mainCharacter.getShape());
	}

	public void draw(Renderer renderer)
	{
		this.rectangle.draw(renderer);
	}
}