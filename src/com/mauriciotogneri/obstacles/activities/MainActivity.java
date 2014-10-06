package com.mauriciotogneri.obstacles.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.mauriciotogneri.obstacles.engine.Game;
import com.mauriciotogneri.obstacles.engine.Renderer;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity
{
	private Game game;
	private GLSurfaceView screen;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		this.game = new Game(this);

		this.screen = new GLSurfaceView(this);
		this.screen.setEGLContextClientVersion(2);

		Renderer renderer = new Renderer(this.game, this, this.screen);
		this.screen.setRenderer(renderer);
		
		setContentView(this.screen);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (this.game != null)
		{
			this.game.resume();
		}
		
		if (this.screen != null)
		{
			this.screen.onResume();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		if (this.game != null)
		{
			this.game.pause(isFinishing());
		}

		if (this.screen != null)
		{
			this.screen.onPause();
		}
	}

	@Override
	protected void onDestroy()
	{
		if (this.game != null)
		{
			this.game.stop();
		}
		
		super.onDestroy();
	}
}