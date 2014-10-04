package com.mauriciotogneri.obstacles.util;

import android.opengl.GLES20;

public class ShaderHelper
{
	private static int compileShader(int type, String shaderCode)
	{
		// Create a new shader object.
		final int shaderObjectId = GLES20.glCreateShader(type);

		// Pass in the shader source.
		GLES20.glShaderSource(shaderObjectId, shaderCode);

		// Compile the shader.
		GLES20.glCompileShader(shaderObjectId);

		// Get the compilation status.
		final int[] compileStatus = new int[1];
		GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

		// Verify the compile status.
		if (compileStatus[0] == 0)
		{
			// If it failed, delete the shader object.
			GLES20.glDeleteShader(shaderObjectId);

			return 0;
		}

		// Return the shader object ID.
		return shaderObjectId;
	}

	public static int linkProgram(String vertexShader, String fragmentShader)
	{
		int vertexShaderId = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
		int fragmentShaderId = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

		// Create a new program object.
		final int programObjectId = GLES20.glCreateProgram();
		
		// Attach the vertex shader to the program.
		GLES20.glAttachShader(programObjectId, vertexShaderId);
		// Attach the fragment shader to the program.
		GLES20.glAttachShader(programObjectId, fragmentShaderId);

		// Link the two shaders together into a program.
		GLES20.glLinkProgram(programObjectId);

		// Get the link status.
		final int[] linkStatus = new int[1];
		GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);
		
		// Verify the link status.
		if (linkStatus[0] == 0)
		{
			// If it failed, delete the program object.
			GLES20.glDeleteProgram(programObjectId);
			return 0;
		}

		// Return the program object ID.
		return programObjectId;
	}
}