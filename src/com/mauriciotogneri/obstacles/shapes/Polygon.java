package com.mauriciotogneri.obstacles.shapes;

import android.graphics.PointF;
import android.opengl.GLES20;

public class Polygon extends Shape
{
	public Polygon(int color, PointF... points)
	{
		super(GLES20.GL_TRIANGLE_STRIP, color);
		
		float[] vertices = new float[points.length * 2];

		int index = 0;

		for (PointF point : points)
		{
			vertices[index++] = point.x;
			vertices[index++] = point.y;
		}
		
		setVertices(vertices);
	}
}