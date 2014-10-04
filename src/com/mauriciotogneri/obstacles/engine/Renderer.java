package com.mauriciotogneri.obstacles.engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.mauriciotogneri.obstacles.R;
import com.mauriciotogneri.obstacles.R.raw;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.input.InputManager;
import com.mauriciotogneri.obstacles.util.ShaderHelper;
import com.mauriciotogneri.obstacles.util.TextResourceReader;

public class Renderer implements android.opengl.GLSurfaceView.Renderer
{
	public int width = 0;
	public int height = 0;

	private long startTime;
	
	private final Game game;
	private final Context context;
	private final GLSurfaceView screen;
	
	private final float[] projectionMatrix = new float[16];

	private int matrixLocation;
	private int positionLocation;
	private int colorLocation;

	private int resolutionX = 0;
	private int resolutionY = 0;
	
	// input
	private final Input input = new Input();
	private final Object inputLock = new Object();
	
	// state
	private RendererStatus state = null;
	private final Object stateChangedLock = new Object();

	// renderer status
	private enum RendererStatus
	{
		RUNNING, IDLE, PAUSED, FINISHED
	}

	public Renderer(Game game, Context context, GLSurfaceView screen)
	{
		this.game = game;
		this.context = context;
		this.startTime = System.nanoTime();

		this.screen = screen;
		this.screen.setOnTouchListener(new InputManager()
		{
			@Override
			public void onPress(float x, float y)
			{
				processInput(getScreenX(x), true);
			}

			@Override
			public void onRelease(float x, float y)
			{
				processInput(getScreenX(x), false);
			}
		});
	}
	
	private float getScreenX(float x)
	{
		return ((x / this.width) * this.resolutionX);
	}
	
	// private float getScreenY(float y)
	// {
	// return (this.resolutionY - ((y / this.height) * this.resolutionY));
	// }
	
	public int getResolutionX()
	{
		return this.resolutionX;
	}
	
	public int getResolutionY()
	{
		return this.resolutionY;
	}
	
	private void processInput(float x, boolean pressed)
	{
		synchronized (this.inputLock)
		{
			if (pressed)
			{
				this.input.press(x, this.resolutionX);
			}
			else
			{
				this.input.release(x, this.resolutionX);
			}
		}
	}

	private void update(float delta)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUniformMatrix4fv(this.matrixLocation, 1, false, this.projectionMatrix, 0);
		
		synchronized (this.inputLock)
		{
			this.game.update(delta, this.input, this.positionLocation, this.colorLocation);
		}
	}

	@Override
	public void onDrawFrame(GL10 unused)
	{
		RendererStatus status = null;

		synchronized (this.stateChangedLock)
		{
			status = this.state;
		}

		if (status == RendererStatus.RUNNING)
		{
			long currentTime = System.nanoTime();
			float delta = (currentTime - this.startTime) / 1000000000f;
			this.startTime = currentTime;

			// FPS.log(currentTime);

			update(delta);
		}
		else if ((status == RendererStatus.PAUSED) || (status == RendererStatus.FINISHED))
		{
			synchronized (this.stateChangedLock)
			{
				this.state = RendererStatus.IDLE;
				this.stateChangedLock.notifyAll();
			}
		}
	}
	
	@Override
	public void onSurfaceCreated(GL10 screen, EGLConfig config)
	{
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		GLES20.glClearColor(1f, 1f, 1f, 1f);
		
		String vertexShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.fragment_shader);
		
		int program = ShaderHelper.linkProgram(vertexShaderSource, fragmentShaderSource);
		GLES20.glUseProgram(program);
		
		this.matrixLocation = GLES20.glGetUniformLocation(program, "u_Matrix");
		this.colorLocation = GLES20.glGetUniformLocation(program, "u_Color");
		this.positionLocation = GLES20.glGetAttribLocation(program, "a_Position");
	}
	
	@Override
	public void onSurfaceChanged(GL10 screen, int width, int height)
	{
		this.width = width;
		this.height = height;

		this.resolutionX = 100;
		this.resolutionY = (int)(this.resolutionX / ((float)width / (float)height));
		
		GLES20.glViewport(0, 0, width, height);

		Matrix.orthoM(this.projectionMatrix, 0, 0, this.resolutionX, 0, this.resolutionY, -1f, 1f);
		
		synchronized (this.stateChangedLock)
		{
			this.state = RendererStatus.RUNNING;
		}

		this.game.start(this);
	}
	
	public void pause(boolean finishing)
	{
		synchronized (this.stateChangedLock)
		{
			if (this.state == RendererStatus.RUNNING)
			{
				if (finishing)
				{
					this.state = RendererStatus.FINISHED;
				}
				else
				{
					this.state = RendererStatus.PAUSED;
				}
				
				while (true)
				{
					try
					{
						this.stateChangedLock.wait();
						break;
					}
					catch (Exception e)
					{
					}
				}
			}

			if (this.screen != null)
			{
				this.screen.onPause();
			}
		}
	}

	public void resume()
	{
		if (this.screen != null)
		{
			this.screen.onResume();
		}
	}
}