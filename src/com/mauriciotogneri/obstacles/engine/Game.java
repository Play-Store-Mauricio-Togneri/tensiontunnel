package com.mauriciotogneri.obstacles.engine;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.objects.Background;
import com.mauriciotogneri.obstacles.objects.MainCharacter;
import com.mauriciotogneri.obstacles.objects.Wall;

public class Game
{
	private Renderer renderer;

	private MainCharacter mainCharacter;
	private Background background;
	private final List<Wall> walls = new ArrayList<Wall>();
	
	private boolean started = false;

	private static final int BASE_SPEED = 30;
	private static final int EXTRA_SPEED = 20;

	public void start(Renderer renderer)
	{
		if (this.renderer == null)
		{
			this.renderer = renderer;

			this.mainCharacter = new MainCharacter(renderer.getResolutionY() / 2);
			
			this.background = new Background(renderer.getResolutionX(), renderer.getResolutionY());

			this.walls.add(new Wall(60, 5, 10, 20, Color.argb(255, 90, 110, 120)));
			this.walls.add(new Wall(120, renderer.getResolutionY() - 20, 10, 20, Color.argb(255, 90, 110, 120)));
		}
	}

	// ======================== UPDATE ====================== \\
	
	public void update(float delta, Input input, Renderer renderer)
	{
		if (input.jumpPressed || input.advancePressed)
		{
			this.started = true;
		}
		
		if (this.started)
		{
			update(delta, input);
		}
		
		draw(renderer);
	}

	private void update(float delta, Input input)
	{
		float speed = getSpeed(delta, input);

		this.background.update(speed * 0.75f);
		updateWalls(speed);
		updateCharacter(delta, input);
	}
	
	private void updateWalls(float speed)
	{
		for (Wall wall : this.walls)
		{
			wall.update(speed);
		}
	}

	private void updateCharacter(float delta, Input input)
	{
		this.mainCharacter.updateCharacter(delta, input, this.renderer.getResolutionY());
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
	
	private void draw(Renderer renderer)
	{
		this.background.draw(renderer);

		for (Wall wall : this.walls)
		{
			wall.draw(renderer);
		}

		this.mainCharacter.draw(renderer);
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