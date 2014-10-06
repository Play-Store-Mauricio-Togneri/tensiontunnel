package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class BeamUp extends Beam
{
	private final int screenHeight;

	private static final int BEAM_SPEED = 50;

	public BeamUp(float x, float y, int screenHeight)
	{
		super(new Rectangle(x, y, Beam.BEAM_WIDTH, Beam.BEAM_HEIGHT, Beam.COLOR), BeamUp.BEAM_SPEED);
		
		this.screenHeight = screenHeight;
	}

	@Override
	protected boolean isFinished()
	{
		return ((this.rectangle.getY() + this.rectangle.getHeight()) > (this.screenHeight - Background.WALL_HEIGHT));
	}
}