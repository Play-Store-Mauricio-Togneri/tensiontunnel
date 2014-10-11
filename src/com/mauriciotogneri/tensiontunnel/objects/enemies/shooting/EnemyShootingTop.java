package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.beams.BeamDown;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.util.Constants;

public class EnemyShootingTop extends EnemyShooting
{
	public EnemyShootingTop(float x, float timeLimit)
	{
		super(new Rectangle(x - (EnemyShooting.ENEMY_WIDTH / 2f), Constants.Screen.RESOLUTION_Y - Background.WALL_HEIGHT - EnemyShooting.ENEMY_WIDTH, EnemyShooting.ENEMY_WIDTH, EnemyShooting.ENEMY_WIDTH, EnemyShooting.COLOR), timeLimit);
	}

	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamDown(this.rectangle.getX() + (this.rectangle.getWidth() / 2f), Constants.Screen.RESOLUTION_Y - Background.WALL_HEIGHT - EnemyShooting.ENEMY_WIDTH - Beam.BEAM_HEIGHT, -beamSpeed);
	}
}