package com.mauriciotogneri.tensiontunnel.objects.enemies.rotating;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.MainCharacter;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class EnemyRotatingSingle
{
	private float x = 0;
	private float y = 0;
	private float angle = 0;
	private float radius = 0;
	private final Rectangle rectangle;
	
	private static final int COLOR = Color.argb(255, 255, 0, 255);
	private static final int ENEMY_WIDTH = 3;

	public EnemyRotatingSingle(float x)
	{
		this.x = x;
		this.y = Renderer.RESOLUTION_Y / 2f;
		this.radius = Renderer.RESOLUTION_Y / 4;
		
		this.rectangle = new Rectangle(x + this.radius, this.y, EnemyRotating.ENEMY_WIDTH, EnemyRotating.ENEMY_WIDTH, EnemyRotating.COLOR);
	}

	public void update(float delta, float distance, float speed)
	{
		this.x -= distance;
		
		this.angle += (delta * speed);
		this.angle = (this.angle % 360);
		
		float newX = ((float)Math.cos(this.angle) * this.radius) + this.x;
		float newY = ((float)Math.sin(this.angle) * this.radius) + this.y;
		
		this.rectangle.set(newX, newY);
	}
	
	public boolean isFinished()
	{
		return ((this.rectangle.getX() + this.rectangle.getWidth() + this.radius) < 0);
	}

	public boolean collide(MainCharacter mainCharacter)
	{
		return GeometryUtils.collide(this.rectangle, mainCharacter.getShape());
	}

	public boolean insideScreen()
	{
		return this.rectangle.insideScreen(Renderer.RESOLUTION_X);
	}

	public void draw(Renderer renderer)
	{
		this.rectangle.draw(renderer);
	}
}