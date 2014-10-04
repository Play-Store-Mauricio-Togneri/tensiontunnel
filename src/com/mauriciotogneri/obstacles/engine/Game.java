package com.mauriciotogneri.obstacles.engine;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.objects.Background;
import com.mauriciotogneri.obstacles.objects.MainCharacter;
import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.shapes.Shape;
import com.mauriciotogneri.obstacles.shapes.Square;

public class Game
{
	private Renderer renderer;

	private Rectangle squareTop;
	private Rectangle squareBottom;
	
	private MainCharacter character;
	private Background background;

	private static final int BASE_SPEED = 30;
	private static final int EXTRA_SPEED = 20;
	
	public void start(Renderer renderer)
	{
		if (this.renderer == null)
		{
			this.renderer = renderer;
			
			this.character = new MainCharacter(renderer.getResolutionY() / 2);

			this.squareTop = new Rectangle(0, renderer.getResolutionY() - 5, renderer.getResolutionX(), 5, Color.BLACK);
			this.squareBottom = new Rectangle(0, 0, renderer.getResolutionX(), 5, Color.BLACK);
			
			this.background = new Background(renderer.getResolutionY());
		}
	}
	
	// ======================== UPDATE ====================== \\

	public void update(float delta, Input input, int positionLocation, int colorLocation)
	{
		List<Shape> shapes = update(delta, input);
		
		draw(shapes, positionLocation, colorLocation);
	}
	
	private List<Shape> update(float delta, Input input)
	{
		List<Shape> result = new ArrayList<Shape>();

		float speed = getSpeed(delta, input);
		
		this.background.update(speed);
		updateCharacter(delta, input, result);
		
		return result;
	}

	private void updateCharacter(float delta, Input input, List<Shape> shapes)
	{
		Square square = this.character.updateCharacter(delta, input, this.renderer.getResolutionY());
		shapes.add(square);
	}
	
	private float getSpeed(float delta, Input input)
	{
		float result = Game.BASE_SPEED;

		if (input.advancePressed)
		{
			result += Game.EXTRA_SPEED;
		}

		return (delta * result);
	}

	// ======================== DRAW ====================== \\

	private void draw(List<Shape> shapes, int positionLocation, int colorLocation)
	{
		this.background.draw(positionLocation, colorLocation);
		
		for (Shape shape : shapes)
		{
			shape.draw(positionLocation, colorLocation);
		}
		
		this.squareTop.draw(positionLocation, colorLocation);
		this.squareBottom.draw(positionLocation, colorLocation);
	}
	
	// ======================== LIFE CYCLE ====================== \\
	
	public void pause(boolean finishing)
	{
		// pauseAudio();
		
		if (this.renderer != null)
		{
			this.renderer.pause(finishing);
		}
	}
	
	public void resume()
	{
		// resumeAudio();

		if (this.renderer != null)
		{
			this.renderer.resume();
		}
	}
	
	public void stop()
	{
		// stopAudio();
	}
}