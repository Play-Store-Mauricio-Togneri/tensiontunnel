package com.mauriciotogneri.tensiontunnel.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.R;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.shapes.Shape;
import com.mauriciotogneri.tensiontunnel.shapes.Square;

public class Resources
{
	public static class Sounds
	{
		private static final String ROOT = "audio/sounds/";

		public static final String BEAM = Sounds.ROOT + "beam.ogg";
		public static final String EXPLOSION = Sounds.ROOT + "explosion.ogg";
		public static final String POINT = Sounds.ROOT + "point.ogg";
		public static final String BOX = Sounds.ROOT + "box.ogg";
	}

	public static class Music
	{
		private static final String ROOT = "audio/music/";

		public static final String MUSIC = Music.ROOT + "music.ogg";
	}

	public static class Sprites
	{
		public static Shape[] BOX;
		public static Shape[] BEAM;
		public static Shape[] PLAYER;
		public static Shape[] ENEMY_ROTATING;
		public static Shape[] ENEMY_SHOOTING;

		private static final String ATTRIBUTE_TYPE = "type";
		private static final String ATTRIBUTE_X = "x";
		private static final String ATTRIBUTE_Y = "y";
		private static final String ATTRIBUTE_COLOR = "color";
		private static final String ATTRIBUTE_SIZE = "size";
		private static final String ATTRIBUTE_WIDTH = "width";
		private static final String ATTRIBUTE_HEIGHT = "height";

		private static final String SHAPE_SQUARE = "SQUARE";
		private static final String SHAPE_RECTANGLE = "RECTANGLE";
		
		public static void initialize(Context context)
		{
			Sprites.BOX = Sprites.loadSprite(context, R.raw.sprite_box);
			Sprites.BEAM = Sprites.loadSprite(context, R.raw.sprite_beam);
			Sprites.PLAYER = Sprites.loadSprite(context, R.raw.sprite_player);
			Sprites.ENEMY_ROTATING = Sprites.loadSprite(context, R.raw.sprite_enemy_rotating);
			Sprites.ENEMY_SHOOTING = Sprites.loadSprite(context, R.raw.sprite_enemy_shooting);
		}

		private static Shape[] loadSprite(Context context, int resourceId)
		{
			Shape[] result = new Shape[0];
			String data = Resources.readTextFile(context, resourceId);
			
			try
			{
				JSONArray array = new JSONArray(data);
				result = new Shape[array.length()];

				for (int i = 0; i < array.length(); i++)
				{
					result[i] = Sprites.getShape(array.getJSONObject(i));
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			
			return result;
		}
		
		private static Shape getShape(JSONObject json)
		{
			Shape result = null;
			
			String type = json.optString(Sprites.ATTRIBUTE_TYPE);
			
			if (type.equals(Sprites.SHAPE_SQUARE))
			{
				float x = (float)json.optDouble(Sprites.ATTRIBUTE_X);
				float y = (float)json.optDouble(Sprites.ATTRIBUTE_Y);
				String color = json.optString(Sprites.ATTRIBUTE_COLOR);
				float size = (float)json.optDouble(Sprites.ATTRIBUTE_SIZE);
				
				result = new Square(x, y, size, Color.parseColor(color));
			}
			else if (type.equals(Sprites.SHAPE_RECTANGLE))
			{
				float x = (float)json.optDouble(Sprites.ATTRIBUTE_X);
				float y = (float)json.optDouble(Sprites.ATTRIBUTE_Y);
				String color = json.optString(Sprites.ATTRIBUTE_COLOR);
				float width = (float)json.optDouble(Sprites.ATTRIBUTE_WIDTH);
				float height = (float)json.optDouble(Sprites.ATTRIBUTE_HEIGHT);
				
				result = new Rectangle(x, y, width, height, Color.parseColor(color));
			}
			
			return result;
		}
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