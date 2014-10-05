package com.mauriciotogneri.obstacles.engine;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
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
		this.walls.add(new Wall(60, 5, 10, 20, Color.argb(255, 90, 110, 120)));
		this.walls.add(new Wall(120, this.renderer.getResolutionY() - 20, 10, 20, Color.argb(255, 90, 110, 120)));
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
		for (Wall wall : this.walls)
		{
			wall.update(speed);
		}
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
			wall.draw(renderer);
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