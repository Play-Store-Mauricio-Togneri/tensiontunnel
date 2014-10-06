package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class EnemyBottom extends Enemy
{
	private final int screenHeight;

	public EnemyBottom(float x, int screenWidth, int screenHeight, float timeLimit)
	{
		super(new Rectangle(x - (Enemy.ENEMY_WIDTH / 2f), Background.WALL_HEIGHT, Enemy.ENEMY_WIDTH, Enemy.ENEMY_WIDTH, Enemy.COLOR), screenWidth, timeLimit);
		
		this.screenHeight = screenHeight;
	}

	@Override
	protected Beam getNewBeam(float beamSpeed)
	{
		return new BeamUp(this.rectangle.getX() + (this.rectangle.getWidth() / 2f), Background.WALL_HEIGHT + Enemy.ENEMY_WIDTH, this.screenHeight, beamSpeed);
	}
}