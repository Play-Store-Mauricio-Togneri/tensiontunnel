package com.mauriciotogneri.tensiontunnel.objects.score;

import android.graphics.Color;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.shapes.Rectangle;

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
	
	public static final int DIGIT_WIDTH = 4;
	private static final int DIGIT_HEIGHT = 5;
	
	private static final int COLOR = Color.WHITE;

	public Digit(float x)
	{
		float y = Renderer.RESOLUTION_Y - Digit.DIGIT_HEIGHT - 1;
		
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

	private static Rectangle getSection(int id, float x, float y)
	{
		Rectangle result = null;
		
		if (id == 1)
		{
			result = new Rectangle(x, y + 4, Digit.DIGIT_WIDTH, 1, Digit.COLOR);
		}
		else if (id == 2)
		{
			result = new Rectangle(x, y + 2, Digit.DIGIT_WIDTH, 1, Digit.COLOR);
		}
		else if (id == 3)
		{
			result = new Rectangle(x, y, Digit.DIGIT_WIDTH, 1, Digit.COLOR);
		}
		else if (id == 4)
		{
			result = new Rectangle(x, y + 2, 1, 3, Digit.COLOR);
		}
		else if (id == 5)
		{
			result = new Rectangle(x + 3, y + 2, 1, 3, Digit.COLOR);
		}
		else if (id == 6)
		{
			result = new Rectangle(x, y, 1, 3, Digit.COLOR);
		}
		else if (id == 7)
		{
			result = new Rectangle(x + 3, y, 1, 3, Digit.COLOR);
		}

		return result;
	}
	
	private class Zero
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		private final Rectangle rectangle4;
		private final Rectangle rectangle5;
		private final Rectangle rectangle6;
		
		public Zero(float x, float y)
		{
			this.rectangle1 = Digit.getSection(1, x, y);
			this.rectangle2 = Digit.getSection(3, x, y);
			this.rectangle3 = Digit.getSection(4, x, y);
			this.rectangle4 = Digit.getSection(5, x, y);
			this.rectangle5 = Digit.getSection(6, x, y);
			this.rectangle6 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
			this.rectangle4.draw(renderer);
			this.rectangle5.draw(renderer);
			this.rectangle6.draw(renderer);
		}
	}

	private class One
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		
		public One(float x, float y)
		{
			this.rectangle1 = Digit.getSection(5, x, y);
			this.rectangle2 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
		}
	}

	private class Two
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		private final Rectangle rectangle4;
		private final Rectangle rectangle5;
		
		public Two(float x, float y)
		{
			this.rectangle1 = Digit.getSection(1, x, y);
			this.rectangle2 = Digit.getSection(2, x, y);
			this.rectangle3 = Digit.getSection(3, x, y);
			this.rectangle4 = Digit.getSection(5, x, y);
			this.rectangle5 = Digit.getSection(6, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
			this.rectangle4.draw(renderer);
			this.rectangle5.draw(renderer);
		}
	}
	
	private class Three
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		private final Rectangle rectangle4;
		private final Rectangle rectangle5;
		
		public Three(float x, float y)
		{
			this.rectangle1 = Digit.getSection(1, x, y);
			this.rectangle2 = Digit.getSection(2, x, y);
			this.rectangle3 = Digit.getSection(3, x, y);
			this.rectangle4 = Digit.getSection(5, x, y);
			this.rectangle5 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
			this.rectangle4.draw(renderer);
			this.rectangle5.draw(renderer);
		}
	}
	
	private class Four
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		private final Rectangle rectangle4;
		
		public Four(float x, float y)
		{
			this.rectangle1 = Digit.getSection(2, x, y);
			this.rectangle2 = Digit.getSection(4, x, y);
			this.rectangle3 = Digit.getSection(5, x, y);
			this.rectangle4 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
			this.rectangle4.draw(renderer);
		}
	}
	
	private class Five
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		private final Rectangle rectangle4;
		private final Rectangle rectangle5;
		
		public Five(float x, float y)
		{
			this.rectangle1 = Digit.getSection(1, x, y);
			this.rectangle2 = Digit.getSection(2, x, y);
			this.rectangle3 = Digit.getSection(3, x, y);
			this.rectangle4 = Digit.getSection(4, x, y);
			this.rectangle5 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
			this.rectangle4.draw(renderer);
			this.rectangle5.draw(renderer);
		}
	}
	
	private class Six
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		private final Rectangle rectangle4;
		private final Rectangle rectangle5;
		private final Rectangle rectangle6;
		
		public Six(float x, float y)
		{
			this.rectangle1 = Digit.getSection(1, x, y);
			this.rectangle2 = Digit.getSection(2, x, y);
			this.rectangle3 = Digit.getSection(3, x, y);
			this.rectangle4 = Digit.getSection(4, x, y);
			this.rectangle5 = Digit.getSection(6, x, y);
			this.rectangle6 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
			this.rectangle4.draw(renderer);
			this.rectangle5.draw(renderer);
			this.rectangle6.draw(renderer);
		}
	}
	
	private class Seven
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		
		public Seven(float x, float y)
		{
			this.rectangle1 = Digit.getSection(1, x, y);
			this.rectangle2 = Digit.getSection(5, x, y);
			this.rectangle3 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
		}
	}
	
	private class Eight
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		private final Rectangle rectangle4;
		private final Rectangle rectangle5;
		private final Rectangle rectangle6;
		private final Rectangle rectangle7;
		
		public Eight(float x, float y)
		{
			this.rectangle1 = Digit.getSection(1, x, y);
			this.rectangle2 = Digit.getSection(2, x, y);
			this.rectangle3 = Digit.getSection(3, x, y);
			this.rectangle4 = Digit.getSection(4, x, y);
			this.rectangle5 = Digit.getSection(5, x, y);
			this.rectangle6 = Digit.getSection(6, x, y);
			this.rectangle7 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
			this.rectangle4.draw(renderer);
			this.rectangle5.draw(renderer);
			this.rectangle6.draw(renderer);
			this.rectangle7.draw(renderer);
		}
	}

	private class Nine
	{
		private final Rectangle rectangle1;
		private final Rectangle rectangle2;
		private final Rectangle rectangle3;
		private final Rectangle rectangle4;
		private final Rectangle rectangle5;
		private final Rectangle rectangle6;
		
		public Nine(float x, float y)
		{
			this.rectangle1 = Digit.getSection(1, x, y);
			this.rectangle2 = Digit.getSection(2, x, y);
			this.rectangle3 = Digit.getSection(3, x, y);
			this.rectangle4 = Digit.getSection(4, x, y);
			this.rectangle5 = Digit.getSection(5, x, y);
			this.rectangle6 = Digit.getSection(7, x, y);
		}
		
		public void draw(Renderer renderer)
		{
			this.rectangle1.draw(renderer);
			this.rectangle2.draw(renderer);
			this.rectangle3.draw(renderer);
			this.rectangle4.draw(renderer);
			this.rectangle5.draw(renderer);
			this.rectangle6.draw(renderer);
		}
	}
}