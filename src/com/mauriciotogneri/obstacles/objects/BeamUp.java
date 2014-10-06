package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class BeamUp extends Beam
{
	private final int screenHeight;
	
	public BeamUp(float x, float y, int screenHeight, float speed)
	{
		super(new Rectangle(x, y, Beam.BEAM_WIDTH, Beam.BEAM_HEIGHT, Beam.COLOR), speed);

		this.screenHeight = screenHeight;
	}
	
	@Override
	protected boolean isFinished()
	{
		return ((this.rectangle.getY() + this.rectangle.getHeight()) > (this.screenHeight - Background.WALL_HEIGHT));
	}
}