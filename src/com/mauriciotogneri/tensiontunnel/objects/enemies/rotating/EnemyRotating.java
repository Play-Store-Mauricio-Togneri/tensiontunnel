package com.mauriciotogneri.tensiontunnel.objects.enemies.rotating;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.MainCharacter;

public abstract class EnemyRotating
{
	protected float x = 0;
	protected float y = 0;
	protected float angle = 0;
	protected float radius = 0;
	
	protected static final int COLOR = Color.argb(255, 255, 0, 255);
	protected static final int ENEMY_WIDTH = 3;

	public EnemyRotating(float x)
	{
		this.x = x;
		this.y = Renderer.RESOLUTION_Y / 2f;
		this.radius = Renderer.RESOLUTION_Y / 4;
	}

	public abstract void update(float delta, float distance, float speed);
	
	public abstract boolean isFinished();

	public abstract boolean collide(MainCharacter mainCharacter);

	public abstract boolean insideScreen();

	public abstract void draw(Renderer renderer);
}