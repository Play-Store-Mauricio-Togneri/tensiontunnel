package com.mauriciotogneri.tensiontunnel.util;

import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;

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

	public static boolean collide(Sprite spriteA, Sprite spriteB)
	{
		float xA = spriteA.getX();
		float yA = spriteA.getY();
		float widthA = spriteA.getWidth();
		float heightA = spriteA.getHeight();

		float xB = spriteB.getX();
		float yB = spriteB.getY();
		float widthB = spriteB.getWidth();
		float heightB = spriteB.getHeight();

		boolean onLeft = ((xA + widthA) < xB);
		boolean onRight = ((xB + widthB) < xA);
		boolean onTop = ((yB + heightB) < yA);
		boolean onBottom = ((yA + heightA) < yB);

		return (!(onLeft || onRight || onTop || onBottom));
	}
}