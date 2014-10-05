package com.mauriciotogneri.obstacles.shapes;

import android.opengl.GLES20;

public class Rectangle extends Shape
{
	public Rectangle(float x, float y, float width, float height, int color)
	{
		super(GLES20.GL_TRIANGLE_STRIP, color);
		
		float[] vertices = new float[]
			{
				x, y + height, //
				x, y, //
				x + width, y + height, //
				x + width, y
			};
		
		setVertices(vertices);
	}
}