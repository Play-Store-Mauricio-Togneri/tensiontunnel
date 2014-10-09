package com.mauriciotogneri.tensiontunnel.statistics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;

public class Statistics
{
	private static Tracker tracker;
	private static final String CATEGORY_GAME = "GAME";
	
	public static void initialize(Tracker tracker)
	{
		Statistics.tracker = tracker;
	}
	
	public static void sendHitAppLaunched()
	{
		if (Statistics.tracker != null)
		{
			Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					Statistics.tracker.setScreenName("App Launched");
					Statistics.tracker.send(new HitBuilders.ScreenViewBuilder().build());
				}
			});
			thread.start();
		}
	}
	
	public static void sendHitNewGame()
	{
		if (Statistics.tracker != null)
		{
			Thread threadGameMode = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					EventBuilder builder = new HitBuilders.EventBuilder();
					builder.setCategory(Statistics.CATEGORY_GAME);
					builder.setAction("NEW_GAME");
					
					Statistics.tracker.send(builder.build());
				}
			});
			threadGameMode.start();
		}
	}
	
	public static void sendHitFinishGame(final int score)
	{
		if (Statistics.tracker != null)
		{
			Thread threadGameMode = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					EventBuilder builder = new HitBuilders.EventBuilder();
					builder.setCategory(Statistics.CATEGORY_GAME);
					builder.setAction("FINISH_GAME");
					builder.setValue(score);

					Statistics.tracker.send(builder.build());
				}
			});
			threadGameMode.start();
		}
	}
}