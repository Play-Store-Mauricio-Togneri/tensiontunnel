package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Game;
import com.mauriciotogneri.tensiontunnel.engine.Process;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class Wall extends Process
{
	private final Game game;
	private boolean passed = false;
	private final float center;
	
	private final Sprite spriteTop;
	private final Sprite spriteBottom;
	
	private static final int COLOR = Color.argb(255, 90, 110, 120);

	public Wall(Game game, float x, float center, float gap, float wallWidth)
	{
		this.game = game;
		this.center = center;
		
		float y = center + (gap / 2f);
		this.spriteTop = new Sprite(x, y, new Rectangle(wallWidth, Renderer.RESOLUTION_Y - y - Background.getHeight(), Wall.COLOR));
		
		this.spriteBottom = new Sprite(x, Background.getHeight(), new Rectangle(wallWidth, center - (gap / 2f) - Background.getHeight(), Wall.COLOR));
	}

	@Override
	public void update(float delta, float distance, float gameSpeed)
	{
		this.spriteTop.moveX(-distance);
		this.spriteBottom.moveX(-distance);

		if (isPassed())
		{
			this.game.wallPassed();
		}
		
		if (this.spriteTop.getRight() < 0)
		{
			finish();
			this.game.createWall();
		}
	}

	public boolean isPassed()
	{
		boolean result = false;
		
		if (!this.passed)
		{
			result = getRight() < Player.getX();
			
			if (result)
			{
				this.passed = true;
			}
		}

		return result;
	}
	
	public float getRight()
	{
		return this.spriteTop.getRight();
	}
	
	public float getCenter()
	{
		return this.center;
	}

	public boolean collide(Player player)
	{
		return ((GeometryUtils.collide(this.spriteTop, player.getSprite())) || (GeometryUtils.collide(this.spriteBottom, player.getSprite())));
	}

	@Override
	public boolean isVisible()
	{
		return this.spriteTop.insideScreen(Renderer.RESOLUTION_X);
	}
	
	@Override
	public void draw(Renderer renderer)
	{
		this.spriteTop.draw(renderer);
		this.spriteBottom.draw(renderer);
	}
}