package com.mauriciotogneri.obstacles.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mauriciotogneri.obstacles.R;
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

		FrameLayout layout = new FrameLayout(this);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(layoutParams);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/visitor.ttf");

		LinearLayout blockScreen = (LinearLayout)View.inflate(this, R.layout.block_screen, null);
		blockScreen.setVisibility(View.GONE);
		
		setFontTextView((TextView)blockScreen.findViewById(R.id.label_score), font);
		setFontTextView((TextView)blockScreen.findViewById(R.id.score), font);
		setFontTextView((TextView)blockScreen.findViewById(R.id.label_best), font);
		setFontTextView((TextView)blockScreen.findViewById(R.id.best), font);
		
		this.game = new Game(this, blockScreen);

		this.screen = new GLSurfaceView(this);
		this.screen.setEGLContextClientVersion(2);
		
		Renderer renderer = new Renderer(this.game, this, this.screen);
		this.screen.setRenderer(renderer);

		layout.addView(this.screen);
		layout.addView(blockScreen);

		setContentView(layout);
	}

	private void setFontTextView(TextView textView, Typeface font)
	{
		try
		{
			textView.setTypeface(font);
		}
		catch (Exception e)
		{
		}
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