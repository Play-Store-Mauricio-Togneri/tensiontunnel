package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.input.Input;
import com.mauriciotogneri.tensiontunnel.shapes.Square;

public class MainCharacter
{
	private Square square;
	private float acceleration = 0;

	private static final int GRAVITY = 1;
	private static final int JUMP_FORCE = 3;

	private static final int MAX_ACCELERATION_UP = 30;
	private static final int MAX_ACCELERATION_DOWN = 30;

	public static final int CHARACTER_SIZE = 3;
	private static final int CHARACTER_X = 30;
	private static final int CHARACTER_COLOR = Color.argb(255, 220, 75, 60);

	public MainCharacter()
	{
		reset();
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

	public void reset()
	{
		this.acceleration = 0;
		this.square = new Square(MainCharacter.CHARACTER_X, Renderer.RESOLUTION_Y / 2, MainCharacter.CHARACTER_SIZE, MainCharacter.CHARACTER_COLOR);
	}
}