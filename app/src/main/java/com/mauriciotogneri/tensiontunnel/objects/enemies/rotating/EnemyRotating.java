package com.mauriciotogneri.tensiontunnel.objects.enemies.rotating;

import com.mauriciotogneri.tensiontunnel.engine.Process;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Player;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;
import com.mauriciotogneri.tensiontunnel.util.GeometryUtils;
import com.mauriciotogneri.tensiontunnel.util.Resources;

public class EnemyRotating extends Process
{
    private float x = 0;
    private float y = 0;
    private float angle = 0;
    private float radius = 0;
    private final float rotationSpeed;
    private final Direction direction;

    private final Sprite rectangle1;
    private final Sprite rectangle2;

    public enum Direction
    {
        SAME, OPPOSITE
    }

    public EnemyRotating(float x, float rotationSpeed, Direction direction)
    {
        this.x = x;
        this.y = Renderer.RESOLUTION_Y / 2f;
        this.radius = Renderer.RESOLUTION_Y / 4;
        this.rotationSpeed = rotationSpeed;
        this.direction = direction;

        this.rectangle1 = new Sprite(x + this.radius, this.y, Resources.Sprites.ENEMY_ROTATING);
        this.rectangle2 = new Sprite(x + this.radius, this.y, Resources.Sprites.ENEMY_ROTATING);
    }

    @Override
    public void update(float delta, float distance, float gameSpeed)
    {
        this.x -= distance;

        this.angle += (delta * this.rotationSpeed * gameSpeed);
        this.angle = (this.angle % 360);

        float newX1 = ((float) Math.cos(this.angle) * this.radius) + this.x;
        float newY1 = ((float) Math.sin(this.angle) * this.radius) + this.y;
        this.rectangle1.set(newX1, newY1);

        float newX2 = 0;
        float newY2 = 0;

        if (this.direction == Direction.SAME)
        {
            newX2 = ((float) Math.cos(this.angle - 180) * this.radius) + this.x;
            newY2 = ((float) Math.sin(this.angle - 180) * this.radius) + this.y;
        }
        else if (this.direction == Direction.OPPOSITE)
        {
            newX2 = ((float) Math.cos(360 - this.angle) * this.radius) + this.x;
            newY2 = ((float) Math.sin(360 - this.angle) * this.radius) + this.y;
        }

        this.rectangle2.set(newX2, newY2);

        if (isFinished())
        {
            finish();
        }
    }

    private boolean isFinished()
    {
        boolean out1 = ((this.rectangle1.getRight() + this.radius) < 0);
        boolean out2 = ((this.rectangle2.getRight() + this.radius) < 0);

        return (out1 && out2);
    }

    public boolean collide(Player player)
    {
        return GeometryUtils.collide(this.rectangle1, player.getSprite()) || GeometryUtils.collide(this.rectangle2, player.getSprite());
    }

    @Override
    public boolean isVisible()
    {
        return this.rectangle1.insideScreen(Renderer.RESOLUTION_X) || this.rectangle2.insideScreen(Renderer.RESOLUTION_X);
    }

    @Override
    public void draw(Renderer renderer)
    {
        this.rectangle1.draw(renderer);
        this.rectangle2.draw(renderer);
    }
}