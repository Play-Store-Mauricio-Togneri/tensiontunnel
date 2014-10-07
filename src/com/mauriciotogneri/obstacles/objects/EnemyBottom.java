package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class EnemyBottom extends Enemy
{
	public EnemyBottom(float x, float timeLimit)
	{
		super(new Rectangle(x - (Enemy.ENEMY_WIDTH / 2f), Background.WALL_HEIGHT, Enemy.ENEMY_WIDTH, Enemy.ENEMY_WIDTH, Enemy.COLOR), timeLimit);
	}
	
	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamUp(this.rectangle.getX() + (this.rectangle.getWidth() / 2f), Background.WALL_HEIGHT + Enemy.ENEMY_WIDTH, beamSpeed);
	}
}