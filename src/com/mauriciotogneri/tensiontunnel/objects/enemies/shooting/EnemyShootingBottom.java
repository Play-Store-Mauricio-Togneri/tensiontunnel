package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.beams.BeamUp;

public class EnemyShootingBottom extends EnemyShooting
{
	public EnemyShootingBottom(float x, float timeLimit)
	{
		super(x - (EnemyShooting.SIZE_EXTERNAL / 2f), Background.getHeight(), timeLimit);
	}
	
	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamUp(this.squareExternal.getX() + (this.squareExternal.getWidth() / 2f), Background.getHeight() + EnemyShooting.SIZE_EXTERNAL, beamSpeed);
	}
}