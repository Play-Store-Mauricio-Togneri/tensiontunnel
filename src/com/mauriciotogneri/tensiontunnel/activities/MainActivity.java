package com.mauriciotogneri.tensiontunnel.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.mauriciotogneri.tensiontunnel.R;
import com.mauriciotogneri.tensiontunnel.engine.Game;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.statistics.Statistics;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	private Game game;
	private GLSurfaceView screen;
	private GoogleApiClient apiClient;
	private boolean intentInProgress = false;
	private final boolean openingLeaderboard = false;

	private static final int REQUEST_RESOLVE_ERROR = 1001;

	private static final int ACHIVEMENT_1 = 10;
	private static final int ACHIVEMENT_2 = 20;
	private static final int ACHIVEMENT_3 = 50;
	private static final int ACHIVEMENT_4 = 100;
	private static final int ACHIVEMENT_5 = 200;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
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
		
		Statistics.sendHitAppLaunched();
		
		GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this, this, this);
		builder.addApi(Games.API).addScope(Games.SCOPE_GAMES);
		this.apiClient = builder.build();
	}
	
	@Override
	public void onConnected(Bundle connectionHint)
	{
		if (this.openingLeaderboard)
		{
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
		// TODO: ACTIVATE
		// if (this.apiClient.isConnected())
		// {
		// Games.Leaderboards.submitScore(this.apiClient, getString(R.string.leaderboard_high_score), score);
		//
		// if (score >= MainActivity.ACHIVEMENT_5)
		// {
		// Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_200_points));
		// }
		// else if (score >= MainActivity.ACHIVEMENT_4)
		// {
		// Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_100_points));
		// }
		// else if (score >= MainActivity.ACHIVEMENT_3)
		// {
		// Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_50_points));
		// }
		// else if (score >= MainActivity.ACHIVEMENT_2)
		// {
		// Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_20_points));
		// }
		// else if (score >= MainActivity.ACHIVEMENT_1)
		// {
		// Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_10_points));
		// }
		// }
	}
	
	public void showRanking()
	{
		// TODO: ACTIVATE
		// if (this.apiClient.isConnected())
		// {
		// this.openingLeaderboard = false;
		// startActivityForResult(Games.Leaderboards.getLeaderboardIntent(this.apiClient,
		// getString(R.string.leaderboard_high_score)), 456);
		// }
		// else
		// {
		// this.openingLeaderboard = true;
		// this.apiClient.connect();
		// }
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

		if ((this.apiClient != null) && this.apiClient.isConnected())
		{
			this.apiClient.disconnect();
		}
		
		super.onDestroy();
	}
}