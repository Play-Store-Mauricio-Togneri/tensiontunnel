package com.mauriciotogneri.tensiontunnel.audio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class AudioManager
{
	private final Context context;
	private final SoundPool soundPool;
	private final Map<String, Integer> soundsMap;
	private MediaPlayer player;
	private int audioPosition = 0;
	
	private static AudioManager instance;
	
	public static void initialize(Context context)
	{
		AudioManager.instance = new AudioManager(context);
	}
	
	public static AudioManager getInstance()
	{
		return AudioManager.instance;
	}
	
	private AudioManager(Context context)
	{
		this.context = context;
		
		this.soundsMap = new HashMap<String, Integer>();
		
		this.soundPool = new SoundPool(20, android.media.AudioManager.STREAM_MUSIC, 100);
		this.soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener()
		{
			@Override
			public void onLoadComplete(SoundPool soundPool, int resourceId, int status)
			{
				if (status == 0)
				{
					playbackSound(resourceId);
				}
			}
		});
	}
	
	private void loadSound(String soundPath)
	{
		AssetFileDescriptor assetDescriptor = null;
		
		try
		{
			assetDescriptor = this.context.getAssets().openFd(soundPath);
			int resourceId = this.soundPool.load(assetDescriptor, 1);
			this.soundsMap.put(soundPath, resourceId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDescriptor(assetDescriptor);
		}
	}
	
	public void playSound(String soundPath)
	{
		if (this.soundsMap.containsKey(soundPath))
		{
			playbackSound(this.soundsMap.get(soundPath));
		}
		else
		{
			loadSound(soundPath);
		}
	}
	
	private void playbackSound(int resourceId)
	{
		this.soundPool.play(resourceId, 1f, 1f, 1, 0, 1f);
	}
	
	public void playAudio(String audioPath)
	{
		stopMusic();
		
		AssetFileDescriptor assetDescriptor = null;
		
		try
		{
			assetDescriptor = this.context.getAssets().openFd(audioPath);
			
			this.player = new MediaPlayer();
			this.player.setDataSource(assetDescriptor.getFileDescriptor(), assetDescriptor.getStartOffset(), assetDescriptor.getLength());
			this.player.setLooping(true);
			this.player.setVolume(0.5f, 0.5f);
			
			this.player.setOnPreparedListener(new OnPreparedListener()
			{
				@Override
				public void onPrepared(MediaPlayer player)
				{
					player.start();
				}
			});
			
			this.player.prepare();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDescriptor(assetDescriptor);
		}
	}
	
	private void stopMusic()
	{
		if (this.player != null)
		{
			try
			{
				this.player.stop();
				this.player.release();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void resumeAudio()
	{
		try
		{
			if ((this.player != null) && (!this.player.isPlaying()))
			{
				this.player.seekTo(this.audioPosition);
				this.player.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void pauseAudio()
	{
		if (this.player != null)
		{
			try
			{
				this.player.pause();
				this.audioPosition = this.player.getCurrentPosition();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void stopAudio()
	{
		stopMusic();
		
		if (this.soundPool != null)
		{
			Collection<Integer> soundsIds = this.soundsMap.values();
			
			for (Integer soundId : soundsIds)
			{
				this.soundPool.unload(soundId);
			}
			
			this.soundPool.release();
		}
	}
	
	public boolean isAudioPlaying()
	{
		return ((this.player != null) && this.player.isPlaying());
	}
	
	private void closeDescriptor(AssetFileDescriptor assetDescriptor)
	{
		if (assetDescriptor != null)
		{
			try
			{
				assetDescriptor.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}