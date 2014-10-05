package com.mauriciotogneri.obstacles.shapes;

import android.graphics.PointF;
import android.opengl.GLES20;

public class Polygon extends Shape
{
	private final PointF[] points;

	public Polygon(int color, PointF... points)
	{
		super(GLES20.GL_TRIANGLE_STRIP, color);

		this.points = points;

		updateVertices();
	}
	
	public void moveX(float value)
	{
		for (PointF point : this.points)
		{
			point.x += value;
		}

		updateVertices();
	}
	
	public void moveY(float value)
	{
		for (PointF point : this.points)
		{
			point.y += value;
		}

		updateVertices();
	}

	public void move(float valueX, float valueY)
	{
		for (PointF point : this.points)
		{
			point.y += valueX;
			point.y += valueY;
		}

		updateVertices();
	}

	private void updateVertices()
	{
		float[] vertices = new float[this.points.length * 2];
		
		int index = 0;
		
		for (PointF point : this.points)
		{
			vertices[index++] = point.x;
			vertices[index++] = point.y;
		}

		setVertices(vertices);
	}
}