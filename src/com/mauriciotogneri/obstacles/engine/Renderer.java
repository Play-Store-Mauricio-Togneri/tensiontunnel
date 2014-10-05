package com.mauriciotogneri.obstacles.engine;

import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.mauriciotogneri.obstacles.R;
import com.mauriciotogneri.obstacles.input.Input;
import com.mauriciotogneri.obstacles.input.InputManager;
import com.mauriciotogneri.obstacles.util.Resources;
import com.mauriciotogneri.obstacles.util.ShaderUtils;

public class Renderer implements android.opengl.GLSurfaceView.Renderer
{
	public int width = 0;
	public int height = 0;
	
	private long startTime;

	private final Game game;
	private final Context context;
	private final GLSurfaceView screen;

	private final float[] modelMatrix = new float[16];
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
	
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int BYTES_PER_FLOAT = 4;
	private static final int STRIDE = Renderer.POSITION_COMPONENT_COUNT * Renderer.BYTES_PER_FLOAT;
	
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
		
		synchronized (this.inputLock)
		{
			this.game.update(delta, this.input, this);
		}
	}
	
	public void drawShape(FloatBuffer vertexData, float x, float y, int color, float alpha, int mode, int length)
	{
		Matrix.setIdentityM(this.modelMatrix, 0);
		Matrix.translateM(this.modelMatrix, 0, x, y, 0);

		float[] finalMatrix = new float[16];
		Matrix.multiplyMM(finalMatrix, 0, this.projectionMatrix, 0, this.modelMatrix, 0);
		GLES20.glUniformMatrix4fv(this.matrixLocation, 1, false, finalMatrix, 0);

		// -----------------------------------

		vertexData.position(0);
		GLES20.glVertexAttribPointer(this.positionLocation, Renderer.POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, Renderer.STRIDE, vertexData);
		GLES20.glEnableVertexAttribArray(this.positionLocation);

		float red = Color.red(color) / 255f;
		float green = Color.green(color) / 255f;
		float blue = Color.blue(color) / 255f;

		GLES20.glUniform4f(this.colorLocation, red, green, blue, alpha);
		GLES20.glDrawArrays(mode, 0, length);
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

		String vertexShaderSource = Resources.readTextFile(this.context, R.raw.vertex_shader);
		String fragmentShaderSource = Resources.readTextFile(this.context, R.raw.fragment_shader);

		int program = ShaderUtils.linkProgram(vertexShaderSource, fragmentShaderSource);
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
		
		GLES20.glViewport(0, 0, width, height);
		
		this.resolutionX = 100;
		this.resolutionY = (int)(this.resolutionX / ((float)width / (float)height));

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