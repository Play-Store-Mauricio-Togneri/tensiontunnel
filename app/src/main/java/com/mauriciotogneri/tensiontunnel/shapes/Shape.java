package com.mauriciotogneri.tensiontunnel.shapes;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Shape
{
    private final int mode;
    private final int color;
    private final FloatBuffer vertexData;
    private final int length;

    private float x = 0;
    private float y = 0;
    private float width = 0;
    private float height = 0;

    public Shape(int mode, int color, float x, float y, float[] vertices)
    {
        this.mode = mode;
        this.color = color;

        this.x = x;
        this.y = y;

        this.width = getMaxX(vertices);
        this.height = getMaxY(vertices);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        this.vertexData = byteBuffer.asFloatBuffer();
        this.vertexData.put(vertices);

        this.length = (vertices.length / 2);
    }

    private float getMaxX(float[] vertices)
    {
        float result = Float.MIN_VALUE;

        for (int i = 0; i < vertices.length; i += 2)
        {
            float x = vertices[i];

            if (x > result)
            {
                result = x;
            }
        }

        return result;
    }

    private float getMaxY(float[] vertices)
    {
        float result = Float.MIN_VALUE;

        for (int i = 1; i < vertices.length; i += 2)
        {
            float y = vertices[i];

            if (y > result)
            {
                result = y;
            }
        }

        return result;
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }

    public float getWidth()
    {
        return this.width;
    }

    public float getHeight()
    {
        return this.height;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void draw(Renderer renderer, float parentX, float parentY)
    {
        draw(renderer, parentX, parentY, 1);
    }

    private void draw(Renderer renderer, float parentX, float parentY, float alpha)
    {
        draw(renderer, parentX, parentY, alpha, 1f, 1f);
    }

    public void draw(Renderer renderer, float parentX, float parentY, float alpha, float scaleX, float scaleY)
    {
        renderer.drawShape(this.vertexData, parentX + this.x, parentY + this.y, this.color, alpha, scaleX, scaleY, this.mode, this.length);
    }
}