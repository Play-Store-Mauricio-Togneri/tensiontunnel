package com.mauriciotogneri.tensiontunnel.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mauriciotogneri.tensiontunnel.R;
import com.mauriciotogneri.tensiontunnel.engine.Game;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.util.Preferences;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity
{
    private Game game;
    private GLSurfaceView screen;
    private LinearLayout helpScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout layout = new FrameLayout(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/visitor.ttf");

        this.helpScreen = (LinearLayout) View.inflate(this, R.layout.how_to_play, null);
        this.helpScreen.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                MainActivity.this.helpScreen.setVisibility(View.GONE);

                return true;
            }
        });

        setFontTextView((TextView) this.helpScreen.findViewById(R.id.label_how_to_play_1), font);
        setFontTextView((TextView) this.helpScreen.findViewById(R.id.label_how_to_play_2), font);
        setFontTextView((TextView) this.helpScreen.findViewById(R.id.label_left_input), font);
        setFontTextView((TextView) this.helpScreen.findViewById(R.id.label_right_input), font);

        this.helpScreen.setVisibility(View.GONE);

        LinearLayout blockScreen = (LinearLayout) View.inflate(this, R.layout.block_screen, null);
        blockScreen.setVisibility(View.GONE);

        setFontTextView((TextView) blockScreen.findViewById(R.id.label_score), font);
        setFontTextView((TextView) blockScreen.findViewById(R.id.score), font);
        setFontTextView((TextView) blockScreen.findViewById(R.id.label_best), font);
        setFontTextView((TextView) blockScreen.findViewById(R.id.best), font);

        this.game = new Game(this, blockScreen);

        this.screen = new GLSurfaceView(this);
        this.screen.setEGLContextClientVersion(2);

        Renderer renderer = new Renderer(this.game, this, this.screen);
        this.screen.setRenderer(renderer);

        layout.addView(this.screen);
        layout.addView(blockScreen);
        layout.addView(this.helpScreen);

        setContentView(layout);

        Preferences.initialize(this);

        if (Preferences.isFirstLaunch())
        {
            Preferences.setFirstLaunch();
            displayHelp();
        }
    }

    public void displayHelp()
    {
        this.helpScreen.setVisibility(View.VISIBLE);
    }

    private void setFontTextView(TextView textView, Typeface font)
    {
        try
        {
            textView.setTypeface(font);
        }
        catch (Exception e)
        {
            // ignore
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