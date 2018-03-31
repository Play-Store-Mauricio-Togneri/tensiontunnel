package com.mauriciotogneri.tensiontunnel.activities;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.mauriciotogneri.tensiontunnel.R;
import com.mauriciotogneri.tensiontunnel.statistics.Statistics;

import io.fabric.sdk.android.Fabric;

public class TensionTunnel extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        Statistics.initialize(analytics.newTracker(R.xml.app_tracker));
    }
}