package com.mauriciotogneri.tensiontunnel.objects.enemies.rotating;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.MainCharacter;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class EnemyRotatingDouble extends EnemyRotating
{
	private final Rectangle rectangle1;
	private final Rectangle rectangle2;

	public EnemyRotatingDouble(float x)
	{
		super(x);
		
		this.rectangle1 = new Rectangle(x + this.radius, this.y, EnemyRotating.ENEMY_WIDTH, EnemyRotating.ENEMY_WIDTH, EnemyRotating.COLOR);
		this.rectangle2 = new Rectangle(x + this.radius, this.y, EnemyRotating.ENEMY_WIDTH, EnemyRotating.ENEMY_WIDTH, EnemyRotating.COLOR);
	}

	@Override
	public void update(float delta, float distance, float speed)
	{
		this.x -= distance;
		
		this.angle += (delta * speed);
		this.angle = (this.angle % 360);
		
		float newX1 = ((float)Math.cos(this.angle) * this.radius) + this.x;
		float newY1 = ((float)Math.sin(this.angle) * this.radius) + this.y;
		this.rectangle1.set(newX1, newY1);
		
		float newX2 = ((float)Math.cos(this.angle - 180) * this.radius) + this.x;
		float newY2 = ((float)Math.sin(this.angle - 180) * this.radius) + this.y;
		this.rectangle2.set(newX2, newY2);
	}
	
	@Override
	public boolean isFinished()
	{
		boolean out1 = ((this.rectangle1.getX() + this.rectangle1.getWidth() + this.radius) < 0);
		boolean out2 = ((this.rectangle2.getX() + this.rectangle2.getWidth() + this.radius) < 0);

		return (out1 && out2);
	}

	@Override
	public boolean collide(MainCharacter mainCharacter)
	{
		return GeometryUtils.collide(this.rectangle1, mainCharacter.getShape()) || GeometryUtils.collide(this.rectangle2, mainCharacter.getShape());
	}

	@Override
	public boolean insideScreen()
	{
		return this.rectangle1.insideScreen(Renderer.RESOLUTION_X) || this.rectangle2.insideScreen(Renderer.RESOLUTION_X);
	}

	@Override
	public void draw(Renderer renderer)
	{
		this.rectangle1.draw(renderer);
		this.rectangle2.draw(renderer);
	}
}