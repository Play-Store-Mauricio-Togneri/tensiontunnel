package com.mauriciotogneri.obstacles.shapes;

import android.opengl.GLES20;

public class Triangle extends Shape
{
	public Triangle(float x1, float y1, float x2, float y2, float x3, float y3, int color)
	{
		super(GLES20.GL_TRIANGLE_STRIP, color);

		float[] vertices = new float[]
			{
				x1, y1, //
				x2, y2, //
				x3, y3
			};

		setVertices(vertices);
	}
}