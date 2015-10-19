package com.mauriciotogneri.tensiontunnel.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.mauriciotogneri.tensiontunnel.R;
import com.mauriciotogneri.tensiontunnel.engine.Game;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.statistics.Statistics;
import com.mauriciotogneri.tensiontunnel.util.Preferences;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private Game game;
    private GLSurfaceView screen;
    private GoogleApiClient apiClient;
    private LinearLayout helpScreen;
    private boolean intentInProgress = false;
    private boolean openingLeaderboard = false;

    private static final int REQUEST_RESOLVE_ERROR = 1001;

    private static final int ACHIEVEMENT_1 = 10;
    private static final int ACHIEVEMENT_2 = 20;
    private static final int ACHIEVEMENT_3 = 50;
    private static final int ACHIEVEMENT_4 = 100;
    private static final int ACHIEVEMENT_5 = 200;

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
        Statistics.sendHitAppLaunched();

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this, this, this);
        builder.addApi(Games.API).addScope(Games.SCOPE_GAMES);
        this.apiClient = builder.build();

        if (Preferences.isConnectedPlayGameServices())
        {
            this.apiClient.connect();
        }

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

    @Override
    public void onConnected(Bundle connectionHint)
    {
        Preferences.setConnectedPlayGameServices();

        if (this.openingLeaderboard)
        {
            submitScore(Preferences.getBestScore());

            showRanking();
        }
    }

    @Override
    public void onConnectionSuspended(int cause)
    {
        this.apiClient.reconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        if ((!this.intentInProgress) && result.hasResolution())
        {
            try
            {
                this.intentInProgress = true;
                result.startResolutionForResult(this, MainActivity.REQUEST_RESOLVE_ERROR);
            }
            catch (SendIntentException e)
            {
                this.intentInProgress = false;
                this.apiClient.connect();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MainActivity.REQUEST_RESOLVE_ERROR)
        {
            this.intentInProgress = false;

            if (resultCode == Activity.RESULT_OK)
            {
                if ((!this.apiClient.isConnecting()) && (!this.apiClient.isConnected()))
                {
                    this.apiClient.connect();
                }
            }
        }
    }

    public void submitScore(int score)
    {
        if (this.apiClient.isConnected())
        {
            Games.Leaderboards.submitScore(this.apiClient, getString(R.string.leaderboard_high_score), score);

            if (score >= MainActivity.ACHIEVEMENT_5)
            {
                Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_200_points));
            }
            else if (score >= MainActivity.ACHIEVEMENT_4)
            {
                Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_100_points));
            }
            else if (score >= MainActivity.ACHIEVEMENT_3)
            {
                Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_50_points));
            }
            else if (score >= MainActivity.ACHIEVEMENT_2)
            {
                Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_20_points));
            }
            else if (score >= MainActivity.ACHIEVEMENT_1)
            {
                Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_10_points));
            }
        }
    }

    public void showRanking()
    {
        if (this.apiClient.isConnected())
        {
            this.openingLeaderboard = false;
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(this.apiClient, getString(R.string.leaderboard_high_score)), 456);
        }
        else
        {
            this.openingLeaderboard = true;
            this.apiClient.connect();
        }
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

        if ((this.apiClient != null) && this.apiClient.isConnected())
        {
            this.apiClient.disconnect();
        }

        super.onDestroy();
    }
}