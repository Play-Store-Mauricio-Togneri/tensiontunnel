package com.mauriciotogneri.obstacles.objects;

import com.mauriciotogneri.obstacles.engine.Renderer;

public class Score
{
	private float score = 0;
	
	private final Digit digit1;
	private final Digit digit2;
	private final Digit digit3;

	public Score()
	{
		this.digit1 = new Digit(Renderer.RESOLUTION_X - 5);
		this.digit2 = new Digit(Renderer.RESOLUTION_X - 9);
		this.digit3 = new Digit(Renderer.RESOLUTION_X - 13);
	}

	public void add(float value)
	{
		this.score += value;
	}
	
	public void clear()
	{
		this.score = 0;
	}
	
	public void draw(Renderer renderer)
	{
		String value = String.valueOf((int)(this.score / 10f));

		if (value.length() > 0)
		{
			this.digit1.draw(renderer, value.charAt(value.length() - 1));
		}

		if (value.length() > 1)
		{
			this.digit2.draw(renderer, value.charAt(value.length() - 2));
		}

		if (value.length() > 2)
		{
			this.digit3.draw(renderer, value.charAt(value.length() - 3));
		}
	}
}