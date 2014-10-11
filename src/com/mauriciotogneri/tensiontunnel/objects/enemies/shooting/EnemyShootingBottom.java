package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.beams.BeamUp;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;

public class EnemyShootingBottom extends EnemyShooting
{
	public EnemyShootingBottom(float x, float timeLimit)
	{
		super(new Rectangle(x - (EnemyShooting.SIZE / 2f), Background.getHeight(), EnemyShooting.SIZE, EnemyShooting.SIZE, EnemyShooting.COLOR), timeLimit);
	}

	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamUp(this.rectangle.getX() + (this.rectangle.getWidth() / 2f), Background.getHeight() + EnemyShooting.SIZE, beamSpeed);
	}
}