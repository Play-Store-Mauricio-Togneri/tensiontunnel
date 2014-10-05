package com.mauriciotogneri.obstacles.objects;

import android.graphics.Color;
import android.graphics.PointF;
import com.mauriciotogneri.obstacles.shapes.Polygon;
import com.mauriciotogneri.obstacles.shapes.Rectangle;
import com.mauriciotogneri.obstacles.shapes.Shape;

public class Background
{
	private final Rectangle base;

	private Shape shape1;
	private Shape shape2;
	private Shape shape3;
	private Shape shape4;

	private final Rectangle wallTop;
	private final Rectangle wallBottom;

	private float x1 = 0;
	private float x2 = 0;
	private float x3 = 0;
	private float x4 = 0;

	private final int width;
	private final int height;
	
	private static final int SHAPE_WIDTH = 40;
	private static final int SHAPE_SPACE = 30;
	private static final int WALL_HEIGHT = 5;
	
	private static final int COLOR_WALL = Color.argb(255, 65, 65, 65);
	
	private static final int COLOR_BASE = Color.argb(255, 255, 110, 110);
	private static final int COLOR_SHAPES = Color.argb(255, 250, 100, 100);
	
	public Background(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		this.x1 = 0;
		this.x2 = Background.SHAPE_SPACE + Background.SHAPE_WIDTH;
		this.x3 = (Background.SHAPE_SPACE * 2) + Background.SHAPE_WIDTH * 2;
		this.x4 = (Background.SHAPE_SPACE * 3) + Background.SHAPE_WIDTH * 3;

		this.base = new Rectangle(0, 0, width, height, Background.COLOR_BASE);

		this.wallTop = new Rectangle(0, height - Background.WALL_HEIGHT, width, Background.WALL_HEIGHT, Background.COLOR_WALL);
		this.wallBottom = new Rectangle(0, 0, width, Background.WALL_HEIGHT, Background.COLOR_WALL);
		
		updateRectangles();
	}

	public void update(float value)
	{
		this.x1 -= value;

		if ((this.x1 + Background.SHAPE_WIDTH) < 0)
		{
			this.x1 += (Background.SHAPE_WIDTH * 4) + (Background.SHAPE_SPACE * 4);
		}

		this.x2 -= value;
		
		if ((this.x2 + Background.SHAPE_WIDTH) < 0)
		{
			this.x2 += (Background.SHAPE_WIDTH * 4) + (Background.SHAPE_SPACE * 4);
		}
		
		this.x3 -= value;
		
		if ((this.x3 + Background.SHAPE_WIDTH) < 0)
		{
			this.x3 += (Background.SHAPE_WIDTH * 4) + (Background.SHAPE_SPACE * 4);
		}
		
		this.x4 -= value;
		
		if ((this.x4 + Background.SHAPE_WIDTH) < 0)
		{
			this.x4 += (Background.SHAPE_WIDTH * 4) + (Background.SHAPE_SPACE * 4);
		}
		
		updateRectangles();
	}
	
	private void updateRectangles()
	{
		PointF[] points1 = new PointF[4];
		points1[0] = new PointF(this.x1, this.height / 2f);
		points1[1] = new PointF(this.x1 + (Background.SHAPE_WIDTH / 2f), this.height / 4f);
		points1[2] = new PointF(this.x1 + (Background.SHAPE_WIDTH / 2f), this.height * (3f / 4f));
		points1[3] = new PointF(this.x1 + Background.SHAPE_WIDTH, this.height / 2f);
		this.shape1 = new Polygon(Background.COLOR_SHAPES, points1);

		PointF[] points2 = new PointF[3];
		points2[0] = new PointF(this.x2, 0);
		points2[1] = new PointF(this.x2 + (Background.SHAPE_WIDTH / 2f), this.height / 2f);
		points2[2] = new PointF(this.x2 + Background.SHAPE_WIDTH, 0);
		this.shape2 = new Polygon(Background.COLOR_SHAPES, points2);

		PointF[] points3 = new PointF[4];
		points3[0] = new PointF(this.x3, this.height);
		points3[1] = new PointF(this.x3 + (Background.SHAPE_WIDTH / 4f), this.height * (3f / 4f));
		points3[2] = new PointF(this.x3 + Background.SHAPE_WIDTH, this.height);
		points3[3] = new PointF(this.x3 + (Background.SHAPE_WIDTH * (3f / 4f)), this.height * (3f / 4f));
		this.shape3 = new Polygon(Background.COLOR_SHAPES, points3);
		
		PointF[] points4 = new PointF[4];
		points4[0] = new PointF(this.x4, this.height * (3f / 4f));
		points4[1] = new PointF(this.x4, this.height / 4f);
		points4[2] = new PointF(this.x4 + Background.SHAPE_WIDTH, this.height * (3f / 4f));
		points4[3] = new PointF(this.x4 + Background.SHAPE_WIDTH, this.height / 4f);
		this.shape4 = new Polygon(Background.COLOR_SHAPES, points4);
	}

	public void draw(int positionLocation, int colorLocation)
	{
		this.base.draw(positionLocation, colorLocation);

		this.shape1.draw(positionLocation, colorLocation);
		this.shape2.draw(positionLocation, colorLocation);
		this.shape3.draw(positionLocation, colorLocation);
		this.shape4.draw(positionLocation, colorLocation);
		
		this.wallTop.draw(positionLocation, colorLocation);
		this.wallBottom.draw(positionLocation, colorLocation);
	}
}