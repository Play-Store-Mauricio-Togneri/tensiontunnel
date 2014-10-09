package com.mauriciotogneri.tensiontunnel.activities;

import android.app.Application;
import android.os.StrictMode;

public class TensionTunnel extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		StrictMode.ThreadPolicy.Builder threadBuilder = new StrictMode.ThreadPolicy.Builder();
		threadBuilder.detectAll();
		threadBuilder.penaltyLog();
		StrictMode.setThreadPolicy(threadBuilder.build());
		
		StrictMode.VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder();
		vmBuilder.detectAll();
		vmBuilder.penaltyLog();
		StrictMode.setVmPolicy(vmBuilder.build());
	}
}