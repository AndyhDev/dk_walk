package com.dk.walk.util;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.dk.walk.service.GPSservice;

public abstract class ServiceFragment extends Fragment{
	private GPSservice service;
	private ServiceReceiver serviceReceiver;
	
	
	public GPSservice getService(){
		return service;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(service == null){
			Intent intent= new Intent(this.getActivity(), GPSservice.class);
			getActivity().startService(intent);
			getActivity().bindService(intent, mConnection, Context.BIND_ABOVE_CLIENT);
		}
		registerReceiver();
	}
	@Override
	public void onPause() {
		super.onPause();
		if(service != null){
			getActivity().unbindService(mConnection);
			service = null;
		}
		if(serviceReceiver != null){
			getActivity().unregisterReceiver(serviceReceiver);
			serviceReceiver = null;
		}
	}
	@Override
	public void onStop() {
		super.onStop();
		if(service != null){
			getActivity().unbindService(mConnection);
			service = null;
		}
		if(serviceReceiver != null){
			getActivity().unregisterReceiver(serviceReceiver);
			serviceReceiver = null;
		}
	}
	private void registerReceiver(){
		if(serviceReceiver == null){
			serviceReceiver = new ServiceReceiver();

			IntentFilter intentFilter = new IntentFilter(GPSservice.NEW_LOCATION);
			intentFilter.addAction(GPSservice.NEW_STEPS);
			intentFilter.addAction(GPSservice.NEW_WAY);
			getActivity().registerReceiver(serviceReceiver, intentFilter);
		}
	}
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			GPSservice.MyBinder b = (GPSservice.MyBinder) binder;
			service = b.getService();
			ServiceFragment.this.onServiceConnected();

		}

		public void onServiceDisconnected(ComponentName className) {
			service  = null;
			ServiceFragment.this.onServiceDisconnected();
		}
	};
	
	private class ServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(GPSservice.NEW_LOCATION)){
				onNewLocation();
			}else if(intent.getAction().equals(GPSservice.NEW_STEPS)){
				onNewSteps();
			}else if(intent.getAction().equals(GPSservice.NEW_WAY)){
				onNewWay();
			}
		}
	}
	public abstract void onServiceConnected();
	public abstract void onServiceDisconnected();
	public abstract void onNewLocation();
	public abstract void onNewSteps();
	public abstract void onNewWay();
}
