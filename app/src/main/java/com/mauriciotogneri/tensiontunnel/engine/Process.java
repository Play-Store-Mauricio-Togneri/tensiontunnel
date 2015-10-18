package com.mauriciotogneri.tensiontunnel.engine;

import android.util.SparseArray;

public class Process
{
    private final Game game;

    private final SparseArray<Alarm> alarms = new SparseArray<>();

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

    public final void process(float delta, float distance, float gameSpeed)
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

        update(delta, distance, gameSpeed);
    }

    @SuppressWarnings("unused")
    public void update(float delta, float distance, float gameSpeed)
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
}