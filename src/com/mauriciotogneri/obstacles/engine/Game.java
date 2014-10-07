package com.mauriciotogneri.obstacles.engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.os.Vibrator;
import com.mauriciotogneri.obstacles.audio.AudioManager;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.objects.Background;
import com.mauriciotogneri.obstacles.objects.Enemy;
import com.mauriciotogneri.obstacles.objects.EnemyBottom;
import com.mauriciotogneri.obstacles.objects.EnemyTop;
import com.mauriciotogneri.obstacles.objects.MainCharacter;
import com.mauriciotogneri.obstacles.objects.Score;
import com.mauriciotogneri.obstacles.objects.Wall;
import com.mauriciotogneri.obstacles.util.Resources;

public class Game
{
	private final Context context;
	private Renderer renderer;
	
	private MainCharacter mainCharacter;
	private Background background;
	private Score score;
	private final List<Wall> walls = new ArrayList<Wall>();
	private final List<Enemy> enemies = new ArrayList<Enemy>();
	
	private Status status = Status.INIT;
	private float coolDownTimer = 0;

	private final Random random = new Random();

	private Wall lastWall = null;
	private int wallWidth = 0;
	private int wallGap = 0;

	private float beamSpeed = 0;
	private float beamFrequency = 0;
	private boolean enemyBottom = true;
	
	private static final int WALL_WIDTH_INIT_VALUE = 10;
	private static final int WALL_WIDTH_INCREMENT = 1;
	private static final int WALL_WIDTH_LIMIT = 100;
	
	private static final int WALL_GAP_DECREMENT = 1;
	private static final int WALL_GAP_LIMIT = (int)(MainCharacter.CHARACTER_SIZE * (3 / 2f));
	
	private static final int BEAM_SPEED_INIT_VALUE = 40;
	private static final float BEAM_SPEED_INCREMENT = 0.2f;
	private static final int BEAM_SPEED_LIMIT = 50;

	private static final float BEAM_FREQUENCY_INIT_VALUE = 0.75f;
	private static final float BEAM_FREQUENCY_DECREMENT = 0.01f;
	private static final float BEAM_FREQUENCY_LIMIT = 0.3f;
	
	private static final int BASE_SPEED = 30;
	private static final int EXTRA_SPEED = 20;
	
	private enum Status
	{
		INIT, RUNNING, COLLIDE
	}
	
	public Game(Context context)
	{
		this.context = context;
		
		AudioManager.initialize(context);
		AudioManager.getInstance().playAudio(Resources.Music.MUSIC);
	}

	private void vibrate()
	{
		Vibrator vibrator = (Vibrator)this.context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(300);
	}
	
	public void start(Renderer renderer)
	{
		if (this.renderer == null)
		{
			this.renderer = renderer;
			
			this.background = new Background(Renderer.RESOLUTION_X, Renderer.RESOLUTION_Y);
			this.score = new Score();
			
			restart();
		}
	}
	
	private void restart()
	{
		this.mainCharacter = new MainCharacter(Renderer.RESOLUTION_Y / 2);
		this.score.clear();

		this.enemies.clear();
		this.beamSpeed = Game.BEAM_SPEED_INIT_VALUE;
		this.beamFrequency = Game.BEAM_FREQUENCY_INIT_VALUE;
		
		this.walls.clear();
		this.wallWidth = Game.WALL_WIDTH_INIT_VALUE;
		this.wallGap = Renderer.RESOLUTION_Y / 2;
		
		this.lastWall = null;

		this.coolDownTimer = 0;
		
		createWall();
		createEnemy();
		createWall();
		createEnemy();
	}
	
	private void createWall()
	{
		float x = Renderer.RESOLUTION_X;

		if (this.lastWall != null)
		{
			x += this.lastWall.getWidth();
		}

		int deviationLimit = (Renderer.RESOLUTION_Y / 2) - (this.wallGap / 2) - Background.WALL_HEIGHT + 1;
		int centerDeviation = random(0, deviationLimit);
		int center = random((Renderer.RESOLUTION_Y / 2) - centerDeviation, (Renderer.RESOLUTION_Y / 2) + centerDeviation);

		this.lastWall = new Wall(x, center, this.wallGap, this.wallWidth);
		this.walls.add(this.lastWall);

		/* Change difficulty */

		this.wallGap -= Game.WALL_GAP_DECREMENT;

		if (this.wallGap < Game.WALL_GAP_LIMIT)
		{
			this.wallGap = Game.WALL_GAP_LIMIT;
		}

		this.wallWidth += Game.WALL_WIDTH_INCREMENT;

		if (this.wallWidth > Game.WALL_WIDTH_LIMIT)
		{
			this.wallWidth = Game.WALL_WIDTH_LIMIT;
		}
	}
	
	private void createEnemy()
	{
		float x = Renderer.RESOLUTION_X / 2;
		
		if (this.lastWall != null)
		{
			x += this.lastWall.getWidth();
		}
		
		Enemy enemy = null;

		if (this.enemyBottom)
		{
			enemy = new EnemyBottom(x, this.beamFrequency);
		}
		else
		{
			enemy = new EnemyTop(x, this.beamFrequency);
		}
		
		this.enemyBottom = !this.enemyBottom;
		this.enemies.add(enemy);
		
		/* Change difficulty */

		this.beamSpeed += Game.BEAM_SPEED_INCREMENT;
		
		if (this.beamSpeed > Game.BEAM_SPEED_LIMIT)
		{
			this.beamSpeed = Game.BEAM_SPEED_LIMIT;
		}
		
		this.beamFrequency -= Game.BEAM_FREQUENCY_DECREMENT;
		
		if (this.beamFrequency < Game.BEAM_FREQUENCY_LIMIT)
		{
			this.beamFrequency = Game.BEAM_FREQUENCY_LIMIT;
		}
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
			}
			else if (this.status == Status.COLLIDE)
			{
				this.coolDownTimer += delta;
				
				if (this.coolDownTimer > 0.2f)
				{
					this.status = Status.RUNNING;
					restart();
				}
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
		this.score.add(distance);
		
		this.background.update(distance * 0.75f);
		updateWalls(distance);
		updateEnemies(delta, distance);
		updateCharacter(delta, input);
		
		checkCollision(input);
	}
	
	private void checkCollision(Input input)
	{
		if (this.background.collide(this.mainCharacter) || collideWithWall() || collideWithEnemy())
		{
			processCollision(input);
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
	
	private void processCollision(Input input)
	{
		AudioManager.getInstance().playSound(Resources.Sounds.EXPLOSION);
		vibrate();
		input.clear();
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
			enemy.update(delta, distance, this.beamSpeed);
			
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
			if (wall.insideScreen())
			{
				wall.draw(renderer);
			}
		}
		
		for (Enemy enemy : this.enemies)
		{
			if (enemy.insideScreen())
			{
				enemy.draw(renderer);
			}
		}
		
		this.score.draw(renderer);
		this.mainCharacter.draw(renderer);
	}
	
	// ======================== LIFE CYCLE ====================== \\
	
	public void pause(boolean finishing)
	{
		if (AudioManager.getInstance() != null)
		{
			AudioManager.getInstance().pauseAudio();
		}
		
		if (this.renderer != null)
		{
			this.renderer.pause(finishing);
		}
	}
	
	public void resume()
	{
		if (AudioManager.getInstance() != null)
		{
			AudioManager.getInstance().resumeAudio();
		}
	}
	
	public void stop()
	{
		if (AudioManager.getInstance() != null)
		{
			AudioManager.getInstance().stopAudio();
		}
	}
}