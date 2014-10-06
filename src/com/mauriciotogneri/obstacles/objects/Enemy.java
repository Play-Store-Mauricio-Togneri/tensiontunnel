package com.mauriciotogneri.obstacles.objects;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import com.mauriciotogneri.obstacles.audio.AudioManager;
import com.mauriciotogneri.obstacles.engine.Game;
import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.util.GeometryUtils;
import com.mauriciotogneri.obstacles.util.Resources;

public class Enemy
{
	private final Rectangle rectangle;
	private final int screenWidth;
	private final int screenHeight;
	private float timeCounter = 0;
	private final List<BeamUp> beams = new ArrayList<BeamUp>();

	private static final int COLOR = Color.argb(255, 120, 220, 120);
	private static final int ENEMY_WIDTH = 3;
	private static final float TIME_BEAM_LIMIT = 0.5f;

	public Enemy(float x, int screenWidth, int screenHeight)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.rectangle = new Rectangle(x - (Enemy.ENEMY_WIDTH / 2f), Background.WALL_HEIGHT, Enemy.ENEMY_WIDTH, Enemy.ENEMY_WIDTH, Enemy.COLOR);
	}
	
	public void update(float delta, float distance)
	{
		this.rectangle.moveX(-distance);

		if (insideScreen(this.screenWidth))
		{
			this.timeCounter += delta;
			
			if (this.timeCounter > Enemy.TIME_BEAM_LIMIT)
			{
				AudioManager.getInstance().playSound(Resources.Sounds.BEAM);
				this.timeCounter -= Enemy.TIME_BEAM_LIMIT;
				
				this.beams.add(new BeamUp(this.rectangle.getX() + (this.rectangle.getWidth() / 2f), Background.WALL_HEIGHT + Enemy.ENEMY_WIDTH, this.screenHeight));
			}
		}

		BeamUp[] beamList = Game.getArray(this.beams, BeamUp.class);
		
		for (BeamUp beam : beamList)
		{
			beam.update(delta, distance);
			
			if (beam.isFinished())
			{
				this.beams.remove(beam);
			}
		}
	}
	
	public boolean isFinished()
	{
		return ((this.rectangle.getX() + this.rectangle.getWidth()) < 0);
	}
	
	public void destroy()
	{
		this.beams.clear();
	}

	public float getWidth()
	{
		return this.rectangle.getX() + this.rectangle.getWidth();
	}
	
	public boolean collide(MainCharacter mainCharacter)
	{
		boolean result = GeometryUtils.collide(this.rectangle, mainCharacter.getShape());
		
		if (!result)
		{
			for (BeamUp beam : this.beams)
			{
				if (beam.collide(mainCharacter))
				{
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	public boolean insideScreen(int width)
	{
		return this.rectangle.insideScreen(width);
	}

	public void draw(Renderer renderer)
	{
		this.rectangle.draw(renderer);

		for (BeamUp beam : this.beams)
		{
			beam.draw(renderer);
		}
	}
}