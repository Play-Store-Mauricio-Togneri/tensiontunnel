package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.shapes.Square;

public class MainCharacter
{
	private float y = 0;

	private float acceleration = 0;

	private static final int GRAVITY = 2;
	private static final int JUMP_FORCE = 5;
	private static final int MAX_ACCELERATION_UP = 50;
	private static final int MAX_ACCELERATION_DOWN = 50;
	
	public MainCharacter(float y)
	{
		this.y = y;
	}

	public Square updateCharacter(float delta, Input input, int resolutionY)
	{
		if (input.jumpPressed)
		{
			this.acceleration += MainCharacter.JUMP_FORCE;
		}
		
		this.acceleration -= MainCharacter.GRAVITY;
		
		if (this.acceleration > MainCharacter.MAX_ACCELERATION_UP)
		{
			this.acceleration = MainCharacter.MAX_ACCELERATION_UP;
		}
		else if (this.acceleration < -MainCharacter.MAX_ACCELERATION_DOWN)
		{
			this.acceleration = -MainCharacter.MAX_ACCELERATION_DOWN;
		}
		
		this.y += delta * this.acceleration;
		
		if (this.y < 5)
		{
			this.y = 5;
		}
		else if ((this.y + 5) > (resolutionY - 5))
		{
			this.y = (resolutionY - 10);
		}
		
		return new Square(20, this.y, 5, Color.YELLOW);
	}
}