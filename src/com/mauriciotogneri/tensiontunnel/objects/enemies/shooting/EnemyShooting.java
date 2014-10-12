package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.audio.AudioManager;
import com.mauriciotogneri.tensiontunnel.engine.Game;
import com.mauriciotogneri.tensiontunnel.engine.Process;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public abstract class EnemyShooting extends Process
{
	protected final Sprite sprite;

	private final Game game;
	private final float beamSpeed;
	private float timeCounter = 0;
	private final float beamFrequency;
	
	protected static final int SIZE_EXTERNAL = 3;
	
	public EnemyShooting(Game game, float x, float y, float beamFrequency, float beamSpeed)
	{
		this.game = game;
		this.beamFrequency = beamFrequency;
		this.beamSpeed = beamSpeed;

		this.sprite = new Sprite(x, y, Resources.Sprites.ENEMY_SHOOTING);
	}
	
	@Override
	public void update(float delta, float distance)
	{
		this.sprite.moveX(-distance);

		if (this.sprite.getX() < (Renderer.RESOLUTION_X * 1.5f))
		{
			this.timeCounter += delta;

			if (this.timeCounter > this.beamFrequency)
			{
				if (isVisible())
				{
					AudioManager.getInstance().playSound(Resources.Sounds.BEAM);
				}

				this.timeCounter -= this.beamFrequency;

				Beam beam = getNewBeam(this.beamSpeed);
				beam.start();
			}
		}

		if (this.sprite.getRight() < 0)
		{
			// this.beams.clear();
			finish();
			this.game.createEnemy();
		}
	}

	protected abstract Beam getNewBeam(float beamSpeed);

	public boolean collide(Player player)
	{
		return GeometryUtils.collide(this.sprite, player.getSprite());
	}
	
	@Override
	public boolean isVisible()
	{
		return this.sprite.insideScreen(Renderer.RESOLUTION_X);
	}
	
	@Override
	public void draw(Renderer renderer)
	{
		this.sprite.draw(renderer);
	}
}