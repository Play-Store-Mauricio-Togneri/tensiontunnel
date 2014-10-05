package com.mauriciotogneri.obstacles.engine;

import java.util.ArrayList;
import java.util.List;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.objects.Background;
import com.mauriciotogneri.obstacles.objects.Enemy;
import com.mauriciotogneri.obstacles.objects.MainCharacter;

public class Game
{
	private Renderer renderer;
	
	private MainCharacter mainCharacter;
	private Background background;
	private final List<Enemy> enemies = new ArrayList<Enemy>();

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
			
			this.enemies.add(new Enemy(60, 5, 10, 20));
			this.enemies.add(new Enemy(120, renderer.getResolutionY() - 20, 10, 20));
		}
	}
	
	// ======================== UPDATE ====================== \\

	public void update(float delta, Input input, int positionLocation, int colorLocation)
	{
		if (input.jumpPressed || input.advancePressed)
		{
			this.started = true;
		}

		if (this.started)
		{
			update(delta, input);
		}

		draw(positionLocation, colorLocation);
	}
	
	private void update(float delta, Input input)
	{
		float speed = getSpeed(delta, input);
		
		this.background.update(speed * 0.75f);
		updateEnemies(speed);
		updateCharacter(delta, input);
	}

	private void updateEnemies(float speed)
	{
		for (Enemy enemy : this.enemies)
		{
			enemy.update(speed);
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

	private void draw(int positionLocation, int colorLocation)
	{
		this.background.draw(positionLocation, colorLocation);
		
		for (Enemy enemy : this.enemies)
		{
			enemy.draw(positionLocation, colorLocation);
		}
		
		this.mainCharacter.draw(positionLocation, colorLocation);
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