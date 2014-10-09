package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.beams.BeamUp;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;

public class EnemyShootingBottom extends EnemyShooting
{
	public EnemyShootingBottom(float x, float timeLimit)
	{
		super(new Rectangle(x - (EnemyShooting.ENEMY_WIDTH / 2f), Background.WALL_HEIGHT, EnemyShooting.ENEMY_WIDTH, EnemyShooting.ENEMY_WIDTH, EnemyShooting.COLOR), timeLimit);
	}
	
	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamUp(this.rectangle.getX() + (this.rectangle.getWidth() / 2f), Background.WALL_HEIGHT + EnemyShooting.ENEMY_WIDTH, beamSpeed);
	}
}