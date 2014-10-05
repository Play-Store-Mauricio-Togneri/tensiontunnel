package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.shapes.Square;

public class MainCharacter
{
	private final Square square;

	private float acceleration = 0;

	private static final int GRAVITY = 1;
	private static final int JUMP_FORCE = 3;

	private static final int MAX_ACCELERATION_UP = 30;
	private static final int MAX_ACCELERATION_DOWN = 30;
	
	private static final int CHARACTER_SIZE = 3;
	private static final int CHARACTER_X = 30;
	private static final int CHARACTER_COLOR = Color.argb(255, 70, 110, 160);
	
	public MainCharacter(float y)
	{
		this.square = new Square(MainCharacter.CHARACTER_X, y, MainCharacter.CHARACTER_SIZE, MainCharacter.CHARACTER_COLOR);
	}

	public void update(float delta, Input input)
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
}