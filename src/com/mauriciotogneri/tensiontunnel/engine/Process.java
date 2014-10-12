package com.mauriciotogneri.tensiontunnel.engine;

import android.util.SparseArray;
import com.mauriciotogneri.tensiontunnel.engine.Alarm.OnAlarmRing;

public class Process
{
	private final Game game;
	
	private int nextAlarmId = 1;
	private final SparseArray<Alarm> alarms = new SparseArray<Alarm>();
	
	public Process()
	{
		this.game = Game.getInstance();
	}
	
	public void start()
	{
		this.game.addProcess(this);
	}

	public void finish()
	{
		this.game.removeProcess(this);
	}

	public final void process(float delta, float distance)
	{
		int size = this.alarms.size();
		
		if (size > 0)
		{
			for (int i = 0; i < size; i++)
			{
				Alarm alarm = this.alarms.valueAt(i);
				
				if (alarm.step(delta))
				{
					this.alarms.remove(alarm.getId());
				}
			}
		}
		
		update(delta, distance);
	}
	
	@SuppressWarnings("unused")
	public void update(float delta, float distance)
	{
	}
	
	public boolean isVisible()
	{
		return false;
	}

	@SuppressWarnings("unused")
	public void draw(Renderer renderer)
	{
	}

	public int setAlarm(OnAlarmRing listener, int milliseconds)
	{
		int id = this.nextAlarmId++;
		
		this.alarms.put(id, new Alarm(id, listener, milliseconds));
		
		return id;
	}
}