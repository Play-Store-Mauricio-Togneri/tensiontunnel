package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.shapes.Shape;
import com.mauriciotogneri.obstacles.shapes.Square;

public class MainCharacter
{
	private float y = 0;

	private Shape shape;

	private float acceleration = 0;

	private static final int GRAVITY = 1;
	private static final int JUMP_FORCE = 3;

	private static final int MAX_ACCELERATION_UP = 30;
	private static final int MAX_ACCELERATION_DOWN = 30;
	
	private static final int CHARACTER_SIZE = 3;
	private static final int CHARACTER_X = 30;

	private static final int CHARACTER_COLOR = Color.argb(255, 60, 170, 230);
	
	public MainCharacter(float y)
	{
		this.y = y;

		updateShape();
	}

	public void updateCharacter(float delta, Input input, int resolutionY)
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
		else if ((this.y + MainCharacter.CHARACTER_SIZE) > (resolutionY - 5))
		{
			this.y = (resolutionY - 5 - MainCharacter.CHARACTER_SIZE);
		}
		
		updateShape();
	}

	private void updateShape()
	{
		this.shape = new Square(MainCharacter.CHARACTER_X, this.y, MainCharacter.CHARACTER_SIZE, MainCharacter.CHARACTER_COLOR);
	}

	public void draw(int positionLocation, int colorLocation)
	{
		if (this.shape != null)
		{
			this.shape.draw(positionLocation, colorLocation);
		}
	}
}