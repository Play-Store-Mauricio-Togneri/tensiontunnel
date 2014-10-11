package com.mauriciotogneri.tensiontunnel.objects.beams;

import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.util.Constants;

public class BeamUp extends Beam
{
	public BeamUp(float x, float y, float speed)
	{
		super(new Rectangle(x, y, Beam.BEAM_WIDTH, Beam.BEAM_HEIGHT, Beam.COLOR), speed);
	}

	@Override
	public boolean isFinished()
	{
		return ((this.rectangle.getY() + this.rectangle.getHeight()) > (Constants.Screen.RESOLUTION_Y - Background.WALL_HEIGHT));
	}
}