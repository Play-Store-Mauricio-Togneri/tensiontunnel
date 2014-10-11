package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.audio.AudioManager;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.shapes.Square;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public abstract class EnemyShooting
{
	protected final Square squareExternal;
	private final Square squareInternal;

	private float timeCounter = 0;
	private final float timeLimit;
	private final List<Beam> beams = new ArrayList<Beam>();
	
	private static final int COLOR_EXTERNAL = Color.argb(255, 110, 210, 110);
	private static final int COLOR_INTERNAL = Color.argb(255, 120, 220, 120);

	protected static final int SIZE_EXTERNAL = 3;
	private static final int SIZE_INTERNAL = 2;
	private static final float HALF_SIZE_DIFFERENCE = (EnemyShooting.SIZE_EXTERNAL - EnemyShooting.SIZE_INTERNAL) / 2f;
	
	public EnemyShooting(float x, float y, float timeLimit)
	{
		this.timeLimit = timeLimit;

		this.squareExternal = new Square(x, y, EnemyShooting.SIZE_EXTERNAL, EnemyShooting.COLOR_EXTERNAL);
		this.squareInternal = new Square(x + EnemyShooting.HALF_SIZE_DIFFERENCE, y + EnemyShooting.HALF_SIZE_DIFFERENCE, EnemyShooting.SIZE_INTERNAL, EnemyShooting.COLOR_INTERNAL);
	}
	
	public void update(float delta, float distance, float beamSpeed)
	{
		this.squareExternal.moveX(-distance);
		this.squareInternal.moveX(-distance);
		
		if (this.squareExternal.getX() < (Renderer.RESOLUTION_X * 1.5f))
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
		return ((this.squareExternal.getX() + this.squareExternal.getWidth()) < 0);
	}
	
	public void destroy()
	{
		this.beams.clear();
	}
	
	public float getWidth()
	{
		return this.squareExternal.getX() + this.squareExternal.getWidth();
	}
	
	public boolean collide(Player player)
	{
		boolean result = GeometryUtils.collide(this.squareExternal, player.getShape());
		
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
		return this.squareExternal.insideScreen(Renderer.RESOLUTION_X);
	}
	
	public void draw(Renderer renderer)
	{
		this.squareExternal.draw(renderer);
		this.squareInternal.draw(renderer);
		
		for (Beam beam : this.beams)
		{
			beam.draw(renderer);
		}
	}
}