package com.mauriciotogneri.tensiontunnel.objects.beams;

import com.mauriciotogneri.tensiontunnel.objects.Background;

public class BeamDown extends Beam
{
	public BeamDown(float x, float y, float speed)
	{
		super(x, y, speed);
	}
	
	@Override
	public boolean isFinished()
	{
		return (this.rectangle.getY() < Background.getHeight());
	}
}