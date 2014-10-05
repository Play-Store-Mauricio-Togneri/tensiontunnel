package com.mauriciotogneri.obstacles.util;

import android.graphics.Rect;
import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class GeometryUtils
{
	public static boolean collide(Rectangle rectangle1, Rectangle rectangle2)
	{
		Rect rectA = new Rect((int)rectangle1.getX(), (int)rectangle1.getY(), (int)(rectangle1.getWidth() + rectangle1.getX()), (int)(rectangle1.getHeight() + rectangle1.getY()));
		Rect rectB = new Rect((int)rectangle2.getX(), (int)rectangle2.getY(), (int)(rectangle2.getWidth() + rectangle2.getX()), (int)(rectangle2.getHeight() + rectangle2.getY()));
		
		return rectA.intersect(rectB);
	}
}