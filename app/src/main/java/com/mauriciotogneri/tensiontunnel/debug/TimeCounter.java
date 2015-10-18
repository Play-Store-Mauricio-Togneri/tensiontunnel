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

        private final int ratio;
        private final String symbol;

        Unit(int ratio, String symbol)
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

    private TimeCounter(Unit unit, String name, boolean enabled)
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
            Log.e(this.name, this.name + ": " + (this.totalTime / this.times) + " " + this.unit.getSymbol());
        }
    }
}