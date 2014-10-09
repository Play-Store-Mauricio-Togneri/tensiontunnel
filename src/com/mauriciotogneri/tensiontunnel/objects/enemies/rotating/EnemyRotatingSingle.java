package com.mauriciotogneri.tensiontunnel.objects.enemies.rotating;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.MainCharacter;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class EnemyRotatingSingle extends EnemyRotating
{
	private final Rectangle rectangle;

	public EnemyRotatingSingle(float x)
	{
		super(x);
		
		this.rectangle = new Rectangle(x + this.radius, this.y, EnemyRotating.ENEMY_WIDTH, EnemyRotating.ENEMY_WIDTH, EnemyRotating.COLOR);
	}

	@Override
	public void update(float delta, float distance, float speed)
	{
		this.x -= distance;
		
		this.angle += (delta * speed);
		this.angle = (this.angle % 360);
		
		float newX = ((float)Math.cos(this.angle) * this.radius) + this.x;
		float newY = ((float)Math.sin(this.angle) * this.radius) + this.y;
		
		this.rectangle.set(newX, newY);
	}
	
	@Override
	public boolean isFinished()
	{
		return ((this.rectangle.getX() + this.rectangle.getWidth() + this.radius) < 0);
	}

	@Override
	public boolean collide(MainCharacter mainCharacter)
	{
		return GeometryUtils.collide(this.rectangle, mainCharacter.getShape());
	}

	@Override
	public boolean insideScreen()
	{
		return this.rectangle.insideScreen(Renderer.RESOLUTION_X);
	}

	@Override
	public void draw(Renderer renderer)
	{
		this.rectangle.draw(renderer);
	}
}