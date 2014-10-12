package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.mauriciotogneri.tensiontunnel.audio.AudioManager;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public abstract class EnemyShooting
{
	protected final Sprite sprite;

	private float timeCounter = 0;
	private final float timeLimit;
	private final List<Beam> beams = new ArrayList<Beam>();
	
	protected static final int SIZE_EXTERNAL = 3;
	
	public EnemyShooting(float x, float y, float timeLimit)
	{
		this.timeLimit = timeLimit;

		this.sprite = new Sprite(x, y, Resources.Sprites.ENEMY_SHOOTING);
	}
	
	public void update(float delta, float distance, float beamSpeed)
	{
		this.sprite.moveX(-distance);
		
		if (this.sprite.getX() < (Renderer.RESOLUTION_X * 1.5f))
		{
			this.timeCounter += delta;
			
			if (this.timeCounter > this.timeLimit)
			{
				if (insideScreen())
				{
					AudioManager.getInstance().playSound(Resources.Sounds.BEAM);
				}
				
				this.timeCounter -= this.timeLimit;
				
				this.beams.add(getNewBeam(beamSpeed));
			}
		}
		
		updateBeams(delta, distance);
	}

	protected abstract Beam getNewBeam(float beamSpeed);
	
	private void updateBeams(float delta, float distance)
	{
		Iterator<Beam> iterator = this.beams.iterator();
		
		while (iterator.hasNext())
		{
			Beam beam = iterator.next();
			beam.update(delta, distance);
			
			if (beam.isFinished())
			{
				iterator.remove();
			}
		}
	}
	
	public boolean isFinished()
	{
		return ((this.sprite.getX() + this.sprite.getWidth()) < 0);
	}
	
	public void destroy()
	{
		this.beams.clear();
	}
	
	public float getWidth()
	{
		return this.sprite.getX() + this.sprite.getWidth();
	}
	
	public boolean collide(Player player)
	{
		boolean result = GeometryUtils.collide(this.sprite, player.getSprite());
		
		if (!result)
		{
			for (Beam beam : this.beams)
			{
				if (beam.collide(player))
				{
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	public boolean insideScreen()
	{
		return this.sprite.insideScreen(Renderer.RESOLUTION_X);
	}
	
	public void draw(Renderer renderer)
	{
		this.sprite.draw(renderer);
		
		for (Beam beam : this.beams)
		{
			beam.draw(renderer);
		}
	}
}