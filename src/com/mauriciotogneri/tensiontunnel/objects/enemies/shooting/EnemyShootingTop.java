package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.beams.BeamDown;

public class EnemyShootingTop extends EnemyShooting
{
	public EnemyShootingTop(float x, float timeLimit)
	{
		super(x - (EnemyShooting.SIZE_EXTERNAL / 2f), Renderer.RESOLUTION_Y - Background.getHeight() - EnemyShooting.SIZE_EXTERNAL, timeLimit);
	}
	
	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamDown(this.squareExternal.getX() + (this.squareExternal.getWidth() / 2f), Renderer.RESOLUTION_Y - Background.getHeight() - EnemyShooting.SIZE_EXTERNAL - Beam.getHeight(), -beamSpeed);
	}
}