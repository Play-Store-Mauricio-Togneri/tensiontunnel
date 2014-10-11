package com.mauriciotogneri.tensiontunnel.objects.enemies.rotating;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class EnemyRotating
{
	private float x = 0;
	private float y = 0;
	private float angle = 0;
	private float radius = 0;
	private final Direction direction;

	private final Rectangle rectangle1External;
	private final Rectangle rectangle1Internal;

	private final Rectangle rectangle2External;
	private final Rectangle rectangle2Internal;

	private static final int COLOR_EXTERNAL = Color.argb(255, 70, 190, 255);
	private static final int COLOR_INTERNAL = Color.argb(255, 60, 180, 245);
	
	private static final int SIZE_EXTERNAL = 3;
	private static final int SIZE_INTERNAL = 2;
	private static final float HALF_SIZE_DIFFERENCE = (EnemyRotating.SIZE_EXTERNAL - EnemyRotating.SIZE_INTERNAL) / 2f;
	
	public enum Direction
	{
		SAME, OPPOSITE;
	}
	
	public EnemyRotating(float x, Direction direction)
	{
		this.x = x;
		this.y = Renderer.RESOLUTION_Y / 2f;
		this.radius = Renderer.RESOLUTION_Y / 4;
		this.direction = direction;

		this.rectangle1External = new Rectangle(x + this.radius, this.y, EnemyRotating.SIZE_EXTERNAL, EnemyRotating.SIZE_EXTERNAL, EnemyRotating.COLOR_EXTERNAL);
		this.rectangle1Internal = new Rectangle(x + this.radius, this.y, EnemyRotating.SIZE_INTERNAL, EnemyRotating.SIZE_INTERNAL, EnemyRotating.COLOR_INTERNAL);
		
		this.rectangle2External = new Rectangle(x + this.radius, this.y, EnemyRotating.SIZE_EXTERNAL, EnemyRotating.SIZE_EXTERNAL, EnemyRotating.COLOR_EXTERNAL);
		this.rectangle2Internal = new Rectangle(x + this.radius, this.y, EnemyRotating.SIZE_INTERNAL, EnemyRotating.SIZE_INTERNAL, EnemyRotating.COLOR_INTERNAL);
	}
	
	public void update(float delta, float distance, float speed)
	{
		this.x -= distance;

		this.angle += (delta * speed);
		this.angle = (this.angle % 360);

		float newX1 = ((float)Math.cos(this.angle) * this.radius) + this.x;
		float newY1 = ((float)Math.sin(this.angle) * this.radius) + this.y;
		this.rectangle1External.set(newX1, newY1);
		this.rectangle1Internal.set(newX1 + EnemyRotating.HALF_SIZE_DIFFERENCE, newY1 + EnemyRotating.HALF_SIZE_DIFFERENCE);

		float newX2 = 0;
		float newY2 = 0;
		
		if (this.direction == Direction.SAME)
		{
			newX2 = ((float)Math.cos(this.angle - 180) * this.radius) + this.x;
			newY2 = ((float)Math.sin(this.angle - 180) * this.radius) + this.y;
		}
		else if (this.direction == Direction.OPPOSITE)
		{
			newX2 = ((float)Math.cos(360 - this.angle) * this.radius) + this.x;
			newY2 = ((float)Math.sin(360 - this.angle) * this.radius) + this.y;
		}

		this.rectangle2External.set(newX2, newY2);
		this.rectangle2Internal.set(newX2 + EnemyRotating.HALF_SIZE_DIFFERENCE, newY2 + EnemyRotating.HALF_SIZE_DIFFERENCE);
	}

	public boolean isFinished()
	{
		boolean out1 = ((this.rectangle1External.getX() + this.rectangle1External.getWidth() + this.radius) < 0);
		boolean out2 = ((this.rectangle2External.getX() + this.rectangle2External.getWidth() + this.radius) < 0);
		
		return (out1 && out2);
	}
	
	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.rectangle1External, player.getShape()) || GeometryUtils.collide(this.rectangle2External, player.getShape());
	}
	
	public boolean insideScreen()
	{
		return this.rectangle1External.insideScreen(Renderer.RESOLUTION_X) || this.rectangle2External.insideScreen(Renderer.RESOLUTION_X);
	}
	
	public void draw(Renderer renderer)
	{
		this.rectangle1External.draw(renderer);
		this.rectangle1Internal.draw(renderer);
		
		this.rectangle2External.draw(renderer);
		this.rectangle2Internal.draw(renderer);
	}
}