package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class BeamDown extends Beam
{
	public BeamDown(float x, float y, float speed)
	{
		super(new Rectangle(x, y, Beam.BEAM_WIDTH, Beam.BEAM_HEIGHT, Beam.COLOR), speed);

	}
	
	@Override
	protected boolean isFinished()
	{
		return (this.rectangle.getY() < Background.WALL_HEIGHT);
	}
}