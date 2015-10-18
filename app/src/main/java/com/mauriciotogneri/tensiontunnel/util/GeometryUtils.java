package com.mauriciotogneri.tensiontunnel.util;

import com.mauriciotogneri.tensiontunnel.shapes.Sprite;

public class GeometryUtils
{
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