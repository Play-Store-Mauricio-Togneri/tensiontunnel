package com.mauriciotogneri.obstacles.input;

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
}