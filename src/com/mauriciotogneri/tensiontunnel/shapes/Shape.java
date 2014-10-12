package com.mauriciotogneri.tensiontunnel.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;

public class Shape
{
	private final int mode;
	private int color;
	private FloatBuffer vertexData;
	private int length = 0;

	private float x = 0;
	private float y = 0;
	private float width = 0;
	private float height = 0;
	
	public Shape(int mode, int color, float x, float y, float[] vertices)
	{
		this.mode = mode;
		this.color = color;

		this.x = x;
		this.y = y;

		setVertices(vertices);
	}
	
	public Shape(int mode, int color, float[] vertices)
	{
		this(mode, color, 0, 0, vertices);
	}
	
	private void setVertices(float[] vertices)
	{
		this.width = getMaxX(vertices);
		this.height = getMaxY(vertices);

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		this.vertexData = byteBuffer.asFloatBuffer();
		this.vertexData.put(vertices);

		this.length = (vertices.length / 2);
	}
	
	private float getMaxX(float[] vertices)
	{
		float result = Float.MIN_VALUE;
		
		for (int i = 0; i < vertices.length; i += 2)
		{
			float x = vertices[i];

			if (x > result)
			{
				result = x;
			}
		}

		return result;
	}
	
	private float getMaxY(float[] vertices)
	{
		float result = Float.MIN_VALUE;
		
		for (int i = 1; i < vertices.length; i += 2)
		{
			float y = vertices[i];

			if (y > result)
			{
				result = y;
			}
		}

		return result;
	}

	public float getX()
	{
		return this.x;
	}

	public float getY()
	{
		return this.y;
	}

	public float getWidth()
	{
		return this.width;
	}

	public float getHeight()
	{
		return this.height;
	}

	public void moveX(float value)
	{
		this.x += value;
	}

	public void moveY(float value)
	{
		this.y += value;
	}

	public void move(float valueX, float valueY)
	{
		this.x += valueX;
		this.y += valueY;
	}

	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setColor(int color)
	{
		this.color = color;
	}

	public boolean insideScreen(int width)
	{
		boolean onLeft = (this.x + width) < 0;
		boolean onRight = this.x > width;

		return (!(onLeft || onRight));
	}
	
	public void draw(Renderer renderer, float parentX, float parentY)
	{
		draw(renderer, parentX, parentY, 1);
	}
	
	public void draw(Renderer renderer, float parentX, float parentY, float alpha)
	{
		renderer.drawShape(this.vertexData, parentX + this.x, parentY + this.y, this.color, alpha, this.mode, this.length);
	}
}