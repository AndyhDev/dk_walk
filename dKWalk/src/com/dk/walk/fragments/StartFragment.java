package com.dk.walk.fragments;

import com.dk.walk.R;
import com.dk.walk.StartWayActivity;
import com.dk.walk.service.GPSservice;
import com.dk.walk.util.ServiceFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartFragment extends ServiceFragment implements OnClickListener {
	private static final String TAG = "StartFragment";
	
	private RelativeLayout layout;
	private TextView text;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.start, container, false);
		layout.setOnClickListener(this);
		
		text = (TextView) layout.findViewById(R.id.textView1);
		
		return layout;
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick");
		Intent activity = new Intent(getActivity(), StartWayActivity.class);
		startActivity(activity);
	}

	@Override
	public void onServiceConnected() {
		if(getService() != null){
			if(getService().getGPSState() != GPSservice.GPS_STATE_STOP){
				text.setText(getString(R.string.active_way));
				layout.setBackgroundColor(getResources().getColor(R.color.green));
			}else{
				text.setText(getString(R.string.start));
				layout.setBackgroundColor(getResources().getColor(R.color.yellow));
			}
		}
		
	}
	
	@Override
	public void onServiceDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewLocation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewSteps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewWay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateTime() {
		// TODO Auto-generated method stub
		
	}
	
}
