package com.mauriciotogneri.tensiontunnel.engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mauriciotogneri.tensiontunnel.R;
import com.mauriciotogneri.tensiontunnel.activities.MainActivity;
import com.mauriciotogneri.tensiontunnel.audio.AudioManager;
import com.mauriciotogneri.tensiontunnel.input.Input;
import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.MainCharacter;
import com.mauriciotogneri.tensiontunnel.objects.Wall;
import com.mauriciotogneri.tensiontunnel.objects.enemies.rotating.EnemyRotating;
import com.mauriciotogneri.tensiontunnel.objects.enemies.rotating.EnemyRotating.Direction;
import com.mauriciotogneri.tensiontunnel.objects.enemies.shooting.EnemyShooting;
import com.mauriciotogneri.tensiontunnel.objects.enemies.shooting.EnemyShootingBottom;
import com.mauriciotogneri.tensiontunnel.objects.enemies.shooting.EnemyShootingTop;
import com.mauriciotogneri.tensiontunnel.objects.score.Score;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.statistics.Statistics;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public class Game
{
	private final MainActivity mainActivity;
	private Renderer renderer;
	
	private MainCharacter mainCharacter;
	private Background background;
	private Score score;
	private final List<Wall> walls = new ArrayList<Wall>();
	private final List<EnemyShooting> enemiesShooting = new ArrayList<EnemyShooting>();
	private final List<EnemyRotating> enemiesRotating = new ArrayList<EnemyRotating>();
	
	private final Object lockRestart = new Object();
	private Status status = Status.INIT;

	private final Random random = new Random();

	private final LinearLayout blockScreen;
	
	private Wall lastWall = null;

	private int wallWidth = 0;
	private int wallGap = 0;

	private float beamSpeed = 0;
	private float beamFrequency = 0;

	private float rotationSpeed = 0;

	// TODO: CHANGE
	private boolean enemyBottom = true;
	private boolean enemyOpposite = true;
	
	// TODO: INPUT DEBUG
	private final boolean inputDebug = true;
	private Rectangle inputDebugJump;
	private Rectangle inputDebugAdvance;
	
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
	
	private static final int ROTATION_SPEED_INIT_VALUE = 2;
	private static final float ROTATION_SPEED_INCREMENT = 0.1f;
	private static final int ROTATION_SPEED_LIMIT = 5;
	
	private static final int BASE_SPEED = 30;
	private static final int EXTRA_SPEED = 20;
	
	private enum Status
	{
		INIT, RUNNING, COLLIDE
	}
	
	public Game(MainActivity mainActivity, LinearLayout blockScreen)
	{
		this.mainActivity = mainActivity;
		this.blockScreen = blockScreen;

		ImageView play = (ImageView)blockScreen.findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				restart();
			}
		});

		ImageView ranking = (ImageView)blockScreen.findViewById(R.id.ranking);
		ranking.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				showRanking();
			}
		});
		
		AudioManager.initialize(mainActivity);
		AudioManager.getInstance().playAudio(Resources.Music.MUSIC);
	}
	
	private void vibrate()
	{
		Vibrator vibrator = (Vibrator)this.mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(300);
	}

	private void showRanking()
	{
		// TODO: SHOW RANKING
	}
	
	public void start(Renderer renderer)
	{
		if (this.renderer == null)
		{
			this.renderer = renderer;
			
			this.background = new Background(Renderer.RESOLUTION_X, Renderer.RESOLUTION_Y);
			this.score = new Score();
			
			this.inputDebugJump = new Rectangle(5, Renderer.RESOLUTION_Y - Background.WALL_HEIGHT + 1, Background.WALL_HEIGHT - 2, Background.WALL_HEIGHT - 2, Color.argb(255, 0, 255, 255));
			this.inputDebugAdvance = new Rectangle(20 + Background.WALL_HEIGHT, Renderer.RESOLUTION_Y - Background.WALL_HEIGHT + 1, Background.WALL_HEIGHT - 2, Background.WALL_HEIGHT - 2, Color.argb(255, 255, 255, 0));
			
			restart();
		}
	}
	
	private void restart()
	{
		synchronized (this.lockRestart)
		{
			this.status = Status.INIT;

			hideBlockScreen();
			
			this.mainCharacter = new MainCharacter(Renderer.RESOLUTION_Y / 2);
			this.score.clear();
			
			this.enemiesShooting.clear();
			this.beamSpeed = Game.BEAM_SPEED_INIT_VALUE;
			this.beamFrequency = Game.BEAM_FREQUENCY_INIT_VALUE;
			
			this.walls.clear();
			this.wallWidth = Game.WALL_WIDTH_INIT_VALUE;
			this.wallGap = Renderer.RESOLUTION_Y / 2;

			this.rotationSpeed = Game.ROTATION_SPEED_INIT_VALUE;
			this.enemiesRotating.clear();

			this.lastWall = null;
			
			createWall();
			createEnemy();
			createWall();
			createEnemy();
		}
	}

	private void displayBlockScreen()
	{
		this.mainActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				activateBlockScreen();
			}
		});
	}
	
	private void activateBlockScreen()
	{
		SharedPreferences preferences = this.mainActivity.getPreferences(Context.MODE_PRIVATE);
		int bestScore = preferences.getInt("BEST", 0);
		int currentScore = this.score.getValue();

		if (currentScore > bestScore)
		{
			bestScore = currentScore;

			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("BEST", bestScore);
			editor.commit();
		}
		
		Statistics.sendHitFinishGame(currentScore);
		
		// TODO: SUBMIT SCORE
		// Games.Leaderboards.submitScore(getApiClient(), LEADERBOARD_ID, 1337);
		
		TextView scoreView = (TextView)this.blockScreen.findViewById(R.id.score);
		scoreView.setText(String.valueOf(currentScore));
		
		TextView bestView = (TextView)this.blockScreen.findViewById(R.id.best);
		bestView.setText(String.valueOf(bestScore));

		this.blockScreen.setVisibility(View.VISIBLE);
	}
	
	private void hideBlockScreen()
	{
		this.mainActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Game.this.blockScreen.setVisibility(View.GONE);
			}
		});
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
		createEnemyShooting();
		createEnemyRotating();
	}
	
	private void createEnemyShooting()
	{
		float x = Renderer.RESOLUTION_X / 2;
		
		if (this.lastWall != null)
		{
			x += this.lastWall.getWidth();
		}
		
		EnemyShooting enemy = null;

		if (this.enemyBottom)
		{
			enemy = new EnemyShootingBottom(x, this.beamFrequency);
		}
		else
		{
			enemy = new EnemyShootingTop(x, this.beamFrequency);
		}
		
		this.enemyBottom = !this.enemyBottom;
		this.enemiesShooting.add(enemy);
		
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
	
	private void createEnemyRotating()
	{
		float x = Renderer.RESOLUTION_X / 2;
		
		if (this.lastWall != null)
		{
			x += this.lastWall.getWidth();
		}
		
		EnemyRotating enemy = null;
		
		if (this.enemyOpposite)
		{
			enemy = new EnemyRotating(x, Direction.OPPOSITE);
		}
		else
		{
			enemy = new EnemyRotating(x, Direction.SAME);
		}

		this.enemyOpposite = !this.enemyOpposite;
		this.enemiesRotating.add(enemy);
		
		/* Change difficulty */
		
		this.rotationSpeed += Game.ROTATION_SPEED_INCREMENT;
		
		if (this.rotationSpeed > Game.ROTATION_SPEED_LIMIT)
		{
			this.rotationSpeed = Game.ROTATION_SPEED_LIMIT;
		}
	}
	
	private int random(int min, int max)
	{
		return this.random.nextInt(max - min + 1) + min;
	}
	
	// ======================== UPDATE ====================== \\
	
	public void update(float delta, Input input, Renderer renderer)
	{
		synchronized (this.lockRestart)
		{
			if ((this.status == Status.INIT) && (input.jumpPressed || input.advancePressed))
			{
				this.status = Status.RUNNING;
				Statistics.sendHitNewGame();
			}
			
			if (this.status == Status.RUNNING)
			{
				update(delta, input);
			}

			draw(renderer);
			
			if (this.inputDebug)
			{
				if (input.jumpPressed)
				{
					this.inputDebugJump.draw(renderer);
				}

				if (input.advancePressed)
				{
					this.inputDebugAdvance.draw(renderer);
				}
			}
		}
	}
	
	private void update(float delta, Input input)
	{
		float distance = getDistance(delta, input);
		this.score.add(distance);
		
		this.background.update(distance * 0.75f);
		updateWalls(distance);
		updateEnemiesShooting(delta, distance);
		updateEnemiesRotating(delta, distance);
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
		
		for (EnemyShooting enemy : this.enemiesShooting)
		{
			if (enemy.collide(this.mainCharacter))
			{
				result = true;
				break;
			}
		}
		
		if (!result)
		{
			for (EnemyRotating enemy : this.enemiesRotating)
			{
				if (enemy.collide(this.mainCharacter))
				{
					result = true;
					break;
				}
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
		displayBlockScreen();
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
	
	private void updateEnemiesShooting(float delta, float distance)
	{
		EnemyShooting[] enemyList = Game.getArray(this.enemiesShooting, EnemyShooting.class);
		int finished = 0;
		
		for (EnemyShooting enemy : enemyList)
		{
			enemy.update(delta, distance, this.beamSpeed);
			
			if (enemy.isFinished())
			{
				this.enemiesShooting.remove(enemy);
				enemy.destroy();
				finished++;
			}
		}
		
		for (int i = 0; i < finished; i++)
		{
			createEnemy();
		}
	}
	
	private void updateEnemiesRotating(float delta, float distance)
	{
		EnemyRotating[] enemyList = Game.getArray(this.enemiesRotating, EnemyRotating.class);
		
		for (EnemyRotating enemy : enemyList)
		{
			enemy.update(delta, distance, this.rotationSpeed);
			
			if (enemy.isFinished())
			{
				this.enemiesRotating.remove(enemy);
			}
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
		
		for (EnemyShooting enemy : this.enemiesShooting)
		{
			if (enemy.insideScreen())
			{
				enemy.draw(renderer);
			}
		}

		for (EnemyRotating enemy : this.enemiesRotating)
		{
			if (enemy.insideScreen())
			{
				enemy.draw(renderer);
			}
		}
		
		if (this.status != Status.COLLIDE)
		{
			this.score.draw(renderer);
		}
		
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