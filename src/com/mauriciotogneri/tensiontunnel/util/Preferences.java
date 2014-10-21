package com.mauriciotogneri.tensiontunnel.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences
{
	private static final String PREFERENCES = "PREFERENCES";
	private static final String ATTRIBUTE_CONNECTED = "CONNECTED";
	private static final String ATTRIBUTE_BEST = "BEST";

	private static Context context;

	public static void initialize(Context context)
	{
		Preferences.context = context;
	}

	public static void setConnectedPlayGameServices()
	{
		SharedPreferences.Editor editor = Preferences.context.getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE).edit();
		editor.putBoolean(Preferences.ATTRIBUTE_CONNECTED, true);
		editor.commit();
	}

	public static boolean isConnectedPlayGameServices()
	{
		SharedPreferences preferences = Preferences.context.getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);
		
		return preferences.getBoolean(Preferences.ATTRIBUTE_CONNECTED, false);
	}

	public static void setBestScore(int score)
	{
		SharedPreferences.Editor editor = Preferences.context.getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE).edit();
		editor.putInt(Preferences.ATTRIBUTE_BEST, score);
		editor.commit();
	}

	public static int getBestScore()
	{
		SharedPreferences preferences = Preferences.context.getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);
		
		return preferences.getInt(Preferences.ATTRIBUTE_BEST, 0);
	}
}