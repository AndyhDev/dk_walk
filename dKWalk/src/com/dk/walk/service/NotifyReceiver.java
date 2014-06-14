package com.dk.walk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NotifyReceiver extends BroadcastReceiver{
	private static final String TAG = "RemoteControlRecevier";

	public static final String NOTIFY_RECECIVER = "NOTIFYL_RECEIVER";
	public static final String NOTIFY_ACTION = "NOTIFY_ACTION";
	
	public static final String ACTION_PAUSE = "ACTION_PAUSE";
	public static final String ACTION_RESUME = "ACTION_RESUME";
	
	@Override
	public void onReceive(Context context, Intent i) {
		Log.d(TAG, "onReceive");

		Intent service = new Intent(context, GPSservice.class);
		context.startService(service);

		Intent intent = new Intent(NOTIFY_RECECIVER);
		intent.putExtra(NOTIFY_ACTION, i.getAction());
		context.sendBroadcast(intent);
	}

}
