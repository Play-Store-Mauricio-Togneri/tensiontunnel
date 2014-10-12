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
		public static Shape[] PLAYER_NORMAL;
		public static Shape[] PLAYER_HEAVY;
		public static Shape[] THRUST;
		public static Shape[] ENEMY_ROTATING;
		public static Shape[] ENEMY_SHOOTING;
		public static Shape[] BACKGROUND;
		public static Shape[] BACKGROUND_GROUND;
		
		private static boolean loaded = false;
		
		private static final String ATTRIBUTE_TYPE = "type";
		private static final String ATTRIBUTE_X = "x";
		private static final String ATTRIBUTE_Y = "y";
		private static final String ATTRIBUTE_COLOR = "color";
		private static final String ATTRIBUTE_SIZE = "size";
		private static final String ATTRIBUTE_WIDTH = "width";
		private static final String ATTRIBUTE_HEIGHT = "height";
		
		private static final String SHAPE_SQUARE = "SQUARE";
		private static final String SHAPE_RECTANGLE = "RECTANGLE";

		public static void initialize(Context context, float resolutionX, float resolutionY)
		{
			if (!Sprites.loaded)
			{
				Sprites.loaded = true;
				
				Sprites.BOX = Sprites.loadSprite(context, R.raw.sprite_box, resolutionX, resolutionY);
				Sprites.BEAM = Sprites.loadSprite(context, R.raw.sprite_beam, resolutionX, resolutionY);
				Sprites.PLAYER_NORMAL = Sprites.loadSprite(context, R.raw.sprite_player_normal, resolutionX, resolutionY);
				Sprites.PLAYER_HEAVY = Sprites.loadSprite(context, R.raw.sprite_player_heavy, resolutionX, resolutionY);
				Sprites.ENEMY_ROTATING = Sprites.loadSprite(context, R.raw.sprite_enemy_rotating, resolutionX, resolutionY);
				Sprites.ENEMY_SHOOTING = Sprites.loadSprite(context, R.raw.sprite_enemy_shooting, resolutionX, resolutionY);
				Sprites.BACKGROUND = Sprites.loadSprite(context, R.raw.sprite_background, resolutionX, resolutionY);
				Sprites.BACKGROUND_GROUND = Sprites.loadSprite(context, R.raw.sprite_background_ground, resolutionX, resolutionY);
				Sprites.THRUST = Sprites.loadSprite(context, R.raw.sprite_thrust, resolutionX, resolutionY);
			}
		}
		
		private static Shape[] loadSprite(Context context, int resourceId, float resolutionX, float resolutionY)
		{
			Shape[] result = new Shape[0];
			String data = Resources.readTextFile(context, resourceId);

			try
			{
				JSONArray array = new JSONArray(data);
				result = new Shape[array.length()];
				
				for (int i = 0; i < array.length(); i++)
				{
					result[i] = Sprites.getShape(array.getJSONObject(i), resolutionX, resolutionY);
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

			return result;
		}

		private static Shape getShape(JSONObject json, float resolutionX, float resolutionY)
		{
			Shape result = null;

			String type = json.optString(Sprites.ATTRIBUTE_TYPE);

			if (type.equals(Sprites.SHAPE_SQUARE))
			{
				float x = Sprites.getFloat(json, Sprites.ATTRIBUTE_X, resolutionX, resolutionY);
				float y = Sprites.getFloat(json, Sprites.ATTRIBUTE_Y, resolutionX, resolutionY);
				int color = Sprites.getColor(json, Sprites.ATTRIBUTE_COLOR);
				float size = Sprites.getFloat(json, Sprites.ATTRIBUTE_SIZE, resolutionX, resolutionY);

				result = new Square(x, y, size, color);
			}
			else if (type.equals(Sprites.SHAPE_RECTANGLE))
			{
				float x = Sprites.getFloat(json, Sprites.ATTRIBUTE_X, resolutionX, resolutionY);
				float y = Sprites.getFloat(json, Sprites.ATTRIBUTE_Y, resolutionX, resolutionY);
				int color = Sprites.getColor(json, Sprites.ATTRIBUTE_COLOR);
				float width = Sprites.getFloat(json, Sprites.ATTRIBUTE_WIDTH, resolutionX, resolutionY);
				float height = Sprites.getFloat(json, Sprites.ATTRIBUTE_HEIGHT, resolutionX, resolutionY);

				result = new Rectangle(x, y, width, height, color);
			}

			return result;
		}

		private static float getFloat(JSONObject json, String key, float resolutionX, float resolutionY)
		{
			float result = 0;

			String value = json.optString(key);

			if (value.equals("X"))
			{
				result = resolutionX;
			}
			else if (value.equals("Y"))
			{
				result = resolutionY;
			}
			else
			{
				result = (float)json.optDouble(key);
			}
			
			return result;
		}

		private static int getColor(JSONObject json, String key)
		{
			return Color.parseColor(json.optString(key));
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