package com.mauriciotogneri.tensiontunnel.objects;

import android.graphics.Color;
import android.graphics.PointF;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Polygon;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public class Background
{
    private final Sprite base;

    private Sprite triangle1;
    private Sprite triangle2;

    private final Sprite groundTop1;
    private final Sprite groundTop2;

    private final Sprite groundBottom1;
    private final Sprite groundBottom2;

    private static final int SHAPE_WIDTH = 100;
    private static final int GROUND_HEIGHT = 7;
    private static final int NUMBER_OF_SHAPES = 2;

    private static final int COLOR_SHAPE = Color.argb(255, 165, 155, 205);

    private static final float RELATIVE_SPEED = 0.75f;

    public Background()
    {
        float width = Renderer.RESOLUTION_X;
        float height = Renderer.RESOLUTION_Y;

        this.base = new Sprite(0, 0, Resources.Sprites.BACKGROUND);

        this.groundTop1 = new Sprite(0, height - Background.GROUND_HEIGHT, Resources.Sprites.BACKGROUND_GROUND_TOP);
        this.groundTop2 = new Sprite(width, height - Background.GROUND_HEIGHT, Resources.Sprites.BACKGROUND_GROUND_TOP);

        this.groundBottom1 = new Sprite(0, 0, Resources.Sprites.BACKGROUND_GROUND_BOTTOM);
        this.groundBottom2 = new Sprite(width, 0, Resources.Sprites.BACKGROUND_GROUND_BOTTOM);

        createTriangles(height);
    }

    public void update(float distance)
    {
        this.groundTop1.moveX(-distance);

        if ((this.groundTop1.getRight()) < 0)
        {
            this.groundTop1.moveX(Renderer.RESOLUTION_X * 2);
        }

        this.groundTop2.moveX(-distance);

        if ((this.groundTop2.getRight()) < 0)
        {
            this.groundTop2.moveX(Renderer.RESOLUTION_X * 2);
        }

        // =========================================================

        this.groundBottom1.moveX(-distance);

        if ((this.groundBottom1.getRight()) < 0)
        {
            this.groundBottom1.moveX(Renderer.RESOLUTION_X * 2);
        }

        this.groundBottom2.moveX(-distance);

        if ((this.groundBottom2.getRight()) < 0)
        {
            this.groundBottom2.moveX(Renderer.RESOLUTION_X * 2);
        }

        // =========================================================

        float farBackgroundSpeed = distance * Background.RELATIVE_SPEED;

        this.triangle1.moveX(-farBackgroundSpeed);

        if ((this.triangle1.getRight()) < 0)
        {
            this.triangle1.moveX(Background.NUMBER_OF_SHAPES * Background.SHAPE_WIDTH);
        }

        this.triangle2.moveX(-farBackgroundSpeed);

        if ((this.triangle2.getRight()) < 0)
        {
            this.triangle2.moveX(Background.NUMBER_OF_SHAPES * Background.SHAPE_WIDTH);
        }
    }

    public boolean collide(Player player)
    {
        boolean result = false;

        if (GeometryUtils.collide(player.getSprite(), this.groundTop1) || GeometryUtils.collide(player.getSprite(), this.groundTop2))
        {
            result = true;
        }
        else if (GeometryUtils.collide(player.getSprite(), this.groundBottom1) || GeometryUtils.collide(player.getSprite(), this.groundBottom2))
        {
            result = true;
        }

        return result;
    }

    public void draw(Renderer renderer)
    {
        this.base.draw(renderer);

        this.triangle1.draw(renderer);
        this.triangle2.draw(renderer);

        this.groundTop1.draw(renderer);
        this.groundTop2.draw(renderer);

        this.groundBottom1.draw(renderer);
        this.groundBottom2.draw(renderer);
    }

    private void createTriangles(float height)
    {
        PointF[] points1 = new PointF[3];
        points1[0] = new PointF(0, 0);
        points1[1] = new PointF(Background.SHAPE_WIDTH / 2f, height);
        points1[2] = new PointF(Background.SHAPE_WIDTH, 0);
        this.triangle1 = new Sprite(0, 0, new Polygon(0, 0, Background.COLOR_SHAPE, points1));

        PointF[] points2 = new PointF[3];
        points2[0] = new PointF(0, 0);
        points2[1] = new PointF(Background.SHAPE_WIDTH / 2f, height);
        points2[2] = new PointF(Background.SHAPE_WIDTH, 0);
        this.triangle2 = new Sprite(Background.SHAPE_WIDTH, 0, new Polygon(0, 0, Background.COLOR_SHAPE, points2));
    }

    public static int getHeight()
    {
        return Background.GROUND_HEIGHT;
    }
}