package com.dk.walk.fragments;

import java.text.DecimalFormat;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.walk.R;
import com.dk.walk.database.SQLWay;
import com.dk.walk.service.GPSservice;

public class MainFragment extends Fragment implements OnClickListener {
	private static final String TAG = "MainFragment";

	private RelativeLayout layout;
	private Button startBnt;
	private Button stopBnt;
	private TextView way;
	private TextView speed;
	private TextView steps;
	private TextView calories;

	private GPSservice service;
	private Context context;
	private ServiceReceiver serviceReceiver;
	private DecimalFormat format = new DecimalFormat("0.00");
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.main_fragment, container, false);

		context = getActivity();

		startBnt = (Button) layout.findViewById(R.id.start_bnt);
		stopBnt = (Button) layout.findViewById(R.id.stop_bnt);

		startBnt.setOnClickListener(this);
		stopBnt.setOnClickListener(this);

		way = (TextView) layout.findViewById(R.id.way);
		speed = (TextView) layout.findViewById(R.id.speed);
		steps = (TextView) layout.findViewById(R.id.steps);
		calories = (TextView) layout.findViewById(R.id.calories);
		
		return layout;
	}

	private void refresh(){
		if(service != null){
			SQLWay way = service.getWay();
			if(way != null){
				this.way.setText(format.format(way.getWay()) + " m");
				this.speed.setText(format.format(way.getSpeed()) + " km/h");
				this.steps.setText(way.getSteps().toString());
				this.calories.setText(String.valueOf(way.getCalories()));
				
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		if(service == null){
			Intent intent= new Intent(this.getActivity(), GPSservice.class);
			context.startService(intent);
			context.bindService(intent, mConnection, Context.BIND_ABOVE_CLIENT);
		}
		registerReceiver();
	}
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		if(service != null){
			context.unbindService(mConnection);
			service = null;
		}
		if(serviceReceiver != null){
			context.unregisterReceiver(serviceReceiver);
			serviceReceiver = null;
		}
	}
	@Override
	public void onStop() {
		super.onStop();
		if(service != null){
			context.unbindService(mConnection);
			service = null;
		}
		if(serviceReceiver != null){
			context.unregisterReceiver(serviceReceiver);
			serviceReceiver = null;
		}
	}
	private void registerReceiver(){
		if(serviceReceiver == null){
			serviceReceiver = new ServiceReceiver();

			IntentFilter intentFilter = new IntentFilter(GPSservice.NEW_LOCATION);
			intentFilter.addAction(GPSservice.NEW_STEPS);
			//intentFilter.addAction(PlayService.NEW_LOOP);
			context.registerReceiver(serviceReceiver, intentFilter);
		}
	}
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			GPSservice.MyBinder b = (GPSservice.MyBinder) binder;
			service = b.getService();
			refresh();

		}

		public void onServiceDisconnected(ComponentName className) {
			service  = null;
		}
	};
	private class ServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(GPSservice.NEW_LOCATION)){
				refresh();
				Log.d(TAG, GPSservice.NEW_LOCATION);
			}else if(intent.getAction().equals(GPSservice.NEW_STEPS)){
				refresh();
				Log.d(TAG, GPSservice.NEW_STEPS);
			}
		}
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.start_bnt){
			if(service != null){
				service.startGPS();
			}
		}else if(v.getId() == R.id.stop_bnt){
			if(service != null){
				service.stopGPS();
			}
		}
	}

}
