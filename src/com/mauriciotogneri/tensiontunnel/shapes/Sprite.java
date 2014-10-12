package com.mauriciotogneri.tensiontunnel.shapes;

import java.util.ArrayList;
import java.util.List;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;

public class Sprite
{
	private float x;
	private float y;
	private float width = 0;
	private float height = 0;
	private final List<Shape> shapes = new ArrayList<Shape>();

	public Sprite(float x, float y, Shape... shapes)
	{
		this.x = x;
		this.y = y;

		for (Shape shape : shapes)
		{
			add(shape);
		}
	}

	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}

	public float getRight()
	{
		return this.x + this.width;
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

	public void add(Shape shape)
	{
		this.shapes.add(shape);
		updateSize();
	}
	
	private void updateSize()
	{
		this.width = Float.MIN_VALUE;
		this.height = Float.MIN_VALUE;

		for (Shape shape : this.shapes)
		{
			if (shape.getWidth() > this.width)
			{
				this.width = shape.getWidth();
			}
			
			if (shape.getHeight() > this.height)
			{
				this.height = shape.getHeight();
			}
		}
	}
	
	public boolean insideScreen(int width)
	{
		boolean onLeft = (this.x + width) < 0;
		boolean onRight = this.x > width;
		
		return (!(onLeft || onRight));
	}

	public void draw(Renderer renderer)
	{
		for (Shape shape : this.shapes)
		{
			shape.draw(renderer, this.x, this.y);
		}
	}

	public void draw(Renderer renderer, float alpha)
	{
		for (Shape shape : this.shapes)
		{
			shape.draw(renderer, this.x, this.y, alpha);
		}
	}
}