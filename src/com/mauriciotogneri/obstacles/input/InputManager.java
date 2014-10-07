package com.mauriciotogneri.obstacles.input;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class InputManager implements OnTouchListener
{
	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		int action = event.getAction();
		int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

		float x = event.getX(pointerIndex);
		float y = event.getY(pointerIndex);

		switch (action & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN: // 0
			case MotionEvent.ACTION_POINTER_DOWN: // 5
				Log.e("", pointerIndex + ": ooooooooooooooo");
				onPress(x, y);
				break;

			case MotionEvent.ACTION_UP: // 1
			case MotionEvent.ACTION_POINTER_UP: // 6
				Log.e("", pointerIndex + ": ###############");
				onRelease(x, y);
				break;
		}

		return true;
	}

	public abstract void onPress(float x, float y);

	public abstract void onRelease(float x, float y);
}