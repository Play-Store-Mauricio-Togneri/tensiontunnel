package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.engine.Game;
import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.beams.BeamUp;

public class EnemyShootingBottom extends EnemyShooting
{
	public EnemyShootingBottom(Game game, float x, float beamFrequency, float beamSpeed)
	{
		super(game, x - (EnemyShooting.SIZE_EXTERNAL / 2f), Background.getHeight(), beamFrequency, beamSpeed);
	}
	
	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamUp(this.sprite.getX() + (this.sprite.getWidth() / 2f), Background.getHeight() + EnemyShooting.SIZE_EXTERNAL, beamSpeed);
	}
}