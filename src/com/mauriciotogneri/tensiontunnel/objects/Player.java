package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.input.Input;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.shapes.Square;

public class Player
{
	private final Sprite sprite;
	private float acceleration = 0;

	private static final int GRAVITY = 1;
	private static final int JUMP_FORCE = 3;

	private static final int MAX_ACCELERATION_UP = 30;
	private static final int MAX_ACCELERATION_DOWN = 30;

	private static final int X = 30;
	
	private static final int COLOR_EXTERNAL = Color.argb(255, 210, 65, 50);
	private static final int COLOR_INTERNAL = Color.argb(255, 220, 75, 60);
	
	private static final int SIZE_EXTERNAL = 3;
	private static final int SIZE_INTERNAL = 2;
	private static final float HALF_SIZE_DIFFERENCE = (Player.SIZE_EXTERNAL - Player.SIZE_INTERNAL) / 2f;
	
	public Player()
	{
		Square squareExternal = new Square(Player.SIZE_EXTERNAL, Player.COLOR_EXTERNAL);
		Square squareInternal = new Square(Player.HALF_SIZE_DIFFERENCE, Player.HALF_SIZE_DIFFERENCE, Player.SIZE_INTERNAL, Player.COLOR_INTERNAL);
		
		this.sprite = new Sprite(Player.X, Renderer.RESOLUTION_Y / 2, squareExternal, squareInternal);

		reset();
	}

	public void update(float delta, Input input)
	{
		if (input.jumpPressed)
		{
			this.acceleration += Player.JUMP_FORCE;
		}

		this.acceleration -= Player.GRAVITY;

		if (this.acceleration > Player.MAX_ACCELERATION_UP)
		{
			this.acceleration = Player.MAX_ACCELERATION_UP;
		}
		else if (this.acceleration < -Player.MAX_ACCELERATION_DOWN)
		{
			this.acceleration = -Player.MAX_ACCELERATION_DOWN;
		}

		float y = delta * this.acceleration;

		this.sprite.moveY(y);
	}

	public void draw(Renderer renderer)
	{
		this.sprite.draw(renderer);
	}

	public Sprite getSprite()
	{
		return this.sprite;
	}

	public void reset()
	{
		this.acceleration = 0;

		this.sprite.set(Player.X, Renderer.RESOLUTION_Y / 2);
	}

	public static int getSize()
	{
		return Player.SIZE_EXTERNAL;
	}

	public static int getWidth()
	{
		return Player.X + Player.SIZE_EXTERNAL;
	}
}