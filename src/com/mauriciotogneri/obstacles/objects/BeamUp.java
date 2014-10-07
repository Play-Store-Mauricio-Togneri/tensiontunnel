package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class BeamUp extends Beam
{
	public BeamUp(float x, float y, float speed)
	{
		super(new Rectangle(x, y, Beam.BEAM_WIDTH, Beam.BEAM_HEIGHT, Beam.COLOR), speed);
	}

	@Override
	protected boolean isFinished()
	{
		return ((this.rectangle.getY() + this.rectangle.getHeight()) > (Renderer.RESOLUTION_Y - Background.WALL_HEIGHT));
	}
}