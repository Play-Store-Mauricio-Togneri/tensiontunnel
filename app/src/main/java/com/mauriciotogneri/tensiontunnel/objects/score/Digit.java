package com.mauriciotogneri.tensiontunnel.objects.score;

import android.graphics.Color;

import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;
import com.mauriciotogneri.tensiontunnel.shapes.Sprite;

public class Digit
{
    private final Zero zero;
    private final One one;
    private final Two two;
    private final Three three;
    private final Four four;
    private final Five five;
    private final Six six;
    private final Seven seven;
    private final Eight eight;
    private final Nine nine;

    private static final int WIDTH = 4;
    private static final int HEIGHT = 5;

    private static final int COLOR = Color.WHITE;

    public Digit(float x)
    {
        float y = Renderer.RESOLUTION_Y - Digit.HEIGHT - 1;

        this.one = new One(x, y);
        this.zero = new Zero(x, y);
        this.two = new Two(x, y);
        this.three = new Three(x, y);
        this.four = new Four(x, y);
        this.five = new Five(x, y);
        this.six = new Six(x, y);
        this.seven = new Seven(x, y);
        this.eight = new Eight(x, y);
        this.nine = new Nine(x, y);
    }

    public void draw(Renderer renderer, char value)
    {
        if (value == '0')
        {
            this.zero.draw(renderer);
        }
        else if (value == '1')
        {
            this.one.draw(renderer);
        }
        else if (value == '2')
        {
            this.two.draw(renderer);
        }
        else if (value == '3')
        {
            this.three.draw(renderer);
        }
        else if (value == '4')
        {
            this.four.draw(renderer);
        }
        else if (value == '5')
        {
            this.five.draw(renderer);
        }
        else if (value == '6')
        {
            this.six.draw(renderer);
        }
        else if (value == '7')
        {
            this.seven.draw(renderer);
        }
        else if (value == '8')
        {
            this.eight.draw(renderer);
        }
        else if (value == '9')
        {
            this.nine.draw(renderer);
        }
    }

    private static Rectangle getSection(int id)
    {
        Rectangle result = null;

        if (id == 1)
        {
            result = new Rectangle(0, 4, Digit.WIDTH, 1, Digit.COLOR);
        }
        else if (id == 2)
        {
            result = new Rectangle(0, 2, Digit.WIDTH, 1, Digit.COLOR);
        }
        else if (id == 3)
        {
            result = new Rectangle(0, 0, Digit.WIDTH, 1, Digit.COLOR);
        }
        else if (id == 4)
        {
            result = new Rectangle(0, 2, 1, 3, Digit.COLOR);
        }
        else if (id == 5)
        {
            result = new Rectangle(3, 2, 1, 3, Digit.COLOR);
        }
        else if (id == 6)
        {
            result = new Rectangle(0, 0, 1, 3, Digit.COLOR);
        }
        else if (id == 7)
        {
            result = new Rectangle(3, 0, 1, 3, Digit.COLOR);
        }

        return result;
    }

    public static int getHeight()
    {
        return Digit.HEIGHT;
    }

    private class Zero extends Sprite
    {
        public Zero(float x, float y)
        {
            super(x, y, //
                    Digit.getSection(1), //
                    Digit.getSection(3), //
                    Digit.getSection(4), //
                    Digit.getSection(5), //
                    Digit.getSection(6), //
                    Digit.getSection(7));
        }
    }

    private class One extends Sprite
    {
        public One(float x, float y)
        {
            super(x, y, //
                    Digit.getSection(5), //
                    Digit.getSection(7));
        }
    }

    private class Two extends Sprite
    {
        public Two(float x, float y)
        {
            super(x, y, Digit.getSection(1), //
                    Digit.getSection(2),//
                    Digit.getSection(3),//
                    Digit.getSection(5),//
                    Digit.getSection(6));
        }
    }

    private class Three extends Sprite
    {
        public Three(float x, float y)
        {
            super(x, y, Digit.getSection(1), //
                    Digit.getSection(2), //
                    Digit.getSection(3), //
                    Digit.getSection(5), //
                    Digit.getSection(7));
        }
    }

    private class Four extends Sprite
    {
        public Four(float x, float y)
        {
            super(x, y, Digit.getSection(2), //
                    Digit.getSection(4), //
                    Digit.getSection(5), //
                    Digit.getSection(7));
        }
    }

    private class Five extends Sprite
    {
        public Five(float x, float y)
        {
            super(x, y, Digit.getSection(1), //
                    Digit.getSection(2), //
                    Digit.getSection(3), //
                    Digit.getSection(4), //
                    Digit.getSection(7));
        }
    }

    private class Six extends Sprite
    {
        public Six(float x, float y)
        {
            super(x, y, Digit.getSection(1), //
                    Digit.getSection(2), //
                    Digit.getSection(3), //
                    Digit.getSection(4), //
                    Digit.getSection(6), //
                    Digit.getSection(7));
        }
    }

    private class Seven extends Sprite
    {
        public Seven(float x, float y)
        {
            super(x, y, Digit.getSection(1), //
                    Digit.getSection(5), //
                    Digit.getSection(7));
        }
    }

    private class Eight extends Sprite
    {
        public Eight(float x, float y)
        {
            super(x, y, Digit.getSection(1), //
                    Digit.getSection(2), //
                    Digit.getSection(3), //
                    Digit.getSection(4), //
                    Digit.getSection(5), //
                    Digit.getSection(6), //
                    Digit.getSection(7));
        }
    }

    private class Nine extends Sprite
    {
        public Nine(float x, float y)
        {
            super(x, y, Digit.getSection(1), //
                    Digit.getSection(2), //
                    Digit.getSection(3), //
                    Digit.getSection(4), //
                    Digit.getSection(5), //
                    Digit.getSection(7));
        }
    }
}