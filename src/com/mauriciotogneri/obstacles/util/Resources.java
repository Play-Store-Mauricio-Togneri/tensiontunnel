package com.mauriciotogneri.obstacles.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;

public class Resources
{
	public static class Sounds
	{
		private static final String ROOT = "audio/sounds/";
		
		public static final String BEAM = Sounds.ROOT + "beam.ogg";
		public static final String EXPLOSION = Sounds.ROOT + "explosion.ogg";
	}
	
	public static class Music
	{
		private static final String ROOT = "audio/music/";
		
		public static final String MUSIC = Music.ROOT + "music.ogg";
	}
	
	public static String readTextFile(Context context, int resourceId)
	{
		StringBuilder builder = new StringBuilder();
		
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		
		try
		{
			inputStream = context.getResources().openRawResource(resourceId);
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			
			String nextLine;
			
			while ((nextLine = bufferedReader.readLine()) != null)
			{
				builder.append(nextLine);
				builder.append('\n');
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			Resources.closeResource(inputStream);
			Resources.closeResource(inputStreamReader);
			Resources.closeResource(bufferedReader);
		}
		
		return builder.toString();
	}
	
	private static void closeResource(Closeable resource)
	{
		if (resource != null)
		{
			try
			{
				resource.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}