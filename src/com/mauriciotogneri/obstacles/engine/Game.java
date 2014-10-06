package com.mauriciotogneri.obstacles.engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import com.mauriciotogneri.obstacles.audio.AudioManager;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.objects.Background;
import com.mauriciotogneri.obstacles.objects.Enemy;
import com.mauriciotogneri.obstacles.objects.MainCharacter;
import com.mauriciotogneri.obstacles.objects.Wall;
import com.mauriciotogneri.obstacles.util.Resources;

public class Game
{
	private Renderer renderer;
	
	private final AudioManager audioManager;
	
	private MainCharacter mainCharacter;
	private Background background;
	private final List<Wall> walls = new ArrayList<Wall>();
	private final List<Enemy> enemies = new ArrayList<Enemy>();
	
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
		
		this.enemies.clear();

		this.walls.clear();
		this.wallWidth = 10;
		this.wallGap = this.renderer.getResolutionY() / 2;

		this.lastWall = null;

		createWall();
		createEnemy();
		createWall();
		createEnemy();
	}
	
	private Wall lastWall = null;
	private int wallWidth = 0; // TODO: increment to increase difficulty
	private int wallGap = 0; // TODO: reduce to increase difficulty
	
	private void createWall()
	{
		float x = this.renderer.getResolutionX();
		
		if (this.lastWall != null)
		{
			x += this.lastWall.getWidth();
		}
		
		int deviationLimit = (this.renderer.getResolutionY() / 2) - (this.wallGap / 2) - Background.WALL_HEIGHT + 1;
		int centerDeviation = random(0, deviationLimit);
		int center = random((this.renderer.getResolutionY() / 2) - centerDeviation, (this.renderer.getResolutionY() / 2) + centerDeviation);
		
		this.lastWall = new Wall(x, center, this.wallGap, this.wallWidth, this.renderer.getResolutionY());
		this.walls.add(this.lastWall);
		
		this.wallGap--;
		this.wallWidth++;
	}

	private Enemy lastEnemy = null;

	private void createEnemy()
	{
		float x = this.renderer.getResolutionX() / 2;
		
		if (this.lastWall != null)
		{
			x += this.lastWall.getWidth();
		}
		
		this.lastEnemy = new Enemy(x, this.renderer.getResolutionX(), this.renderer.getResolutionY());
		this.enemies.add(this.lastEnemy);
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
		float distance = getDistance(delta, input);
		
		this.background.update(distance * 0.75f);
		updateWalls(distance);
		updateEnemies(delta, distance);
		updateCharacter(delta, input);
		
		checkCollision();
	}

	private void checkCollision()
	{
		if (this.background.collide(this.mainCharacter) || collideWithWall() || collideWithEnemy())
		{
			processCollision();
		}
	}
	
	private boolean collideWithWall()
	{
		boolean result = false;
		
		for (Wall wall : this.walls)
		{
			if (wall.collide(this.mainCharacter))
			{
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	private boolean collideWithEnemy()
	{
		boolean result = false;
		
		for (Enemy enemy : this.enemies)
		{
			if (enemy.collide(this.mainCharacter))
			{
				result = true;
				break;
			}
		}
		
		return result;
	}

	private void processCollision()
	{
		this.audioManager.playSound(Resources.Sounds.EXPLOSION);
		this.status = Status.COLLIDE;
	}
	
	private void updateWalls(float distance)
	{
		Wall[] wallList = Game.getArray(this.walls, Wall.class);
		int finished = 0;

		for (Wall wall : wallList)
		{
			wall.update(distance);
			
			if (wall.isFinished())
			{
				this.walls.remove(wall);
				finished++;
			}
		}

		for (int i = 0; i < finished; i++)
		{
			createWall();
		}
	}

	private void updateEnemies(float delta, float distance)
	{
		Enemy[] enemyList = Game.getArray(this.enemies, Enemy.class);
		int finished = 0;

		for (Enemy enemy : enemyList)
		{
			boolean beamCreated = enemy.update(delta, distance);

			if (beamCreated)
			{
				this.audioManager.playSound(Resources.Sounds.BEAM);
			}
			
			if (enemy.isFinished())
			{
				this.enemies.remove(enemy);
				enemy.destroy();
				finished++;
			}
		}

		for (int i = 0; i < finished; i++)
		{
			createEnemy();
		}
	}
	
	public static <T> T[] getArray(List<T> list, Class<?> clazz)
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
	
	private float getDistance(float delta, Input input)
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

		for (Enemy enemy : this.enemies)
		{
			if (enemy.insideScreen(renderer.getResolutionX()))
			{
				enemy.draw(renderer);
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