package com.mauriciotogneri.tensiontunnel.activities;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender.Type;
import android.app.Application;
import android.os.StrictMode;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.mauriciotogneri.tensiontunnel.R;
import com.mauriciotogneri.tensiontunnel.statistics.Statistics;

@ReportsCrashes(formUri = "http://zeronest.com/acra/report.php", reportType = Type.FORM, formKey = "")
public class TensionTunnel extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		ACRA.init(this);
		ACRA.getErrorReporter().putCustomData("PACKAGE_NAME", getPackageName());

		StrictMode.ThreadPolicy.Builder threadBuilder = new StrictMode.ThreadPolicy.Builder();
		threadBuilder.detectAll();
		threadBuilder.penaltyLog();
		StrictMode.setThreadPolicy(threadBuilder.build());

		StrictMode.VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder();
		vmBuilder.detectAll();
		vmBuilder.penaltyLog();
		StrictMode.setVmPolicy(vmBuilder.build());
		
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		Statistics.initialize(analytics.newTracker(R.xml.app_tracker));
	}
}