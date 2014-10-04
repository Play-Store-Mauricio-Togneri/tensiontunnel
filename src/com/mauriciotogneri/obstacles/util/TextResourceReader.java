package com.mauriciotogneri.obstacles.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;

public class TextResourceReader
{
	public static String readTextFileFromResource(Context context, int resourceId)
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
			TextResourceReader.closeResource(inputStream);
			TextResourceReader.closeResource(inputStreamReader);
			TextResourceReader.closeResource(bufferedReader);
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