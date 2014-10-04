package com.mauriciotogneri.obstacles.debug;

import android.util.Log;

public class TimeCounter
{
	private long totalTime = 0;
	private int times = 0;
	private long start = 0;
	private final String name;
	private boolean enabled = true;

	public TimeCounter(String name, boolean enabled)
	{
		this.name = name;
		this.enabled = enabled;
	}

	public TimeCounter(String name)
	{
		this(name, true);
	}

	public void start()
	{
		this.start = System.nanoTime();
	}

	public void stop()
	{
		if (this.enabled)
		{
			this.totalTime += (System.nanoTime() - this.start) / 1000;
			this.times++;
			Log.e("DEBUG", this.name + (this.totalTime / this.times) + " us");
		}
	}
}