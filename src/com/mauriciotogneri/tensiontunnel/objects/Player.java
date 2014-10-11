package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.input.Input;
import com.mauriciotogneri.tensiontunnel.shapes.Square;

public class Player
{
	private Square square;
	private float acceleration = 0;
	
	private static final int GRAVITY = 1;
	private static final int JUMP_FORCE = 3;
	
	private static final int MAX_ACCELERATION_UP = 30;
	private static final int MAX_ACCELERATION_DOWN = 30;
	
	private static final int SIZE = 3;
	private static final int X = 30;
	private static final int COLOR = Color.argb(255, 220, 75, 60);
	
	public Player()
	{
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
		
		this.square.moveY(y);
	}
	
	public void draw(Renderer renderer)
	{
		this.square.draw(renderer);
	}
	
	public Square getShape()
	{
		return this.square;
	}
	
	public void reset()
	{
		this.acceleration = 0;
		this.square = new Square(Player.X, Renderer.RESOLUTION_Y / 2, Player.SIZE, Player.COLOR);
	}
	
	public static int getSize()
	{
		return Player.SIZE;
	}
}