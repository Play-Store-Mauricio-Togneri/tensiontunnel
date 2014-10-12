package com.mauriciotogneri.tensiontunnel.engine;

public class Alarm
{
	private final int id;
	private final OnAlarmRing callback;
	private final long time;
	private float total = 0;
	
	public Alarm(int id, OnAlarmRing listener, long time)
	{
		this.id = id;
		this.callback = listener;
		this.time = time;
		this.total = 0;
	}

	public int getId()
	{
		return this.id;
	}
	
	public boolean step(float delta)
	{
		boolean remove = false;
		this.total += (delta * 1000f);
		
		if (this.total >= this.time)
		{
			remove = (!this.callback.onAlarmRing());
			this.total -= this.time;
		}
		
		return remove;
	}
	
	public interface OnAlarmRing
	{
		boolean onAlarmRing();
	}
}