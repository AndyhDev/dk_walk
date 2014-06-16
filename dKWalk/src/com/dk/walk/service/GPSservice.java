package com.dk.walk.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dk.walk.MainActivity;
import com.dk.walk.R;
import com.dk.walk.database.SQLWay;
import com.dk.walk.database.SQLiteDataSource;
import com.dk.walk.util.StepDetector;
import com.dk.walk.util.StepListener;

public class GPSservice extends Service implements StepListener {
	private final IBinder binder = new MyBinder();
	public static final String TAG = "GPSservice";
	public static final int GPS_STATE_START = 1;
	public static final int GPS_STATE_PAUSE = 2;
	public static final int GPS_STATE_STOP = 3;

	public final int NOTIFY_ID = 1;

	public final static String NEW_LOCATION = "new_locataion";
	public final static String NEW_STEPS = "new_steps";
	public final static String NEW_WAY = "new_way";
	public final static String UPDATE_TIME = "update_time";
	
	private SQLWay currentWay;
	private Boolean active = false;
	private int GPSState = GPS_STATE_STOP;
	private LocationManager lm;
	private Double currentLon;
	private Double currentLat;
	private Double lastLon = null;
	private Double lastLat = null;
	private Long time = (long) 0;
	private StepDetector stepDetector;
	private Handler handler = new Handler();
	private String tempTitle;
	
	public void onCreate(){
		lm =(LocationManager) getSystemService(LOCATION_SERVICE);
		stepDetector = new StepDetector(this);
		stepDetector.addStepListener(this);
	}

	LocationListener Loclist = new LocationListener(){

		@Override
		public void onLocationChanged(Location loc) {
			if(active){
				if(lastLon == null){
					lastLat = loc.getLatitude();
					lastLon = loc.getLongitude();
				}else{
					currentLat = loc.getLatitude();
					currentLon = loc.getLongitude();
					Log.d("POS", "lon:" + currentLon);
					Log.d("POS", "lat:" + currentLat);

					Location locationA = new Location("a");
					locationA.setLatitude(lastLat);
					locationA.setLongitude(lastLon);

					float distanceMeters = locationA.distanceTo(loc);
					if(distanceMeters > 2){
						currentWay.addLocation(loc);
						currentWay.addWay(distanceMeters);
						lastLat = currentLat;
						lastLon = currentLon;

						Intent action = new Intent(NEW_LOCATION);
						sendBroadcast(action);
					}else{
						Log.d(TAG, "to small:" + distanceMeters);
					}
				}
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}

	public void startGPS(){
		SQLiteDataSource dataSource = new SQLiteDataSource(this);
		dataSource.open();
		currentWay = dataSource.getNewSQLWay();
		dataSource.close();
		
		if(tempTitle != null){
			currentWay.setTitle(tempTitle);
			tempTitle = null;
		}
		active = true;
		GPSState = GPS_STATE_START;
		stepDetector.start();

		handler.postDelayed(timer, 1000);

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, Loclist);
		showNotify();
		
		Intent action = new Intent(NEW_WAY);
		sendBroadcast(action);
	}
	public void togglePauseState(){
		if(GPSState == GPS_STATE_PAUSE){
			resumeGPS();
		}else if(GPSState == GPS_STATE_START){
			pauseGPS();
		}
	}
	public void pauseGPS(){
		stepDetector.stop();
		lm.removeUpdates(Loclist);
		active = false;
		GPSState = GPS_STATE_PAUSE;
	}
	public void resumeGPS(){
		stepDetector.start();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, Loclist);
		active = true;
		GPSState = GPS_STATE_START;
		time = System.currentTimeMillis();

	}
	public void stopGPS(){
		lm.removeUpdates(Loclist);
		stepDetector.stop();
		active = false;
		GPSState = GPS_STATE_STOP;
		SQLiteDataSource dataSource = new SQLiteDataSource(this);
		dataSource.open();
		dataSource.updateSQLWay(currentWay);
		dataSource.close();
		currentWay = null;
		hideNotify();
	}
	public int getGPSState(){
		return GPSState;
	}
	public SQLWay getWay(){
		return currentWay;
	}
	private Runnable timer = new Runnable() {
		@Override
		public void run() {
			if(active){
				currentWay.addTime(1000);
				Intent action = new Intent(UPDATE_TIME);
				sendBroadcast(action);
			}
			handler.postDelayed(this, 1000);
		}
	};
	private void showNotify(){
		//Bitmap icon;
		Intent contentIntent = new Intent(this, MainActivity.class);
		PendingIntent pContentIntent = PendingIntent.getActivity(this, 0, contentIntent, 0);

		Intent pauseIntent = new Intent(this, NotifyReceiver.class);
		pauseIntent.setAction(NotifyReceiver.ACTION_PAUSE);
		PendingIntent pPauseIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent resumeIntent = new Intent(this, NotifyReceiver.class);
		resumeIntent.setAction(NotifyReceiver.ACTION_RESUME);
		PendingIntent pResumeIntent = PendingIntent.getBroadcast(this, 0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		//builder.setLargeIcon(icon);
		builder.setContentText(currentWay.getTitle());
		builder.setContentTitle(getString(R.string.app_name));
		builder.setContentIntent(pContentIntent);

		if(active){
			builder.addAction(R.drawable.ic_action_pause, "", pPauseIntent);
		}else{
			builder.addAction(R.drawable.ic_action_play, "", pResumeIntent);
		}


		Notification n = builder.build();
		startForeground(NOTIFY_ID, n);
	}
	private void hideNotify(){
		stopForeground(true);
	}

	public class MyBinder extends Binder {
		public GPSservice getService() {
			return GPSservice.this;
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onStep() {
		if(currentWay != null && active){
			currentWay.addStep();
			Intent action = new Intent(NEW_STEPS);
			sendBroadcast(action);
		}

	}

	@Override
	public void passValue() {

	}

	public void setTitle(String value) {
		if(currentWay != null){
			currentWay.setTitle(value);
			SQLiteDataSource dataSource = new SQLiteDataSource(this);
			dataSource.open();
			dataSource.updateSQLWay(currentWay);
			dataSource.close();
		}else{
			tempTitle = value;
		}
	}
} 