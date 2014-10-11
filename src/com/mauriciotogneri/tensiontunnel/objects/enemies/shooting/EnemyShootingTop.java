package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.beams.BeamDown;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;

public class EnemyShootingTop extends EnemyShooting
{
	public EnemyShootingTop(float x, float timeLimit)
	{
		super(new Rectangle(x - (EnemyShooting.SIZE / 2f), Renderer.RESOLUTION_Y - Background.getHeight() - EnemyShooting.SIZE, EnemyShooting.SIZE, EnemyShooting.SIZE, EnemyShooting.COLOR), timeLimit);
	}

	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamDown(this.rectangle.getX() + (this.rectangle.getWidth() / 2f), Renderer.RESOLUTION_Y - Background.getHeight() - EnemyShooting.SIZE - Beam.getHeight(), -beamSpeed);
	}
}