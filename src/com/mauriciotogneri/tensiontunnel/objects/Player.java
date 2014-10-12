package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.input.Input;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.shapes.Square;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public class Player
{
	private final Sprite shield;
	private final Sprite sprite;

	private boolean invulnerable = false;
	private float acceleration = 0;
	
	private static final int GRAVITY = 1;
	private static final int JUMP_FORCE = 3;
	
	private static final int MAX_ACCELERATION_UP = 30;
	private static final int MAX_ACCELERATION_DOWN = 30;
	
	private static final int X = 30;

	private static final int SIZE_EXTERNAL = 3;

	public Player()
	{
		this.sprite = new Sprite(Player.X, Renderer.RESOLUTION_Y / 2, Resources.Sprites.PLAYER);
		this.shield = new Sprite(Player.X - 1f, (Renderer.RESOLUTION_Y / 2) - 1f, new Square(0, 0, 5, Color.argb(255, 166, 215, 255)));
		
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
		this.shield.moveY(y);
	}

	public void setInvulnerable(boolean value)
	{
		this.invulnerable = value;
	}
	
	public void draw(Renderer renderer)
	{
		if (this.invulnerable)
		{
			this.shield.draw(renderer);
		}

		this.sprite.draw(renderer);
	}
	
	public Sprite getSprite()
	{
		return this.sprite;
	}
	
	public void reset()
	{
		this.acceleration = 0;
		this.invulnerable = false;
		
		this.sprite.set(Player.X, Renderer.RESOLUTION_Y / 2);
		this.shield.set(Player.X - 1f, (Renderer.RESOLUTION_Y / 2) - 1f);
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