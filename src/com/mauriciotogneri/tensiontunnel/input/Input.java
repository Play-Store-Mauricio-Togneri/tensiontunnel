package com.mauriciotogneri.tensiontunnel.input;

public class Input
{
	public boolean jumpPressed = false;
	public boolean advancePressed = false;

	public void press(float x, int resolutionX)
	{
		if (x < (resolutionX / 2))
		{
			this.jumpPressed = true;
		}
		else
		{
			this.advancePressed = true;
		}
	}

	public void release(float x, int resolutionX)
	{
		if (x < (resolutionX / 2))
		{
			this.jumpPressed = false;
		}
		else
		{
			this.advancePressed = false;
		}
	}

	public void clear()
	{
		this.jumpPressed = false;
		this.advancePressed = false;
	}
}