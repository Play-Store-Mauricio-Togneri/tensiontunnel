package com.mauriciotogneri.tensiontunnel.shapes;

import android.opengl.GLES20;

public class Rectangle extends Shape
{
	public Rectangle(float x, float y, float width, float height, int color)
	{
		super(GLES20.GL_TRIANGLE_STRIP, color, 0, 0, new float[]
			{
				x, y + height, //
				x, y, //
				x + width, y + height, //
				x + width, y
			});
	}

	public Rectangle(float width, float height, int color)
	{
		this(0, 0, width, height, color);
	}
}