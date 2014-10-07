package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class EnemyTop extends Enemy
{
	public EnemyTop(float x, float timeLimit)
	{
		super(new Rectangle(x - (Enemy.ENEMY_WIDTH / 2f), Renderer.RESOLUTION_Y - Background.WALL_HEIGHT - Enemy.ENEMY_WIDTH, Enemy.ENEMY_WIDTH, Enemy.ENEMY_WIDTH, Enemy.COLOR), timeLimit);
	}
	
	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamDown(this.rectangle.getX() + (this.rectangle.getWidth() / 2f), Renderer.RESOLUTION_Y - Background.WALL_HEIGHT - Enemy.ENEMY_WIDTH - Beam.BEAM_HEIGHT, -beamSpeed);
	}
}