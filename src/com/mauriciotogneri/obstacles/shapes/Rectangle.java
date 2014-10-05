package com.mauriciotogneri.obstacles.shapes;

import android.opengl.GLES20;

public class Rectangle extends Shape
{
	private float x;
	private float y;
	private float width;
	private float height;
	
	public Rectangle(float x, float y, float width, float height, int color)
	{
		super(GLES20.GL_TRIANGLE_STRIP, color);

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		updateVertices();
	}
	
	public void moveX(float value)
	{
		this.x += value;
		
		updateVertices();
	}

	public void moveY(float value)
	{
		this.y += value;
		
		updateVertices();
	}

	public void move(float valueX, float valueY)
	{
		this.x += valueX;
		this.y += valueY;
		
		updateVertices();
	}

	public void setX(float x)
	{
		this.x = x;
		
		updateVertices();
	}
	
	public void setY(float y)
	{
		this.y = y;
		
		updateVertices();
	}
	
	public void setWidth(float width)
	{
		this.width = width;
		
		updateVertices();
	}
	
	public void setHeight(float height)
	{
		this.height = height;
		
		updateVertices();
	}
	
	private void updateVertices()
	{
		float[] vertices = new float[]
			{
				this.x, this.y + this.height, //
				this.x, this.y, //
				this.x + this.width, this.y + this.height, //
				this.x + this.width, this.y
			};

		setVertices(vertices);
	}
}