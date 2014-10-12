package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import android.graphics.PointF;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Polygon;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;

public class Background
{
	private final Sprite base;

	private Sprite triangle1;
	private Sprite triangle2;

	private final Sprite wallTop1;
	private final Sprite wallTop2;

	private final Sprite wallBottom1;
	private final Sprite wallBottom2;

	private static final int SHAPE_WIDTH = 120;
	private static final int WALL_HEIGHT = 7;
	private static final int NUMBER_OF_SHAPES = 2;
	
	private static final int COLOR_WALL = Color.argb(255, 90, 110, 120);
	private static final int COLOR_BASE = Color.argb(255, 170, 160, 210);
	private static final int COLOR_SHAPE = Color.argb(255, 165, 155, 205);
	
	private static final float RELATIVE_SPEED = 0.75f;
	
	public Background()
	{
		float width = Renderer.RESOLUTION_X;
		float height = Renderer.RESOLUTION_Y;

		this.base = new Sprite(0, 0, new Rectangle(width, height, Background.COLOR_BASE));
		
		this.wallTop1 = new Sprite(0, height - Background.WALL_HEIGHT, new Rectangle(width, Background.WALL_HEIGHT, Background.COLOR_WALL));
		this.wallTop2 = new Sprite(width, height - Background.WALL_HEIGHT, new Rectangle(width, Background.WALL_HEIGHT, Background.COLOR_WALL));
		
		this.wallBottom1 = new Sprite(0, 0, new Rectangle(width, Background.WALL_HEIGHT, Background.COLOR_WALL));
		this.wallBottom2 = new Sprite(width, 0, new Rectangle(width, Background.WALL_HEIGHT, Background.COLOR_WALL));

		createShapes(height);
	}

	public void update(float distance)
	{
		this.wallTop1.moveX(-distance);
		
		if ((this.wallTop1.getX() + this.wallTop1.getWidth()) < 0)
		{
			this.wallTop1.moveX(Renderer.RESOLUTION_X * 2);
		}
		
		this.wallTop2.moveX(-distance);

		if ((this.wallTop2.getX() + this.wallTop2.getWidth()) < 0)
		{
			this.wallTop2.moveX(Renderer.RESOLUTION_X * 2);
		}
		
		// =========================================================

		this.wallBottom1.moveX(-distance);
		
		if ((this.wallBottom1.getX() + this.wallBottom1.getWidth()) < 0)
		{
			this.wallBottom1.moveX(Renderer.RESOLUTION_X * 2);
		}
		
		this.wallBottom2.moveX(-distance);

		if ((this.wallBottom2.getX() + this.wallBottom2.getWidth()) < 0)
		{
			this.wallBottom2.moveX(Renderer.RESOLUTION_X * 2);
		}

		// =========================================================
		
		float backgroundSpeed = distance * Background.RELATIVE_SPEED;
		
		if ((this.triangle1.getX() + this.triangle1.getWidth()) < 0)
		{
			this.triangle1.moveX(Background.NUMBER_OF_SHAPES * Background.SHAPE_WIDTH);
		}
		else
		{
			this.triangle1.moveX(-backgroundSpeed);
		}

		if ((this.triangle2.getX() + this.triangle2.getWidth()) < 0)
		{
			this.triangle2.moveX(Background.NUMBER_OF_SHAPES * Background.SHAPE_WIDTH);
		}
		else
		{
			this.triangle2.moveX(-backgroundSpeed);
		}
	}

	public boolean collide(Player player)
	{
		boolean result = false;

		if (GeometryUtils.collide(player.getSprite(), this.wallTop1) || GeometryUtils.collide(player.getSprite(), this.wallTop2))
		{
			result = true;
		}
		else if (GeometryUtils.collide(player.getSprite(), this.wallBottom1) || GeometryUtils.collide(player.getSprite(), this.wallBottom2))
		{
			result = true;
		}

		return result;
	}

	public void draw(Renderer renderer)
	{
		this.base.draw(renderer);

		this.triangle1.draw(renderer);
		this.triangle2.draw(renderer);
		
		this.wallTop1.draw(renderer);
		this.wallTop2.draw(renderer);

		this.wallBottom1.draw(renderer);
		this.wallBottom2.draw(renderer);
	}
	
	private void createShapes(float height)
	{
		PointF[] points1 = new PointF[3];
		points1[0] = new PointF(0, 0);
		points1[1] = new PointF(Background.SHAPE_WIDTH / 2f, height);
		points1[2] = new PointF(Background.SHAPE_WIDTH, 0);
		this.triangle1 = new Sprite(0, 0, new Polygon(Background.COLOR_SHAPE, points1));

		PointF[] points2 = new PointF[3];
		points2[0] = new PointF(Background.SHAPE_WIDTH, 0);
		points2[1] = new PointF(Background.SHAPE_WIDTH * (3f / 2f), height);
		points2[2] = new PointF(Background.SHAPE_WIDTH * 2, 0);
		this.triangle2 = new Sprite(0, 0, new Polygon(Background.COLOR_SHAPE, points2));
	}

	public static int getHeight()
	{
		return Background.WALL_HEIGHT;
	}
}