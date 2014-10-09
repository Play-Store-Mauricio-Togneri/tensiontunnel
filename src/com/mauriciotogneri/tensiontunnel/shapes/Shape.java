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
	
	private static final int BYTES_PER_FLOAT = 4;
	
	public Shape(int mode, int color)
	{
		this.mode = mode;
		this.color = color;
	}
	
	protected void setVertices(float[] vertices)
	{
		this.x = getMinX(vertices);
		this.y = getMinY(vertices);

		this.width = getMaxX(vertices) - this.x;
		this.height = getMaxY(vertices) - this.y;

		for (int i = 0; i < vertices.length; i += 2)
		{
			vertices[i] -= this.x;
			vertices[i + 1] -= this.y;
		}
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * Shape.BYTES_PER_FLOAT);
		byteBuffer.order(ByteOrder.nativeOrder());
		this.vertexData = byteBuffer.asFloatBuffer();
		this.vertexData.put(vertices);

		this.length = (vertices.length / 2);
	}
	
	private float getMinX(float[] vertices)
	{
		float result = Float.MAX_VALUE;
		
		for (int i = 0; i < vertices.length; i += 2)
		{
			float x = vertices[i];

			if (x < result)
			{
				result = x;
			}
		}

		return result;
	}
	
	private float getMinY(float[] vertices)
	{
		float result = Float.MAX_VALUE;
		
		for (int i = 1; i < vertices.length; i += 2)
		{
			float y = vertices[i];

			if (y < result)
			{
				result = y;
			}
		}

		return result;
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
	
	public void draw(Renderer renderer)
	{
		draw(renderer, 1);
	}
	
	public void draw(Renderer renderer, float alpha)
	{
		renderer.drawShape(this.vertexData, this.x, this.y, this.color, alpha, this.mode, this.length);
	}
}