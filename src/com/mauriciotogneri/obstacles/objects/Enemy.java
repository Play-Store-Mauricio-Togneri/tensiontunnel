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

public abstract class Enemy
{
	protected final Rectangle rectangle;

	private final int screenWidth;
	private float timeCounter = 0;
	private final float timeLimit;
	private final List<Beam> beams = new ArrayList<Beam>();
	
	protected static final int COLOR = Color.argb(255, 120, 220, 120);
	protected static final int ENEMY_WIDTH = 3;
	
	public Enemy(Rectangle rectangle, int screenWidth, float timeLimit)
	{
		this.screenWidth = screenWidth;
		this.timeLimit = timeLimit;
		this.rectangle = rectangle;
	}
	
	public void update(float delta, float distance, float beamSpeed)
	{
		this.rectangle.moveX(-distance);
		
		if (insideScreen(this.screenWidth))
		{
			this.timeCounter += delta;
			
			if (this.timeCounter > this.timeLimit)
			{
				AudioManager.getInstance().playSound(Resources.Sounds.BEAM);
				this.timeCounter -= this.timeLimit;
				
				this.beams.add(getNewBeam(beamSpeed));
			}
		}
		
		updateBeams(delta, distance);
	}

	protected abstract Beam getNewBeam(float beamSpeed);
	
	private void updateBeams(float delta, float distance)
	{
		Beam[] beamList = Game.getArray(this.beams, Beam.class);
		
		for (Beam beam : beamList)
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
			for (Beam beam : this.beams)
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
		
		for (Beam beam : this.beams)
		{
			beam.draw(renderer);
		}
	}
}