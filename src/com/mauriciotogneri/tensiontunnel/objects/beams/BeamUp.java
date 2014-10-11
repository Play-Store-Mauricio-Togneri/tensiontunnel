package com.mauriciotogneri.tensiontunnel.objects.beams;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;

public class BeamUp extends Beam
{
	public BeamUp(float x, float y, float speed)
	{
		super(new Rectangle(x, y, Beam.WIDTH, Beam.HEIGHT, Beam.COLOR), speed);
	}

	@Override
	public boolean isFinished()
	{
		return ((this.rectangle.getY() + this.rectangle.getHeight()) > (Renderer.RESOLUTION_Y - Background.getHeight()));
	}
}