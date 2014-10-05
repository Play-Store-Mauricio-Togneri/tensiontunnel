package com.mauriciotogneri.obstacles.util;

import com.mauriciotogneri.obstacles.shapes.Rectangle;

public class GeometryUtils
{
	public static boolean collide(Rectangle rectangleA, Rectangle rectangleB)
	{
		float xA = rectangleA.getX();
		float yA = rectangleA.getY();
		float widthA = rectangleA.getWidth();
		float heightA = rectangleA.getHeight();
		
		float xB = rectangleB.getX();
		float yB = rectangleB.getY();
		float widthB = rectangleB.getWidth();
		float heightB = rectangleB.getHeight();
		
		boolean onLeft = ((xA + widthA) < xB);
		boolean onRight = ((xB + widthB) < xA);
		boolean onTop = ((yB + heightB) < yA);
		boolean onBottom = ((yA + heightA) < yB);
		
		return (!(onLeft || onRight || onTop || onBottom));
	}
}