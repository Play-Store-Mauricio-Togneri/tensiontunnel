package com.mauriciotogneri.tensiontunnel.objects.enemies.rotating;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.shapes.Square;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class EnemyRotating
{
	private float x = 0;
	private float y = 0;
	private float angle = 0;
	private float radius = 0;
	private final Direction direction;

	private final Sprite rectangle1;
	private final Sprite rectangle2;
	
	private static final int COLOR_EXTERNAL = Color.argb(255, 60, 180, 245);
	private static final int COLOR_INTERNAL = Color.argb(255, 70, 190, 255);

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

		Square square1External = new Square(EnemyRotating.SIZE_EXTERNAL, EnemyRotating.COLOR_EXTERNAL);
		Square square1Internal = new Square(EnemyRotating.HALF_SIZE_DIFFERENCE, EnemyRotating.HALF_SIZE_DIFFERENCE, EnemyRotating.SIZE_INTERNAL, EnemyRotating.COLOR_INTERNAL);
		this.rectangle1 = new Sprite(x + this.radius, this.y, square1External, square1Internal);

		Square square2External = new Square(EnemyRotating.SIZE_EXTERNAL, EnemyRotating.COLOR_EXTERNAL);
		Square square2Internal = new Square(EnemyRotating.HALF_SIZE_DIFFERENCE, EnemyRotating.HALF_SIZE_DIFFERENCE, EnemyRotating.SIZE_INTERNAL, EnemyRotating.COLOR_INTERNAL);
		this.rectangle2 = new Sprite(x + this.radius, this.y, square2External, square2Internal);
	}
	
	public void update(float delta, float distance, float speed)
	{
		this.x -= distance;

		this.angle += (delta * speed);
		this.angle = (this.angle % 360);

		float newX1 = ((float)Math.cos(this.angle) * this.radius) + this.x;
		float newY1 = ((float)Math.sin(this.angle) * this.radius) + this.y;
		this.rectangle1.set(newX1, newY1);

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

		this.rectangle2.set(newX2, newY2);
	}

	public boolean isFinished()
	{
		boolean out1 = ((this.rectangle1.getX() + this.rectangle1.getWidth() + this.radius) < 0);
		boolean out2 = ((this.rectangle2.getX() + this.rectangle2.getWidth() + this.radius) < 0);
		
		return (out1 && out2);
	}
	
	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.rectangle1, player.getSprite()) || GeometryUtils.collide(this.rectangle2, player.getSprite());
	}
	
	public boolean insideScreen()
	{
		return this.rectangle1.insideScreen(Renderer.RESOLUTION_X) || this.rectangle2.insideScreen(Renderer.RESOLUTION_X);
	}
	
	public void draw(Renderer renderer)
	{
		this.rectangle1.draw(renderer);
		this.rectangle2.draw(renderer);
	}
}