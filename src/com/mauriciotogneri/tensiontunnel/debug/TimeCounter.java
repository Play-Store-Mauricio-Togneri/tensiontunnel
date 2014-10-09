package com.mauriciotogneri.tensiontunnel.debug;

import android.util.Log;

public class TimeCounter
{
	private final Unit unit;
	private long totalTime = 0;
	private int times = 0;
	private long start = 0;
	private final String name;
	private boolean enabled = true;

	public enum Unit
	{
		MILLISECONDS(1000000, "ms"), MICROSECONDS(1000, "us"), NANOSECONDS(1, "ns");

		private int ratio;
		private String symbol;

		private Unit(int ratio, String symbol)
		{
			this.ratio = ratio;
			this.symbol = symbol;
		}

		public int getRatio()
		{
			return this.ratio;
		}

		public String getSymbol()
		{
			return this.symbol;
		}
	}
	
	public TimeCounter(Unit unit, String name, boolean enabled)
	{
		this.unit = unit;
		this.name = name;
		this.enabled = enabled;
	}
	
	public TimeCounter(Unit unit, String name)
	{
		this(unit, name, true);
	}
	
	public void start()
	{
		this.start = System.nanoTime();
	}
	
	public void stop()
	{
		if (this.enabled)
		{
			this.totalTime += (System.nanoTime() - this.start) / this.unit.getRatio();
			this.times++;
			Log.e("DEBUG", this.name + (this.totalTime / this.times) + " " + this.unit.getSymbol());
		}
	}
}