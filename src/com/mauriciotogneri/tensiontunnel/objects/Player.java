package com.mauriciotogneri.tensiontunnel.objects;

import com.mauriciotogneri.tensiontunnel.engine.Process;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.input.Input;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public class Player
{
	private final Sprite shield;
	private final Sprite spriteNormal;
	private final Sprite spriteHeavy;

	private boolean heavier = false;
	private boolean invulnerable = false;
	private float acceleration = 0;
	
	private static final int GRAVITY = 1;
	private static final float EXTRA_GRAVITY = 0.15f;
	private static final int JUMP_FORCE = 3;
	
	private static final int MAX_ACCELERATION_UP = 30;
	private static final int MAX_ACCELERATION_DOWN = 30;
	
	private static final int X = 30;

	private static final int MAX_SIZE = 5;

	public Player()
	{
		this.spriteNormal = new Sprite(0, 0, Resources.Sprites.PLAYER_NORMAL);
		this.spriteHeavy = new Sprite(0, 0, Resources.Sprites.PLAYER_HEAVY);
		this.shield = new Sprite(0, 0, Resources.Sprites.SHIELD);
		
		reset();
	}
	
	public void update(float delta, Input input, boolean fast)
	{
		if (input.jumpPressed)
		{
			this.acceleration += Player.JUMP_FORCE;
		}
		
		if (fast)
		{
			Thrust thrust = new Thrust(Player.X, this.spriteNormal.getY());
			thrust.start();
		}
		
		this.acceleration -= Player.GRAVITY;

		if (this.heavier)
		{
			this.acceleration -= Player.EXTRA_GRAVITY;
		}
		
		if (this.acceleration > Player.MAX_ACCELERATION_UP)
		{
			this.acceleration = Player.MAX_ACCELERATION_UP;
		}
		else if (this.acceleration < -Player.MAX_ACCELERATION_DOWN)
		{
			this.acceleration = -Player.MAX_ACCELERATION_DOWN;
		}
		
		float y = delta * this.acceleration;
		
		this.spriteNormal.moveY(y);
		this.spriteHeavy.moveY(y);
		this.shield.moveY(y);
	}

	public void setInvulnerable(boolean value)
	{
		this.invulnerable = value;
	}
	
	public void setHeavier(boolean value)
	{
		this.heavier = value;
	}
	
	public void draw(Renderer renderer)
	{
		if (this.heavier)
		{
			this.spriteHeavy.draw(renderer);
		}
		else
		{
			this.spriteNormal.draw(renderer);
		}

		if (this.invulnerable)
		{
			this.shield.draw(renderer, 0.6f);
		}
	}
	
	public Sprite getSprite()
	{
		return (this.heavier ? this.spriteHeavy : this.spriteNormal);
	}
	
	public void reset()
	{
		this.acceleration = 0;
		this.heavier = false;
		this.invulnerable = false;
		
		this.spriteNormal.set(Player.X, Renderer.RESOLUTION_Y / 2);
		this.spriteHeavy.set(Player.X - 1, (Renderer.RESOLUTION_Y / 2) - 1);
		this.shield.set(Player.X - 1f, (Renderer.RESOLUTION_Y / 2) - 1f);
	}
	
	public static int getMaxSize()
	{
		return Player.MAX_SIZE;
	}
	
	public static int getX()
	{
		return Player.X;
	}

	private class Thrust extends Process
	{
		private float factor = 1f;
		private final Sprite sprite1;
		
		public Thrust(float x, float y)
		{
			this.sprite1 = new Sprite(x, y, Resources.Sprites.THRUST);
		}

		@Override
		public void update(float delta, float distance, float gameSpeed)
		{
			this.factor -= (delta * 2);
			
			if (this.factor <= 0)
			{
				finish();
			}

			this.sprite1.moveX(-distance);
		}
		
		@Override
		public boolean isVisible()
		{
			return true;
		}
		
		@Override
		public void draw(Renderer renderer)
		{
			this.sprite1.draw(renderer, this.factor, this.factor, this.factor);
		}
	}
}