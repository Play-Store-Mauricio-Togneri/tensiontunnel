package com.mauriciotogneri.tensiontunnel.statistics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;

public class Statistics
{
	private static Tracker tracker;
	
	private static final String CATEGORY_GAME = "GAME";
	private static final String CATEGORY_COLLISION = "COLLISION";
	private static final String EVENT_APP_LAUNCHED = "APP_LAUNCHED";
	private static final String EVENT_NEW_GAME = "NEW_GAME";
	
	public enum CollisionType
	{
		BACKGROUND, WALL, ENEMY_SHOOTING, ENEMY_ROTATING, BEAM;
	}
	
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
					Statistics.tracker.setScreenName(Statistics.EVENT_APP_LAUNCHED);
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
					builder.setAction(Statistics.EVENT_NEW_GAME);

					Statistics.tracker.send(builder.build());
				}
			});
			threadGameMode.start();
		}
	}
	
	public static void sendHitCollision(final CollisionType collisionType)
	{
		if (Statistics.tracker != null)
		{
			Thread threadGameMode = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					EventBuilder builder = new HitBuilders.EventBuilder();
					builder.setCategory(Statistics.CATEGORY_COLLISION);
					builder.setAction(collisionType.toString());

					Statistics.tracker.send(builder.build());
				}
			});
			threadGameMode.start();
		}
	}
}