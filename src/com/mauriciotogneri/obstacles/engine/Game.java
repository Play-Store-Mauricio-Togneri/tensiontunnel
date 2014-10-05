package com.mauriciotogneri.obstacles.engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import com.mauriciotogneri.obstacles.audio.AudioManager;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.objects.Background;
import com.mauriciotogneri.obstacles.objects.MainCharacter;
import com.mauriciotogneri.obstacles.objects.Wall;
import com.mauriciotogneri.obstacles.util.GeometryUtils;
import com.mauriciotogneri.obstacles.util.Resources;

public class Game
{
	private Renderer renderer;

	private final AudioManager audioManager;

	private MainCharacter mainCharacter;
	private Background background;
	private final List<Wall> walls = new ArrayList<Wall>();

	private Status status = Status.INIT;
	
	private final Random random = new Random();

	private static final int BASE_SPEED = 30;
	private static final int EXTRA_SPEED = 20;

	private enum Status
	{
		INIT, RUNNING, COLLIDE
	}

	public Game(Context context)
	{
		this.audioManager = new AudioManager(context);
	}

	public void start(Renderer renderer)
	{
		if (this.renderer == null)
		{
			this.renderer = renderer;

			restart();
		}
	}

	private void restart()
	{
		this.mainCharacter = new MainCharacter(this.renderer.getResolutionY() / 2);

		this.background = new Background(this.renderer.getResolutionX(), this.renderer.getResolutionY());

		this.walls.clear();

		// createWall(this.renderer.getResolutionX());
		// createWall(this.renderer.getResolutionX() + 33);
		// createWall(this.renderer.getResolutionX() + 66);

		int limit = this.renderer.getResolutionX() + 20;

		for (int i = 0; i < limit; i++)
		{
			createWall(this.renderer.getResolutionX() + (i * 10));
		}
	}
	
	private void createWall(float x)
	{
		final int GAP = 10;

		Wall wall = null;

		int width = 10;
		int height = 10;// 15 + random(0, 10);

		if (random(0, 1) == 0)
		{
			// down
			wall = new Wall(x + GAP, 0, width, height);
		}
		else
		{
			// up
			wall = new Wall(x + GAP, this.renderer.getResolutionY() - height, width, height);
		}

		// this.walls.add(wall);

		// ------------
		this.walls.add(new Wall(x, this.renderer.getResolutionY() - height, width, height));
		this.walls.add(new Wall(x + GAP, 0, width, height));
	}
	
	private int random(int min, int max)
	{
		return this.random.nextInt(max - min + 1) + min;
	}

	// ======================== UPDATE ====================== \\

	public void update(float delta, Input input, Renderer renderer)
	{
		if ((input.jumpPressed || input.advancePressed))
		{
			if (this.status == Status.INIT)
			{
				this.status = Status.RUNNING;
				this.audioManager.playAudio(Resources.Music.MUSIC);
			}
			else if (this.status == Status.COLLIDE)
			{
				this.status = Status.RUNNING;
				restart();
			}
		}

		if (this.status == Status.RUNNING)
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

		checkCollision();
	}
	
	private void checkCollision()
	{
		if (this.background.collide(this.mainCharacter))
		{
			this.audioManager.playSound(Resources.Sounds.EXPLOSION);
			this.status = Status.COLLIDE;
		}
		else
		{
			for (Wall wall : this.walls)
			{
				if (GeometryUtils.collide(wall.getRectangle(), this.mainCharacter.getShape()))
				{
					this.audioManager.playSound(Resources.Sounds.EXPLOSION);
					this.status = Status.COLLIDE;
					break;
				}
			}
		}
	}

	private void updateWalls(float speed)
	{
		Wall[] wallList = getArray(this.walls, Wall.class);
		
		for (Wall wall : wallList)
		{
			wall.update(speed);

			if (wall.isFinished())
			{
				this.walls.remove(wall);
				// createWall(wall.getRectangle().getX() + wall.getRectangle().getWidth() +
				// this.renderer.getResolutionX());
			}
		}
	}

	private <T> T[] getArray(List<T> list, Class<?> clazz)
	{
		@SuppressWarnings("unchecked")
		T[] array = (T[])Array.newInstance(clazz, list.size());
		list.toArray(array);

		return array;
	}

	private void updateCharacter(float delta, Input input)
	{
		this.mainCharacter.update(delta, input);
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
			if (wall.insideScreen(renderer.getResolutionX()))
			{
				wall.draw(renderer);
			}
		}

		this.mainCharacter.draw(renderer);
	}

	// ======================== LIFE CYCLE ====================== \\

	public void pause(boolean finishing)
	{
		if (this.audioManager != null)
		{
			this.audioManager.pauseAudio();
		}

		if (this.renderer != null)
		{
			this.renderer.pause(finishing);
		}
	}

	public void resume()
	{
		if (this.audioManager != null)
		{
			this.audioManager.resumeAudio();
		}

		if (this.renderer != null)
		{
			this.renderer.resume();
		}
	}

	public void stop()
	{
		if (this.audioManager != null)
		{
			this.audioManager.stopAudio();
		}
	}
}