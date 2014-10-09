package com.mauriciotogneri.tensiontunnel.objects.score;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;

public class Score
{
	private float score = 0;

	private final Digit digit1;
	private final Digit digit2;
	private final Digit digit3;
	private final Digit digit4;
	private final Digit digit5;
	
	public Score()
	{
		this.digit1 = new Digit(Renderer.RESOLUTION_X - (Digit.DIGIT_WIDTH + 1));
		this.digit2 = new Digit(Renderer.RESOLUTION_X - ((Digit.DIGIT_WIDTH + 1) * 2));
		this.digit3 = new Digit(Renderer.RESOLUTION_X - ((Digit.DIGIT_WIDTH + 1) * 3));
		this.digit4 = new Digit(Renderer.RESOLUTION_X - ((Digit.DIGIT_WIDTH + 1) * 4));
		this.digit5 = new Digit(Renderer.RESOLUTION_X - ((Digit.DIGIT_WIDTH + 1) * 5));
	}
	
	public void add(float value)
	{
		this.score += value;
	}

	public int getValue()
	{
		return (int)(this.score / 10f);
	}

	public void clear()
	{
		this.score = 0;
	}

	public void draw(Renderer renderer)
	{
		String value = String.valueOf(getValue());
		
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

		if (value.length() > 3)
		{
			this.digit4.draw(renderer, value.charAt(value.length() - 4));
		}

		if (value.length() > 4)
		{
			this.digit5.draw(renderer, value.charAt(value.length() - 5));
		}
	}
}