package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import android.graphics.PointF;
import com.mauriciotogneri.obstacles.engine.Renderer;
import com.mauriciotogneri.obstacles.shapes.Polygon;
import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.util.GeometryUtils;

public class Background
{
	private final Rectangle base;
	
	private Polygon polygon1;
	private Polygon polygon2;
	private Polygon polygon3;
	private Polygon polygon4;
	
	private final Rectangle wallTop;
	private final Rectangle wallBottom;
	
	private static final int SHAPE_WIDTH = 40;
	private static final int SHAPE_SPACE = 30;
	private static final int WALL_HEIGHT = 5;
	private static final int NUMBER_OF_SHAPES = 4;

	// TODO: RED: 255, 110, 110
	private static final int COLOR_WALL = Color.argb(255, 90, 110, 120);
	private static final int COLOR_BASE = Color.argb(255, 170, 160, 210);
	private static final int COLOR_SHAPES = Color.argb(255, 165, 155, 205);

	public Background(int width, int height)
	{
		this.base = new Rectangle(0, 0, width, height, Background.COLOR_BASE);
		
		this.wallTop = new Rectangle(0, height - Background.WALL_HEIGHT, width, Background.WALL_HEIGHT, Background.COLOR_WALL);
		this.wallBottom = new Rectangle(0, 0, width, Background.WALL_HEIGHT, Background.COLOR_WALL);
		
		createShapes(height);
	}
	
	public void update(float value)
	{
		if ((this.polygon1.getX() + this.polygon1.getWidth()) < 0)
		{
			this.polygon1.moveX(Background.NUMBER_OF_SHAPES * (Background.SHAPE_WIDTH + Background.SHAPE_SPACE));
		}
		else
		{
			this.polygon1.moveX(-value);
		}
		
		if ((this.polygon2.getX() + this.polygon2.getWidth()) < 0)
		{
			this.polygon2.moveX(Background.NUMBER_OF_SHAPES * (Background.SHAPE_WIDTH + Background.SHAPE_SPACE));
		}
		else
		{
			this.polygon2.moveX(-value);
		}

		if ((this.polygon3.getX() + this.polygon3.getWidth()) < 0)
		{
			this.polygon3.moveX(Background.NUMBER_OF_SHAPES * (Background.SHAPE_WIDTH + Background.SHAPE_SPACE));
		}
		else
		{
			this.polygon3.moveX(-value);
		}

		if ((this.polygon4.getX() + this.polygon4.getWidth()) < 0)
		{
			this.polygon4.moveX(Background.NUMBER_OF_SHAPES * (Background.SHAPE_WIDTH + Background.SHAPE_SPACE));
		}
		else
		{
			this.polygon4.moveX(-value);
		}
	}

	public boolean collide(MainCharacter mainCharacter)
	{
		boolean result = false;

		if (GeometryUtils.collide(mainCharacter.getShape(), this.wallTop))
		{
			result = true;
		}
		else if (GeometryUtils.collide(mainCharacter.getShape(), this.wallBottom))
		{
			result = true;
		}
		
		return result;
	}
	
	public void draw(Renderer renderer)
	{
		this.base.draw(renderer);
		
		this.polygon1.draw(renderer);
		this.polygon2.draw(renderer);
		this.polygon3.draw(renderer);
		this.polygon4.draw(renderer);

		this.wallTop.draw(renderer);
		this.wallBottom.draw(renderer);
	}

	private void createShapes(int height)
	{
		PointF[] points1 = new PointF[4];
		points1[0] = new PointF(0, height / 2f);
		points1[1] = new PointF(Background.SHAPE_WIDTH / 2f, height / 4f);
		points1[2] = new PointF(Background.SHAPE_WIDTH / 2f, height * (3f / 4f));
		points1[3] = new PointF(Background.SHAPE_WIDTH, height / 2f);
		this.polygon1 = new Polygon(Background.COLOR_SHAPES, points1);
		
		PointF[] points2 = new PointF[3];
		points2[0] = new PointF(Background.SHAPE_SPACE + Background.SHAPE_WIDTH, 0);
		points2[1] = new PointF(Background.SHAPE_SPACE + Background.SHAPE_WIDTH + (Background.SHAPE_WIDTH / 2f), height / 2f);
		points2[2] = new PointF(Background.SHAPE_SPACE + Background.SHAPE_WIDTH + Background.SHAPE_WIDTH, 0);
		this.polygon2 = new Polygon(Background.COLOR_SHAPES, points2);
		
		PointF[] points3 = new PointF[4];
		points3[0] = new PointF(((Background.SHAPE_SPACE * 2) + Background.SHAPE_WIDTH * 2), height);
		points3[1] = new PointF(((Background.SHAPE_SPACE * 2) + Background.SHAPE_WIDTH * 2) + (Background.SHAPE_WIDTH / 4f), height * (3f / 4f));
		points3[2] = new PointF(((Background.SHAPE_SPACE * 2) + Background.SHAPE_WIDTH * 2) + Background.SHAPE_WIDTH, height);
		points3[3] = new PointF(((Background.SHAPE_SPACE * 2) + Background.SHAPE_WIDTH * 2) + (Background.SHAPE_WIDTH * (3f / 4f)), height * (3f / 4f));
		this.polygon3 = new Polygon(Background.COLOR_SHAPES, points3);
		
		PointF[] points4 = new PointF[4];
		points4[0] = new PointF(((Background.SHAPE_SPACE * 3) + Background.SHAPE_WIDTH * 3), height * (3f / 4f));
		points4[1] = new PointF(((Background.SHAPE_SPACE * 3) + Background.SHAPE_WIDTH * 3), height / 4f);
		points4[2] = new PointF(((Background.SHAPE_SPACE * 3) + Background.SHAPE_WIDTH * 3) + Background.SHAPE_WIDTH, height * (3f / 4f));
		points4[3] = new PointF(((Background.SHAPE_SPACE * 3) + Background.SHAPE_WIDTH * 3) + Background.SHAPE_WIDTH, height / 4f);
		this.polygon4 = new Polygon(Background.COLOR_SHAPES, points4);
	}
}