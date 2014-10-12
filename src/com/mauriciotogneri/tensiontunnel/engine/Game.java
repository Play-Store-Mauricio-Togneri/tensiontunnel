package com.mauriciotogneri.tensiontunnel.engine;

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
import com.mauriciotogneri.tensiontunnel.objects.Box;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.objects.Wall;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.enemies.rotating.EnemyRotating;
import com.mauriciotogneri.tensiontunnel.objects.enemies.rotating.EnemyRotating.Direction;
import com.mauriciotogneri.tensiontunnel.objects.enemies.shooting.EnemyShooting;
import com.mauriciotogneri.tensiontunnel.objects.enemies.shooting.EnemyShootingBottom;
import com.mauriciotogneri.tensiontunnel.objects.enemies.shooting.EnemyShootingTop;
import com.mauriciotogneri.tensiontunnel.objects.score.Score;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.statistics.Statistics;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public class Game
{
	private final MainActivity mainActivity;
	private Renderer renderer;

	private Player player;
	private Background background;
	private Score score;

	private final List<Process> processes = new ArrayList<Process>();

	private final Object lockRestart = new Object();
	private Status status = Status.INIT;

	private final Random random = new Random();

	private final LinearLayout blockScreen;

	private float powerUpTimer = 0;
	private PowerUp currentPowerUp = null;

	private Wall lastWall = null;

	private int wallWidth = 0;
	private int wallGap = 0;

	private float beamSpeed = 0;
	private float beamFrequency = 0;

	private float rotationSpeed = 0;

	// TODO: CHANGE
	private boolean enemyShootingBottom = true;
	private boolean enemyRotatingOpposite = true;

	// TODO: INPUT DEBUG
	private final static boolean INPUT_DEBUG = true;
	private Sprite inputDebugJump;
	private Sprite inputDebugAdvance;

	private static Game INSTANCE = null;

	private static final int POWER_UP_TIME_LIMIT = 5;

	private static final int WALL_WIDTH_INIT_VALUE = 10;
	private static final int WALL_WIDTH_INCREMENT = 1;
	private static final int WALL_WIDTH_LIMIT = 100;

	private static final int WALL_GAP_INIT_RATIO = 3;
	private static final int WALL_GAP_DECREMENT = 1;
	private static final int WALL_GAP_LIMIT = Player.getSize() * 2;

	private static final int BEAM_SPEED_INIT_VALUE = 40;
	private static final float BEAM_SPEED_INCREMENT = 0.2f;
	private static final int BEAM_SPEED_LIMIT = 50;

	private static final float BEAM_FREQUENCY_INIT_VALUE = 0.75f;
	private static final float BEAM_FREQUENCY_DECREMENT = 0.01f;
	private static final float BEAM_FREQUENCY_LIMIT = 0.3f;

	private static final int ROTATION_SPEED_INIT_VALUE = 2;
	private static final float ROTATION_SPEED_INCREMENT = 0.1f;
	private static final int ROTATION_SPEED_LIMIT = 4;

	private static final int BASE_SPEED = 30;
	private static final int EXTRA_SPEED = 20;

	private enum Status
	{
		INIT, RUNNING, COLLIDE
	}
	
	private enum PowerUp
	{
		INVULNERABILITY, SLOW_TIME, FAST_SPEED, HEAVY_GRAVITY;
	}

	// TODO:
	// reset the leaderboard
	// https://developers.google.com/games/services/management/api/scores/resetForAllPlayers
	public Game(MainActivity mainActivity, LinearLayout blockScreen)
	{
		Game.INSTANCE = this;

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

	public static Game getInstance()
	{
		return Game.INSTANCE;
	}

	public void addProcess(Process process)
	{
		this.processes.add(process);
	}

	public void removeProcess(Process process)
	{
		this.processes.remove(process);
	}

	private void vibrate()
	{
		Vibrator vibrator = (Vibrator)this.mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(300);
	}

	private void showRanking()
	{
		this.mainActivity.showRanking();
	}

	public void start(Renderer renderer)
	{
		if (this.renderer == null)
		{
			this.renderer = renderer;

			this.player = new Player();
			this.background = new Background();
			this.score = new Score();

			this.inputDebugJump = new Sprite(5, Renderer.RESOLUTION_Y - Background.getHeight() + 1, new Rectangle(Background.getHeight() - 2, Background.getHeight() - 2, Color.argb(255, 0, 255, 255)));
			this.inputDebugAdvance = new Sprite(20 + Background.getHeight(), Renderer.RESOLUTION_Y - Background.getHeight() + 1, new Rectangle(Background.getHeight() - 2, Background.getHeight() - 2, Color.argb(255, 255, 255, 0)));

			restart();
		}
	}

	private void restart()
	{
		synchronized (this.lockRestart)
		{
			this.status = Status.INIT;

			hideBlockScreen();

			this.processes.clear();

			this.player.reset();
			this.score.clear();

			this.wallWidth = Game.WALL_WIDTH_INIT_VALUE;
			this.wallGap = Renderer.RESOLUTION_Y / Game.WALL_GAP_INIT_RATIO;

			this.beamSpeed = Game.BEAM_SPEED_INIT_VALUE;
			this.beamFrequency = Game.BEAM_FREQUENCY_INIT_VALUE;

			this.rotationSpeed = Game.ROTATION_SPEED_INIT_VALUE;

			this.currentPowerUp = null;
			this.powerUpTimer = 0;

			this.lastWall = null;

			createWall();
			createEnemy();
			createWall();
			createEnemy();
		}
	}

	// ====================== BLOCK SCREEN ======================== \\

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

		this.mainActivity.submitScore(currentScore);

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

	public void wallPassed()
	{
		this.score.add(1);
		AudioManager.getInstance().playSound(Resources.Sounds.POINT);
	}

	// =================== CREATE OBJECTS ================ \\

	public void createWall()
	{
		float x = Renderer.RESOLUTION_X;

		if (this.lastWall != null)
		{
			x += this.lastWall.getWidth();
		}

		int deviationLimit = (Renderer.RESOLUTION_Y / 2) - (this.wallGap / 2) - Background.getHeight() + 1;
		int centerDeviation = random(0, deviationLimit);
		int center = random((Renderer.RESOLUTION_Y / 2) - centerDeviation, (Renderer.RESOLUTION_Y / 2) + centerDeviation);

		Wall wall = new Wall(this, x, center, this.wallGap, this.wallWidth);
		wall.start();
		this.lastWall = wall;

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

		createBox();
	}

	private void createBox()
	{
		if (this.currentPowerUp == null)
		{
			float x = this.lastWall.getWidth() + 20;

			Box box = new Box(x, this.lastWall.getCenter());
			box.start();
		}
	}

	public void createEnemy()
	{
		createEnemyRotating();
		createEnemyShooting();
	}

	private void createEnemyShooting()
	{
		float x = Renderer.RESOLUTION_X / 2;

		if (this.lastWall != null)
		{
			x += this.lastWall.getWidth();
		}

		EnemyShooting enemy = null;

		if (this.enemyShootingBottom)
		{
			enemy = new EnemyShootingBottom(this, x, this.beamFrequency, this.beamSpeed);
		}
		else
		{
			enemy = new EnemyShootingTop(this, x, this.beamFrequency, this.beamSpeed);
		}

		this.enemyShootingBottom = !this.enemyShootingBottom;
		enemy.start();

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

		if (this.enemyRotatingOpposite)
		{
			enemy = new EnemyRotating(x, this.rotationSpeed, Direction.OPPOSITE);
		}
		else
		{
			enemy = new EnemyRotating(x, this.rotationSpeed, Direction.SAME);
		}

		this.enemyRotatingOpposite = !this.enemyRotatingOpposite;
		enemy.start();

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

			if (Game.INPUT_DEBUG)
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
		Process[] list = new Process[this.processes.size()];
		this.processes.toArray(list);

		float distance = getDistance(delta, input);

		this.background.update(distance);
		updateProcesses(list, delta, distance);

		updatePlayer(delta, input);
		
		this.powerUpTimer += delta;

		if ((this.currentPowerUp != null) && (this.powerUpTimer > Game.POWER_UP_TIME_LIMIT))
		{
			if (this.currentPowerUp == PowerUp.INVULNERABILITY)
			{
				this.player.setInvulnerable(false);
			}

			this.currentPowerUp = null;
		}
		
		checkCollision(list, input);
	}

	private void updateProcesses(Process[] list, float delta, float distance)
	{
		for (Process process : list)
		{
			process.process(delta, distance);
		}
	}

	private void updatePlayer(float delta, Input input)
	{
		this.player.update(delta, input);
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

	// ======================== COLLISION ====================== \\

	private void checkCollision(Process[] list, Input input)
	{
		if (this.background.collide(this.player) || collideWithElement(list))
		{
			processCollision(input);
		}

		collideWithBox(list);
	}

	private void collideWithBox(Process[] list)
	{
		for (Process process : list)
		{
			if (process instanceof Box)
			{
				Box box = (Box)process;

				if (box.collide(this.player))
				{
					box.finish();
					AudioManager.getInstance().playSound(Resources.Sounds.BOX);

					this.currentPowerUp = PowerUp.INVULNERABILITY; // PowerUp.values()[random(0,
																	// PowerUp.values().length)];
					
					if (this.currentPowerUp == PowerUp.INVULNERABILITY)
					{
						this.player.setInvulnerable(true);
					}
					
					this.powerUpTimer = 0;
					removeAllBoxes(list);
					break;
				}
			}
		}
	}
	
	private void removeAllBoxes(Process[] list)
	{
		for (Process process : list)
		{
			if (process instanceof Box)
			{
				Box box = (Box)process;
				box.finish();
			}
		}
	}
	
	private boolean collideWithElement(Process[] list)
	{
		boolean result = false;

		for (Process process : list)
		{
			if (process instanceof Wall)
			{
				Wall wall = (Wall)process;

				if (wall.collide(this.player))
				{
					result = true;
				}
			}
			else if (this.currentPowerUp != PowerUp.INVULNERABILITY)
			{
				if (process instanceof EnemyShooting)
				{
					EnemyShooting enemy = (EnemyShooting)process;
					
					if (enemy.collide(this.player))
					{
						result = true;
					}
				}
				else if (process instanceof EnemyRotating)
				{
					EnemyRotating enemy = (EnemyRotating)process;
					
					if (enemy.collide(this.player))
					{
						result = true;
					}
				}
				else if (process instanceof Beam)
				{
					Beam beam = (Beam)process;
					
					if (beam.collide(this.player))
					{
						result = true;
					}
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

	// ======================== DRAW ====================== \\

	private void draw(Renderer renderer)
	{
		this.background.draw(renderer);

		for (Process process : this.processes)
		{
			if (process.isVisible())
			{
				process.draw(renderer);
			}
		}

		if (this.status != Status.COLLIDE)
		{
			this.score.draw(renderer);
		}

		this.player.draw(renderer);
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