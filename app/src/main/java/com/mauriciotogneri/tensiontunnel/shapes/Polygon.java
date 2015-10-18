package com.mauriciotogneri.tensiontunnel.shapes;

import android.graphics.PointF;
import android.opengl.GLES20;

public class Polygon extends Shape
{
    public Polygon(float x, float y, int color, PointF... points)
    {
        super(GLES20.GL_TRIANGLE_STRIP, color, x, y, Polygon.getVertices(points));
    }

    private static float[] getVertices(PointF... points)
    {
        float[] result = new float[points.length * 2];

        int index = 0;

        for (PointF point : points)
        {
            result[index++] = point.x;
            result[index++] = point.y;
        }

        return result;
    }
}