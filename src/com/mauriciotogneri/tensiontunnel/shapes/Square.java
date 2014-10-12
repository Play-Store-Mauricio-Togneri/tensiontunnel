package com.mauriciotogneri.tensiontunnel.shapes;

public class Square extends Rectangle
{
	public Square(float x, float y, float width, int color)
	{
		super(x, y, width, width, color);
	}
	
	public Square(float width, int color)
	{
		this(0, 0, width, color);
	}
}